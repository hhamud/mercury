(ns app.pocket
 (:require [environ.core :refer [env]]
            [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.string :as str]))

;; Functions
(defn read-env
  "Reads in a local .env file and returns the contents as a map of key-value pairs."
  []
  (let [env-string (slurp "./.env")
        env-lines (str/split-lines env-string)
        env-pairs (map #(str/split % #"=") env-lines)]
    (into {} env-pairs)))


(defn get-code-token []
    (let [consumer-token (get (read-env) "CONSUMER_KEY")
           json-resp (http/post "https://getpocket.com/v3/oauth/request"
                               { :headers { "Content-Type" "application/json"
                                          "X-Accept" "application/json"}}
                                { :body (json/write-str {:redirect_uri "http://www.google.com"
                                                      :consumer_key consumer-token}) } )
          list-resp (json/read-str json-resp)
          data (:code list-resp)]
      ))
(get-code-token)

(defn get-auth-token []
  (let [json-resp
        (http/post "https://getpocket.com/v3/oauth/authorize"
                             { :headers { "Content-Type" "application/json"
                                        "X-Accept" "application/json"}}
                             { :body (json/write-str {:consumer_key consumer-token
                                                    :code code-token})})

        list-resp (json/read-str json-resp)
        data (:access_token list-resp)]
    (set! auth-token data)))


(defn auth []
  (do
    (get-code-token)
    (get-auth-token)))

