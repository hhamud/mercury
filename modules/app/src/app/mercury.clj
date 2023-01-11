(ns app.mercury
  (:require [pocket.retrieve :refer [get-articles]]
            [clojure.data.json :refer [read-str]]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello"))

(def home (System/getProperty "user.home"))
(def base-dir (str home "/Documents/downloaded_articles/"))
(def html-dir (str base-dir "html_files/"))
(def markdown-dir (str base-dir "markdown_files/"))

(defn parse-title
  "Makes unix friendly titles."
  [title]
  (str/replace (str/replace title #"[^a-zA-Z0-9_.-]+" "_") #"^_|_$" ""))

(defn create-hash-map
  "helper function for creating a hashmap out of the pocket api."
  [acc item]
  (assoc acc (parse-title (get item "resolved_title")) (get item "resolved_url")))

(defn get-article-links
  "Fetch all article links stored in Pocket."
  []
  (let [resp  (-> (get-articles)
                  read-str
                  (get "list")
                  vals)
        body (reduce create-hash-map  (hash-map) resp)]
    body))

(defn download-article
  "Download any html article."
  [name link path-to-folder]
  (spit (str path-to-folder (str name ".html")) (slurp link)))

(defn html-to-markdown
  "Converts html file to markdown."
  [html-file output-file]
  (let [command (str "pandoc " html-file " -f html -t markdown -o " output-file)]
    (sh command)))

(defn pocket-download
  "Downloads pocket articles and converts it all into markdown."
  []
  )
