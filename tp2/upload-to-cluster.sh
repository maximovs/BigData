#!/bin/bash

#Quite mode
TAR_OPTS="-zcf"
#Verbose mode
#TAR_OPTS="-zcvf"

if [ $# = 0 ]; then
	tar $TAR_OPTS tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh
	echo "No arguments. Taring everything and uploading to cluster. This may take a while..."
elif [ $# = 1 ] && [ $1 = 'exclude-jars' ]; then
	tar $TAR_OPTS tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh --exclude='target'
	echo "Exluding jars. Taring everything except jars and uploading to cluster. This shouldn't take long."
elif [ $# = 1 ] && [ $1 != 'exclude-jars' ]; then
	tar $TAR_OPTS tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh --exclude=$1
	echo "Excluding 1 project - $1, but including jars. Taring everything except jars and uploading to cluster. This may take a while..."
elif [ $# = 2 ] && [ $1 != 'exclude-jars' ]; then
	tar $TAR_OPTS tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh --exclude=$1 --exclude=$2
	echo "Excluding 2 projects - $1 and $2, but including jars. Taring everything except jars and uploading to cluster. This may take a while..."
elif [ $# = 2 ] && [ $1 = 'exclude-jars' ]; then
	tar $TAR_OPTS tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh --exclude='target' --exclude=$2
	echo "Exluding jars and 1 project - $2. Taring everything except jars and uploading to cluster. This shouldn't take long."
elif [ $# = 3 ] && [ $1 = 'exclude-jars' ]; then
	tar $TAR_OPTS tp2.tar.gz flume/ storm/ tweetsStream/ README.md make.sql upload-to-cluster.sh --exclude='target' --exclude=$2 --exclude=$3
	echo "Exluding jars and 2 projects - $2 and $3. Taring everything except jars and uploading to cluster. This shouldn't take long."
fi

scp -i id_dsa tp2.tar.gz hadoop@hadoop-2013-datanode-4:./grupoBarri/
