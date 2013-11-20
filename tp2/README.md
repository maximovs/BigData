Esta guía asume que se tiene en el archivo /etc/hosts la información correspondiente a las ips del cluster de Amazon
la cátedra de Big Data de ITBA.

# Proyecto Flume 
Se requiere que Flume se encuentre instalado y agregado a la variable de entorno PATH. 
Esto último se puede hacer de la siguiente manera:

	export PATH=absolute-path/to/flumeDir/bin:$PATH

Para verificar que flume está agregado al PATH se debería poder correr desde la consola el comando ``flume-ng`` y 
verificar que el output es el del comando en cuestión.

Para compilar el proyecto flume con el custom sink para activeMQ correr dentro de la carpeta flume

	mvn clean package -DskipTests

Para correr el agente de flume correr dentro de la carpeta flume

	sh run-flume.sh

La configuración del agente de flume se encuentra en la carpeta flume/conf.

# Proyecto Storm
Se requiere que Storm se encuentre instalado y agregado a la variable de entorno PATH. 

Para compilar el proyecto storm con la topología correr dentro de la carpeta storm

	mvn clean package

Para correr la topología de storm correr dentro de la carpeta storm
	
	storm jar target/storm-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.itba.g2.storm.StormTopology topologyName activeMQaddress sqlDBUrl sqlTable mainField countField sqlUser sqlPass HBaseTable columnFamily

donde ``topologyName`` es el nombre que se le quiere dar a la topología de storm que se correrá, ``activeMQUrl`` es la url de la cola de mensajes
ActiveMQ, ``sqlDBUrl``
es la url de la base de datos MYSQL donde se escribirán los resultados, ``sqlTable`` es el nombre de la tabla mysql 
donde se almacenarán los resultados, ``mainField`` y ``countField`` son los campos de la tabla que representan
el nombre de la agrupación y la cantidad de menciones, ``sqlUser `` y ``sqlPass `` son el usuario y la contraseña para acceder
a la base de datos MYSQL, ``HBaseTable `` es el nombre de la table de HBase y ``columnFamily`` es el nombre de la columna de la tabla de hbase que contiene las palabras
a buscar.

Una vez ejecutada una topología, si se desea volverla a correrla (con el mismo nombre) se debe dar de baja previamente la topología anterior. Esto se logra corriendo

	storm kill topologyName

En caso de que Nimbus se encuentre caído, lo cual puede percibirse desde la storm UI o por algunos errores extraños
de conexión, se debe levantarlo corriendo
	
	storm nimbus

Adicionalmente, quizás la storm UI puede estar desactivada, para activarla basta correr

	storm ui

Si se encuentra bajo un proxy, como es el caso de los laboratorios de ITBA, que bloquea ciertos puertos, puede tener
problemas para monitorear algunos los sistemas. Esto puede solucionarse abriendo un tunel ssh al namenode.


	ssh -i id_dsa -L 9000:hadoop-2013-datanode-4:8080 hadoop@hadoop-2013-namenode

Por último, en caso de que no haya supervisors corriendo se deben levantar de la siguiente forma corriendo 
en el cluster

	nohup storm supervisor&


# ActiveMQ 

Tanto el proyecto Flume como el de Storm utilizan una cola de mensaje ActiveMQ, que debe estar correctamente instalado, 
y cuya url es uno de los parámetros para correr el proyecto de Storm.

Para monitorear el funcionamiento de las colas en ActiveMQ se puede acceder a 
[http://hadoop-2013-datanode-7:8161/admin/queues.jsp](http://hadoop-2013-datanode-7:8161/admin/queues.jsp), en donde, 
al tiempo de este proyecto, se encuentra levantaba la cola de mensajes de ActiveMQ.

# Proyecto TweetsStream

Para compilar el proyecto del consumidor de streams de Twitter correr dentro de la carpeta tweetsStream

    mvn clean package
	
para que el proyecto se compile de forma correcta, en la carpeta /tweetsStream/src/main/resources debe existir el archivo oauthcredentials con el siguiente formato:

	consumerKey = xxxxxxxx
	consumerSecret = xxxxxxx
	accessToken = xxxxxxx
	accessTokenSecret = xxxxxxxx

siendo estos los datos provistos por Twitter para la autenticación mediante OAuth.

Para correr el consumidor de streams de Twitter correr dentro de la carpeta tweetsStream/target

    java -jar bigdata-twitterclient-jar-with-dependencies.jar <outputFolder> <terms>

donde <outputFolder> es el path relativo de la carpeta en donde se crearan los archivos de salida. Esta carpeta debe existir previamente. 
Paralelamente <terms> es la lista de términos a buscar separados por comas (","). Un ejemplo de términos podría ser "termino1,termino2,termino de prueba".


# MySQL
Los resultados son procesados en storm y se almancenan en un table de MYSQL.

El nombre de la tabla se toma como parámetro del proyecto de Storm, al igual que los nombres de las columnas.
Para ver los resultados, se puede acceder a la base de datos que, al tiempo de este proyecto, 
se encuentra en ``hadoop-2013-datanode-6`` y correr

	select * from table_name;
