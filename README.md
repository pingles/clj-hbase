# clj-hbase

HBase client for Clojure.

Although other HBase clients exist I wanted to learn more about the API and evolve a client library for experimenting with HBase. This repository will chart my progress :)

## Usage

    ; config currently is read from the ./resources directory
    ; where it expects to have core-site.xml and hbase-site.xml
    ; that tell the client how to connect to the cluster.
    (def config (create-config))
    (def admin (create-administrator config))
    (with-connection config
      (list-tables))
    ; ()
    
    (create-table "MyFirstTable")
    (with-connection config
      (list-tables))
    ; ({:name "MyFirstTable"})

## License

Copyright &copy; 2011 Paul Ingles

Distributed under the Eclipse Public License, the same as Clojure.
