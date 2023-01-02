(ns pocket.helpers
  (:require [aero.core :refer [read-config]]))


(defn write-config
  "Writes key value pair to config.edn."
  [key value]
  (let [data (read-string (slurp "config.edn"))
        updated-data (assoc data key value)
        edn-string (pr-str updated-data)]
    (spit "config.edn" edn-string)))
