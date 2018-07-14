(ns components.state-panel)

(defn state-panel [phone-number-1 phone-number-2]
  [:div.state-panel
   "Phone numbers in app state:"
   [:div "1: " @phone-number-1]
   [:div "2: " @phone-number-2]])
