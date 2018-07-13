(ns hb-test-task.utils)

(defn get-country-code-by-id [country-db id]
  (let [country (some #(when (= (:id %) id) %) country-db)]
    (if (nil? country) "" (:country-code country))))
