(ns hb-test-task.core
  (:require [reagent.core :as r]
            [components.app :refer [app]]
            [components.phone-input :refer [phone-input]]))

(enable-console-print!)

(println "This text is printed from src/hb-test-task/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (r/atom {:selected-country "0"
                            :input-value      ""}))

(def selected-country (r/cursor app-state [:selected-country]))
(def input-value (r/cursor app-state [:input-value]))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(defn on-select-change [e]
  (let [new-country-id (-> e .-target .-value)]
    (reset! selected-country new-country-id)))

(defn on-input-change [e]
  (let [new-phone-number (-> e .-target .-value)]
    (reset! input-value new-phone-number)))

(def country-db [{:id           "0"
                  :label        "??"
                  :flag         "🏴"
                  :country-code "+44"
                  :phone-format "+44 (##) #### ####"}
                 {:id           "1"
                  :label        "UK"
                  :flag         "🇬🇧"
                  :country-code "+44"
                  :phone-format "+44 (##) #### ####"}
                 {:id           "2"
                  :label        "US"
                  :flag         "🇺🇸"
                  :country-code "+1"
                  :phone-format "+1 (###) ### ####"}
                 {:id           "3"
                  :label        "RU"
                  :flag         "🇷🇺"
                  :country-code "+7"
                  :phone-format "+7 (###) ### ####"}])

(r/render [app
           [phone-input {:options          country-db
                         :input-value      input-value
                         :hint             "hint text"
                         :on-input-change  on-input-change
                         :on-select-change on-select-change
                         :select-value     selected-country}]]
          (.getElementById js/document "app"))
