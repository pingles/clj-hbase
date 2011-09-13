(defproject clj-hbase "0.0.1-SNAPSHOT"
  :description "HBase client for Clojure."
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.apache.hbase/hbase "0.90.3-cdh3u1"]]
  :repositories {"cdh.snapshots.repo" "https://repository.cloudera.com/content/repositories/releases/"}
  :resources-path "resources")
