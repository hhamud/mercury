(ns pocket.retrieve
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [org.httpkit.sni-client :as sni-client]
            [clojure.string :as str]
            [aero.core :refer [read-config]]
            [clojure.java.browse :as browse]
            [pocket.helpers :refer [headers-json, get-key]]
            [pocket.auth :refer [auth]]))

;; Change default client for your whole application:
(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

;; parameters
(def pocket-get-url "https://getpocket.com/v3/get")

;; Functions
(defn set-body
  "Sets the body of the request for retrieval of articles from Pocket API."
  [state content-type]
  {:consumer_key (get-key :consumer-key)
   :access_token (get-key :access-token)
   :state state
   :contentType content-type
   })

(defn get-articles
  "Gets all the articles from personal pocket account."
  []
  (if-not (get-key :access-token)
    (auth))
  (let [{:keys [status headers body error] :as resp}
        @(http/post pocket-get-url
                    {:headers headers-json
                     :body (json/write-str (set-body "all" "article"))})]
    (if error
      (println "Failed request: " error)
      body)))

