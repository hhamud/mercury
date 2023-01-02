(ns pocket.retrieve
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [aero.core :refer [read-config]]
            [clojure.java.browse :as browse]
            [pocket.helpers :refer [write-config]]))

;; parameters
(def pocket-get-url "https://getpocket.com/v3/get")
(def retrieve-body {:state "all" :content-type "article" :consumer-token "" :access-token ""})
