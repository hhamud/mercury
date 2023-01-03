(ns pocket.auth
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [aero.core :refer [read-config]]
            [clojure.java.browse :as browse]
            [pocket.helpers :refer [write-config, headers-json, default-url, get-key]]
            [pocket.server :refer [pocket-server, get-port]]))

;; parameters
(def pocket-url "https://getpocket.com/v3/oauth/")
(def server-url "127.0.0.1:8000")

;; Functions
(defn consumer-key
  "Read config file for consumer key"
  [file]
  (:consumer-key (read-config file)))

(defn get-code-token
  "Gets the code token from pocket API with consumer key and the redirect link."
  [consumer-key, redirect-uri]
  (let [json-resp
        (http/post (str pocket-url "request")
                   {:headers headers-json
                    :body (json/write-str {:redirect_uri redirect-uri
                                           :consumer_key consumer-key})
                    :debug true
                    :throw-exceptions false})
        list-resp (get (json/read-str (json-resp :body)) "code")]
    list-resp))

(defn redirect-link
  "Create authorisation link with request-token and redirect uri."
  [request-token redirect-uri]
  (browse/browse-url (format "https://getpocket.com/auth/authorize?request_token=%s&redirect_uri=%s" request-token redirect-uri)))

(defn get-auth-token
  "Gets authorisation token from Pocket API with the consumer token and code token generated from get-code-token"
  [consumer-key code-token]
  (let [json-resp
        (http/post (str pocket-url "authorize")
                   {:headers headers-json
                    :body (json/write-str {:consumer_key consumer-key
                                           :code code-token})
                    })]
    json-resp))

(get-auth-token (get-key :consumer-key) (get-key :code-token))


(defn auth
  "Authenticate new user with Pocket API."
  []
  (if-not (:code-token (read-config "config.edn"))
    (-> "config.edn"
        consumer-key
        (pocket-server (get-auth-token (get-key :consumer-key) (get-key :code-token)))
        (get-code-token server-url)
        (redirect-link server-url))
    (let [consumer-key (consumer-key "config.edn")
          code-token (:code-token (read-config "config.edn"))
          auth-resp (get-auth-token consumer-key code-token)
          auth-token (get (json/read-str (auth-resp :body)) "access_token")]

(defn authenticate
  []
  (let [consumer-key (consumer-key "config.edn")])
  )
