(ns pocket.server
  (:require [org.httpkit.server :as server])
  (:import (java.net ServerSocket BindException)))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;PORT FUNCTIONS;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn optional-keys [& {:keys [port timeout]
                        :or   {port 8000 timeout 100}}]
  (str "Port: " port ", timeout " timeout))

(defn- get-available-port
  "Return a random available TCP port in allowed range (between 1024 and 65535) or a specified one"
  ([] (get-available-port 0))
  ([port]
   (with-open [socket (ServerSocket. port)]
     (.getLocalPort socket))))

(defn make-range
  "Make a range of ports.
  Must be in the range 1024...65535"
  [from to]
  (range from (inc to)))

(defn get-port
  "Get an available TCP port according to the supplied options.
  - A preferred port: (get-port {:port 3000})
  - A vector of preferred ports: (get-port {:port [3000 3004 3010]})
  - Use the `make-range` helper in case you need a port in a certain (inclusive) range: (get-port {:port (make-range 3000 3005)})
  No args return a random available port"
  ([] (get-available-port))
  ([opts]
   (loop [port (:port opts)]
     (let [result
           (try
             (get-available-port (if (number? port) port (first port)))
             (catch Exception e (instance? BindException (.getCause e))))]
       (or result (recur (if (number? port) 0 (next port))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;SERVER FUNCTIONS;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn handler
  "A function that handles all requests from the server.
  Arguments: `req` is a ring request hash-map
  Return: ring response hash-map including :status :headers and :body"
  [req]
  {:status  200
   :headers {}
   :body    ()})

(defn create-server
  "A ring-based server listening to all http requests
  port is an Integer greater than 128"
  [port]
  (server/run-server handler {:port port}))

(defonce ^:private api-server (atom nil))

(defn stop-server
  "Gracefully shutdown the server, waiting 100ms "
  []
  (when-not (nil? @api-server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@api-server :timeout 100)
    (reset! api-server nil)))



(defn pocket-server
  "Start a httpkit server with a random port
  #' enables hot-reload of the handler function and anything that code calls"
  [fn & args]
  (let [ip "127.0.0.1"
        port 8000
        num-args (count (:arglists fn))
        arg-list (take num-args args)]

    (println (format "INFO: Starting httpkit server on IP:%s port:%s" ip, port))
    (apply fn arg-list)
    (reset! api-server (server/run-server #'handler {:port port}))))

