tar -zcvf tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh --exclude='target'
scp -i id_dsa tp2.tar.gz hadoop@hadoop-2013-datanode-4:./grupoBarri/