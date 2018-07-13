(ns components.phone-input)

(defn phone-input [{:keys [input-value select-value options on-select-change on-input-change]}]
  [:div.phone-input
   [:select
    {:value     select-value
     :on-change on-select-change}
    (map #(vector
            :option {:key (:id %)} (str (:flag %) " " (:label %)))
         options)]
   [:input {:value     input-value
            :on-change on-input-change}]])
