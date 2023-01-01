(ns app.pocket
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [aero.core :refer [read-config]]))

;; parameters
(def default-url "https://www.google.com")
(def headers-json
  {"X-Accept" "application/json"
   "Content-Type" "application/json"})
(def pocket-url "https://getpocket.com/v3/oauth/")

;; Functions
(defn consumer-key
  "Read config file for consumer key"
  [file]
  (:consumer_key (read-config file)))

(defn get-code-token
  "Gets the code token from pocket API with consumer key and the redirect link."
  [consumer-key, redirect-uri]
  (let [json-resp
        (http/post (str pocket-url "request")
         {:headers headers-json
          :body (json/write-str {:redirect_uri redirect-uri
                                 :consumer_key consumer-key})})
        list-resp (get (json/read-str (json-resp :body)) "code")]
    list-resp))

(defn redirect-link
  "Create authorisation link with request-token and redirect uri."
  [request-token redirect-uri]
  (println (format "https://getpocket.com/auth/authorize?request_token=%s&redirect_uri=%s" request-token redirect-uri)))

(defn get-auth-token
  "Gets authorisation token from Pocket API with the consumer token and code token generated from get-code-token"
  [consumer-key code-token]
  (let [json-resp
        (http/post (str pocket-url "authorize")
                   {:headers headers-json
                    :body (json/write-str {:consumer_key consumer-key
                                           :code code-token})
                    :debug true
                    :throw-exceptions false})

        json-resp]))
