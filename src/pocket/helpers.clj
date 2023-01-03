(ns pocket.helpers
  (:require [aero.core :refer [read-config]]))



;;Parameters

(def default-url "pocketapp93023:authorizationFinished")
(def headers-json
  {"X-Accept" "application/json"
   "Content-Type" "application/json"})

;;Functions

(defn write-config
  "Writes key value pair to config.edn."
  [key value]
  (let [data (read-string (slurp "config.edn"))
        updated-data (assoc data value key)
        edn-string (pr-str updated-data)]
    (spit "config.edn" edn-string)))


(defn get-key
  "Read config file for consumer key"
  [key]
  (key (read-config "config.edn")))
