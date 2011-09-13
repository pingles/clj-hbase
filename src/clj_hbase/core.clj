(ns clj-hbase.core
  (:import [org.apache.hadoop.conf Configuration]
           [org.apache.hadoop.hbase HBaseConfiguration HTableDescriptor HColumnDescriptor]
           [org.apache.hadoop.hbase.client Get Result Put HTable HConnectionManager HBaseAdmin]))

(defprotocol ToClojure
  (to-clojure [_]))

(extend-protocol ToClojure
  HColumnDescriptor
  (to-clojure [x] {:name (.getName x)})
  HTableDescriptor
  (to-clojure [x]
    {:name (.getName x)
     :families (map to-clojure (.getFamilies x))}))

(defn create-config
  []
  (HBaseConfiguration/create))

(defn create-connection
  [config]
  (HConnectionManager/getConnection config))

(declare ^:dynamic *connection*)
(declare ^:dynamic *table*)

(defn list-tables
  ([] (list-tables *connection*))
  ([connection] (map to-clojure (.listTables connection))))

(defn disable-table
  [administrator name]
  (.disableTable administrator name))

(defn delete-table
  [administrator name]
  (.deleteTable administrator name))

(defn create-column-descriptor
  [name]
  (HColumnDescriptor. name))

(defn create-table-descriptor
  "Name is expected to be a byte array"
  [name family-names]
  (let [descriptors (map create-column-descriptor family-names)
           table (HTableDescriptor. name)]
       (doseq [d descriptors] (.addFamily table d))
       table))

(defn create-table
  "Creates a table with optional column families: strings."
  [administrator ^String name & family-names]
  (.createTable administrator (create-table-descriptor name family-names)))

(defn put
  "Stores value for row row-key in column with timestamp. Timestamp
   should be a long. row-key, column and value should all be byte[]."
  ([row-key family column timestamp value]
     (put *table* row-key family column timestamp value))
  ([table row-key family column timestamp value]
     (.put table (doto (Put. row-key)
                   (.add family column timestamp value)))))

(defn fetch
  "Executes a Get to load all data for the specified row-key"
  ([row-key]
     (get2 *table* row-key))
  ([table row-key]
     (.getMap (.get table (Get. row-key)))))

(defmacro with-table
  [config name & body]
  `(binding [*table* (HTable. ~config ~name)]
     ~@body))

(defn create-administrator
  [configuration]
  (HBaseAdmin. configuration))

(defmacro with-connection
  [config & body]
  `(binding [*connection* (create-connection ~config)]
     (try ~@body
          (finally (HConnectionManager/deleteConnection ~config true)))))

