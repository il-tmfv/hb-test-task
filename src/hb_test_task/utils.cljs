(ns hb-test-task.utils
  (:require [clojure.string :as s]))

(defn get-country-code-by-id [country-db id]
  (let [country (some #(when (= (:id %) id) %) country-db)]
    (if (nil? country) "" (:country-code country))))

(defn get-phone-format-by-id [country-db id]
  (let [country (some #(when (= (:id %) id) %) country-db)]
    (if (nil? country) "" (:phone-format country))))

(defn get-country-id-by-phone [country-db phone]
  (let [country (some #(when (.startsWith phone (:country-code %)) %) country-db)]
    (if (nil? country) "0" (:id country))))

(defn strip-phone-number
  "Removes all non-digit chars from a phone number. Also leaves `+` sign"
  [phone-number]
  (s/replace phone-number #"[^0-9\+]+" ""))

(defn generate-replace-pattern-by-phone-format
  "Generates replace pattern for string/replace func. E.g. `+7 (###)` becomes `+7 ($1$2$3)`"
  [phone-format]
  (let [index (atom 0)]
    (s/replace phone-format "#" (fn [] (swap! index inc) (str "$" @index)))))

(defn generate-regex-by-phone-format
  "Generates a regex by a phone format"
  [phone-format]
  (-> phone-format
      (s/replace #"." #(get
                         {"+" "\\+"
                          "(" "\\("
                          ")" "\\)"
                          " " " "
                          "#" "\\d"} % %))
      re-pattern))

(defn generate-stripped-regex-by-phone-format
  "Generates a stripped (removes all extra chars like parenthesis) regex by a phone format"
  [phone-format]
  (-> phone-format
      (s/replace #"." #(get
                         {"+" "\\+"
                          "(" ""
                          ")" ""
                          " " ""
                          "#" "(\\d)"} % %))
      re-pattern))

(defn format-phone-number
  "Formats phone number according to a format"
  [format phone-number]
  (let [replace-pattern (generate-replace-pattern-by-phone-format format)
        stripped-phone-number (strip-phone-number phone-number)
        regex (generate-stripped-regex-by-phone-format format)]
    (s/replace stripped-phone-number regex replace-pattern)))

(defn phone-number-valid? [format phone-number]
  (let [regex (generate-regex-by-phone-format format)]
    (-> (re-matches regex phone-number) nil? not)))
