(ns components.phone-input
  (:require [reagent.core :as r]
            [hb-test-task.utils :refer [strip-forbidden-chars
                                        generate-hint
                                        phone-number-valid?
                                        get-phone-format-by-id
                                        get-country-code-by-id
                                        get-country-id-by-phone
                                        check-enough-digits
                                        strip-phone-number
                                        format-phone-number]]))

(def country-db [{:id           "0"
                  :label        "?"
                  :flag         "🏴"
                  :country-code "?"
                  :phone-format ""}
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

(defn phone-input [{:keys [title
                           value
                           on-change]}]
  (let [selected-country (r/atom "0")
        input-value (r/atom "")
        error? (r/atom false)
        hint (r/atom "")
        on-select-change (fn [e]
                           (let [new-country-id (if (string? e)
                                                  e
                                                  (-> e .-target .-value))
                                 country-code (get-country-code-by-id country-db new-country-id)]
                             (reset! selected-country new-country-id)
                             (reset! input-value country-code)))
        on-input-change (fn [format? e]
                          (let [new-phone-number (->
                                                   (if (string? e)
                                                     e
                                                     (-> e .-target .-value))
                                                   strip-forbidden-chars)
                                country-id (get-country-id-by-phone country-db new-phone-number)
                                phone-format (get-phone-format-by-id country-db country-id)
                                checked-phone-number (check-enough-digits new-phone-number @input-value phone-format)]
                            (reset! input-value (if format? (format-phone-number phone-format checked-phone-number) checked-phone-number))
                            (reset! selected-country country-id)))
        on-input-blur (fn [e]
                        (let [new-phone-number (-> e .-target .-value)
                              phone-format (get-phone-format-by-id country-db @selected-country)
                              formatted-phone-number (format-phone-number phone-format new-phone-number)
                              has-error? (phone-number-valid? phone-format formatted-phone-number)]
                          (reset! error? (not has-error?))
                          (reset! input-value formatted-phone-number)
                          (on-change (strip-phone-number formatted-phone-number))))
        ]
    (add-watch selected-country :selected-country-watcher
               (fn [_ _ _ new-country-id]
                 (reset! hint (generate-hint (get-phone-format-by-id country-db new-country-id)))))
    (on-input-change true @value)
    (fn [] [:div
            [:div.phone-input__title title]
            [:div.phone-input
             [:div.phone-input__select
              [:select
               {:value     @selected-country
                :on-change on-select-change}
               (map (fn [option]
                      (let [id (:id option)
                            flag (:flag option)
                            label (:label option)
                            disabled (= id "0")]
                        (vector
                          :option {:key id :value id :disabled disabled}
                          (str flag " " label))))
                    country-db)]]
             [:input
              {:class     ["phone-input__input" (when @error? "phone-input__input-error")]
               :value     @input-value
               :on-blur   on-input-blur
               :on-change (partial on-input-change false)}]]
            [:div.phone-input__hint @hint]])))
