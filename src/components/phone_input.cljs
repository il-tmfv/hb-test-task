(ns components.phone-input)

(defn phone-input [{:keys [hint
                           input-value
                           select-value
                           options
                           on-input-blur
                           on-select-change
                           on-input-change]}]
  [:div
   [:div.phone-input
    [:div..phone-input__select
     [:select
      {:value     @select-value
       :on-change on-select-change}
      (map #(vector
              :option {:key (:id %) :value (:id %)}
              (str (:flag %) " " (:label %)))
           options)]]
    [:input.phone-input__input
     {:value     @input-value
      :on-blur   (partial on-input-blur @select-value)
      :on-change on-input-change}]]
   [:div.phone-input__hint hint]])
