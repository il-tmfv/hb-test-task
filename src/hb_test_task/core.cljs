(ns hb-test-task.core
  (:require [reagent.core :as r]
            [components.app :refer [app]]
            [components.phone-input :refer [phone-input]]))

(enable-console-print!)

(println "This text is printed from src/hb-test-task/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (r/atom {:phone-number-1 "" :phone-number-2 ""}))
(defonce phone-number-1 (r/cursor app-state [:phone-number-1]))
(defonce phone-number-2 (r/cursor app-state [:phone-number-2]))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(r/render [app
           [:div
            [phone-input {:value     phone-number-1
                          :on-change #(reset! phone-number-1 (-> % .-target .-value))}]
            [phone-input {:value     phone-number-2
                          :on-change #(reset! phone-number-2 (-> % .-target .-value))}]]]
          (.getElementById js/document "app"))
