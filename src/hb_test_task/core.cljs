(ns hb-test-task.core
  (:require [reagent.core :as r]
            [hb-test-task.utils :refer [get-phone-format-by-id get-country-code-by-id get-country-id-by-phone format-phone-number]]
            [components.app :refer [app]]
            [components.phone-input :refer [phone-input]]))

(enable-console-print!)

(println "This text is printed from src/hb-test-task/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(def country-db [{:id           "0"
                  :label        "?"
                  :flag         "🏴"
                  :country-code "?"
                  :phone-format "?"}
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
                  :phone-format "+7 (###) ### ## ##"}])


(defonce app-state (r/atom {:selected-country "0"
                            :input-value      ""}))

(def selected-country (r/cursor app-state [:selected-country]))
(def input-value (r/cursor app-state [:input-value]))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(defn on-select-change [country-db e]
  (let [new-country-id (-> e .-target .-value)
        country-code (get-country-code-by-id country-db new-country-id)]
    (reset! selected-country new-country-id)
    (reset! input-value country-code)))

(defn on-input-change [country-db e]
  (let [new-phone-number (-> e .-target .-value)
        country-id (get-country-id-by-phone country-db new-phone-number)]
    (reset! input-value new-phone-number)
    (reset! selected-country country-id)))

(defn on-input-blur [country-db select-value e]
  (let [new-phone-number (-> e .-target .-value)
        phone-format (get-phone-format-by-id country-db select-value)
        formatted-phone-number (format-phone-number phone-format new-phone-number)]
   (reset! input-value formatted-phone-number)))

(r/render [app
           [phone-input {:options          country-db
                         :input-value      input-value
                         :hint             "hint text"
                         :on-input-blur    (partial on-input-blur country-db)
                         :on-input-change  (partial on-input-change country-db)
                         :on-select-change (partial on-select-change country-db)
                         :select-value     selected-country}]]
          (.getElementById js/document "app"))
