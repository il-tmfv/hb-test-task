(ns hb-test-task.utils)

(defn get-country-code-by-id [country-db id]
  (let [country (some #(when (= (:id %) id) %) country-db)]
    (if (nil? country) "" (:country-code country))))

(defn get-country-id-by-phone [country-db phone]
  (let [country (some #(when (.startsWith phone (:country-code %)) %) country-db)]
    (if (nil? country) "0" (:id country))))
