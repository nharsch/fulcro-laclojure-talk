(ns app.core
  (:require
   [goog.object :as gobj]
   [goog.string :as gstring]
    goog.string.format
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   )
  )



(defn init []
  (js/console.log "starting")
  ;; need to tell the lib where to load the worker from, also using same CDN
)
