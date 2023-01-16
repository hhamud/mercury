(ns pocket.add
  (:require [pocket.helpers :refer [get-key]]
            [org.httpkit.client :as http]
            [clojure.data.json :as json]))


(def pocket-url "https://getpocket.com/v3/add")


(defn set-body
  "Body for the Post request to add Pocket API."
  [url & title]
  {:consumer_key (get-key :consumer-key)
   :access_token (get-key :access-token)
   :url url
   :title title})

(defn pocket-add
  "Add url to personal pocket account."
  [url title]
  (if-not (get-key :access-token)
    (auth))
  (let [{:keys [status headers body error] :as resp}
        @(http/post pocket-url
             :headers
             :body (json/write-str (set-body url title)))]
    (if error
      (println "Failed to add: " error))
    body))
