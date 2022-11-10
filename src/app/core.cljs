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

(defsc Root [this props]
  (dom/div "TODO"))




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
  (pprint (app/current-state app))
  (::app/state-atom app)
  (reset! (::app/state-atom app))
  (app/mount! app Root "fulcro-app")
  (app/schedule-render! app)
  ;; (merge/merge-component! app Person {:person/id :you :person/name "You"})
  (pprint (app/current-state app))
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
