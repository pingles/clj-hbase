# clj-hbase

HBase client for Clojure.

Although other HBase clients exist I wanted to learn more about the API and evolve a client library for experimenting with HBase. This repository will chart my progress :)

## Usage

    ; config currently is read from the ./resources directory
    ; where it expects to have core-site.xml and hbase-site.xml
    ; that tell the client how to connect to the cluster.
    (def config (create-config))

### Creating and Listing tables

    (with-connection config
      (list-tables))
    ; ()
    
    (def admin (create-administrator config))
    (create-table admin "MyFirstTable")
    (with-connection config
      (list-tables))

    ; ({:name "MyFirstTable"})

### Storing rows

    (with-table config "PaulsFirstTable"
      (put (.getBytes "key1")
           (.getBytes "family1")
           (.getBytes "colA")
           1 ; timestamp/version
           (.getBytes "This is my value")))

### Retrieving rows

    (with-table config "PaulsFirstTable"
      (fetch (.getBytes "key1")))

## License

Copyright &copy; 2011 Paul Ingles

Distributed under the Eclipse Public License, the same as Clojure.
