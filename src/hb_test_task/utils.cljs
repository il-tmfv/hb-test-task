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

(defn strip-forbidden-chars
  "Removes forbidden chars from the phone number"
  [phone-number]
  (-> phone-number
      (s/replace #"[^0-9\+\(\)\s]" "")
      (s/replace #"(?<=.)\+(?=\d?)" "") ))

(defn strip-phone-number
  "Removes all non-digit chars from the phone number. Also leaves `+` sign"
  [phone-number]
  (s/replace phone-number #"[^0-9\+]+" ""))

(defn generate-replace-pattern-by-phone-format
  "Generates replace pattern for string/replace func. E.g. `+7 (###)` becomes `+7 ($1$2$3)`"
  [phone-format]
  (let [index (atom 0)]
    (s/replace phone-format "#" (fn [] (swap! index inc) (str "$" @index)))))

(defn generate-regex-by-phone-format
  "Generates a regex by the phone format"
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
  "Generates a stripped (removes all extra chars like parenthesis) regex by the phone format"
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
  "Formats the phone number according to the format"
  [format phone-number]
  (let [replace-pattern (generate-replace-pattern-by-phone-format format)
        stripped-phone-number (strip-phone-number phone-number)
        regex (generate-stripped-regex-by-phone-format format)]
    (s/replace stripped-phone-number regex replace-pattern)))

(defn phone-number-valid? [format phone-number]
  (let [regex (generate-regex-by-phone-format format)]
    (-> (re-matches regex phone-number) nil? not)))

(defn generate-hint
  "Generates hint about the phone number format"
  [format]
  (let [formatted-example (s/replace format "#" "1")
        not-formatted-example (strip-phone-number formatted-example)]
    (when (not (empty? formatted-example))
      (str "e.g. " not-formatted-example " or " formatted-example))))

(defn count-char-in-str
  "Counts how many chars in string matches provided regex"
  [regex str]
  (->> str seq (filter #(re-matches regex %)) count))

(defn check-enough-digits
  "Returns `user-input` if user still can enter new digit, otherwise returns `current-phone-number`"
  [user-input current-phone-number format]
  (let [user-input-number-digits (count-char-in-str #"\d" user-input)
        format-digits (count-char-in-str #"\d|#" format)]
    (if (= format-digits 0)
      user-input
      (if (> user-input-number-digits format-digits)
        current-phone-number
        user-input))))
