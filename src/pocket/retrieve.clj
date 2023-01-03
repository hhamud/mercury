(ns pocket.retrieve
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [aero.core :refer [read-config]]
            [clojure.java.browse :as browse]
            [pocket.helpers :as helper]
            [pocket.auth :refer [auth]]))

;; parameters
(def pocket-get-url "https://getpocket.com/v3/get")

;; check authentication as well
;; better clojure error handling
;; raising exceptions

(defn set-body
  "Sets the body of the request for retrieval of articles from Pocket API."
  [state content-type]
  {:consumer_key (:consumer-key (read-config "config.edn"))
   :access_token (:auth-token (read-config "config.edn"))
   :state state
   :content-type content-type})

(defn get-articles
  "Gets all the articles from personal pocket account."
  []
  (if-not (:auth-token (read-config "config.edn"))
    (auth))
  (let [json-resp (http/post pocket-get-url
                            {:headers helper/headers-json
                             :body (json/write-str (set-body "all" "article"))
                             :throw-entire-message? true})]
    json-resp))

(get-articles)
