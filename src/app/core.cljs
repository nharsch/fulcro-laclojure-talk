(ns app.core
  (:require
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.data-fetch :as df]
            [com.fulcrologic.fulcro.dom :as dom :refer [div p ul li]]
            [com.fulcrologic.fulcro.algorithms.lookup :as ah]
            [com.fulcrologic.fulcro.algorithms.merge :as merge]
            [com.fulcrologic.fulcro.algorithms.data-targeting :as targeting]
            [com.fulcrologic.fulcro.algorithms.denormalize :as fdn]
            [com.fulcrologic.fulcro.algorithms.normalize :as fnorm]
            [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
            [com.fulcrologic.fulcro.algorithms.tempid :as tempid]
            [com.fulcrologic.fulcro.algorithms.form-state :as fs]
            [com.fulcrologic.fulcro.algorithms.tx-processing.synchronous-tx-processing :as stx]
            [com.fulcrologic.fulcro.networking.http-remote :refer [fulcro-http-remote]]
            [com.fulcrologic.fulcro.ui-state-machines :as uism]
            [com.fulcrologic.fulcro.react.hooks :as hooks]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
            [com.fulcrologic.fulcro.routing.legacy-ui-routers :as r]
            [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
            [edn-query-language.core :as eql]
            [cljs.pprint :refer [pprint]]
            )
  )
(defonce app (app/fulcro-app))

(defsc Person [this {:person/keys [name age]} {:keys [onDelete]}]
  {:query         [:person/name :person/age]
   :initial-state (fn [{:keys [name age] :as params}] {:person/name name :person/age age})}
  (dom/li
    (dom/h5 (str name " (age: " age ")") (dom/button {:onClick #(onDelete name)} "X"))))

(def ui-person (comp/factory Person {:keyfn :person/name}))

(defsc PersonList [this {:list/keys [label people]}] ;
  {:query [:list/label {:list/people (comp/get-query Person)}]
   :initial-state
          (fn [{:keys [label]}]
            {:list/label  label
             :list/people (if (= label "Friends")
                                   [(comp/get-initial-state Person {:name "Sally" :age 32})
                                    (comp/get-initial-state Person {:name "Joe" :age 22})]
                                   [(comp/get-initial-state Person {:name "Fred" :age 11})
                                    (comp/get-initial-state Person {:name "Bobby" :age 55})])})}
  (let [delete-person (fn [name] (println label "asked to delete" name))]
    (dom/div
      (dom/h4 label)
      (dom/ul
        (map (fn [p] (ui-person (comp/computed p {:onDelete delete-person}))) people)))))

(def ui-person-list (comp/factory PersonList))

(defsc Root [this {:keys [friends enemies]}]
  {:query         [{:friends (comp/get-query PersonList)}
                   {:enemies (comp/get-query PersonList)}]
   :initial-state (fn [params] {:friends (comp/get-initial-state PersonList {:label "Friends"})
                                :enemies (comp/get-initial-state PersonList {:label "Enemies"})}) }
  (dom/div
    (ui-person-list friends)
    (ui-person-list enemies)))
(comp/get-query Root)

;; (defsc Person
;;   [this {:person/keys [name age] :as props}]
;;   {:query [:person/id :person/name :person/age]
;;    :ident (fn [] [:person/id (:person/id props)])}
;;   (div
;;     (p "Name: " name ", age:" age)))
;; (comp/get-query Person)

;; (def ui-person (comp/factory Person))

;; (defsc PersonList [this {:list/keys [label people]}]
;;   (dom/div
;;     (dom/h4 label)
;;     (dom/ul
;;       (map ui-person people))))
;; ;; (comp/get-query PersonList)

;; (def ui-person-list (comp/factory PersonList))

;; (comp/get-query Root)


(comment
  (::app/state-atom app)
  (pprint (app/current-state app))
  (reset! (::app/state-atom app))
  (app/mount! app Root "fulcro-app")
  (app/schedule-render! app)
  ;; (merge/merge-component! app Person {:person/id :you :person/name "You"})
  (pprint (app/current-state app))
  (fdn/db->tree [{:friends [:list/label]}] (comp/get-initial-state Root {}) {})
  )

(defn ^:export init []
  (app/mount! app Root "app")
  (js/console.log "Loaded"))

(defn ^:export refresh []
  ;; re-mounting will cause forced UI refresh
  (app/mount! app Root "app")
  ;; 3.3.0+ Make sure dynamic queries are refreshed
  (comp/refresh-dynamic-queries! app)
  (js/console.log "Hot reload"))
