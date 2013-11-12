export FLUME_CLASSPATH=`pwd`/target/bigdata-tp2-flume-jar-with-dependencies.jar
flume-ng agent --conf-file conf/flume-conf.properties --name agent -Dflume.root.logger=INFO,console --classpath $FLUME_CLASSPATH
