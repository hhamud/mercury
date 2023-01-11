(ns pocket.retrieve
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [aero.core :refer [read-config]]
            [clojure.java.browse :as browse]
            [pocket.helpers :refer [headers-json, get-key]]
            [pocket.auth :refer [auth]]))

;; parameters
(def pocket-get-url "https://getpocket.com/v3/get")

(defn set-body
  "Sets the body of the request for retrieval of articles from Pocket API."
  [state content-type]
  {:consumer_key (get-key :consumer-key)
   :access_token (get-key :access-token)
   :state state
   :content-type content-type})

(defn get-articles
  "Gets all the articles from personal pocket account."
  []
  (if-not (get-key :access-token)
    (auth))
  (let [json-resp (http/post pocket-get-url
                             {:headers headers-json
                              :body (json/write-str (set-body "all" "article"))})]
    json-resp))
