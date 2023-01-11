(ns pocket.auth
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [aero.core :refer [read-config]]
            [clojure.java.browse :as browse]
            [pocket.helpers :refer [write-config, headers-json, default-url, get-key, pocket-url]]
            [pocket.server :refer [pocket-server, get-port, stop-server]]))

;; parameters
(def server-url "127.0.0.1:8000")

;; Functions
(defn get-code-token
  "Gets the code token from pocket API with consumer key and the redirect link."
  [consumer-key, redirect-uri]
  (let [json-resp
        (http/post (str pocket-url "request")
                   {:headers headers-json
                    :body (json/write-str {:redirect_uri redirect-uri
                                           :consumer_key consumer-key})
                    })
        list-resp (get (json/read-str (json-resp :body)) "code")]
    (write-config :code-token list-resp)))

(defn redirect-link
  "Create authorisation link with request-token and redirect uri."
  [request-token redirect-uri]
  (browse/browse-url (format "https://getpocket.com/auth/authorize?request_token=%s&redirect_uri=%s" request-token redirect-uri)))

(defn auth
  "Authenticate new user with Pocket API."
  []
  (if-not (get-key :access-token)
    (do
    (pocket-server)
    (get-code-token (get-key :consumer-key) server-url)
    (redirect-link (get-key :code-token) server-url)))
  (println "User already authorised"))
