cp target/bigdata-tp2-flume-jar-with-dependencies.jar conf/
/home/acrespo/bigdata/apache-flume-1.4.0-bin/bin/flume-ng agent --conf-file conf/flume-conf.properties --name agent -Dflume.root.logger=INFO,console --classpath $FLUME_CLASSPATH
