(ns clj-hbase.core
  (:import [org.apache.hadoop.conf Configuration]
           [org.apache.hadoop.hbase HBaseConfiguration HTableDescriptor]
           [org.apache.hadoop.hbase.client HTable HConnectionManager HBaseAdmin]))

(defprotocol ToClojure
  (to-clojure [_]))

(extend-protocol ToClojure
  HTableDescriptor
  (to-clojure [^HTableDescriptor x]
    {:name (String. (.getName x))}))

(defn create-config
  []
  (HBaseConfiguration/create))

(defn create-connection
  [config]
  (HConnectionManager/getConnection config))

(declare ^:dynamic *connection*)

(defn list-tables
  ([] (list-tables *connection*))
  ([connection] (map to-clojure (.listTables connection))))

(defn create-table
  [administrator ^String name]
  (.createTable administrator (HTableDescriptor. (.getBytes name))))

(defn create-administrator
  [configuration]
  (HBaseAdmin. configuration))

(defmacro with-connection
  [config & body]
  `(binding [*connection* (create-connection ~config)]
     (try ~@body
          (finally (HConnectionManager/deleteConnection ~config true)))))

