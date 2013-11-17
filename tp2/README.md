

Estas instrucciones asumen que se cuenta con un entorno Unix, con ActiveMQ instalado y corriendo en el puerto default (61616), y Flume
instalado y
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

Para compilar el proyecto storm con la topología correr dentro de la carpeta storm

	mvn clean package

Para correr la topología de storm correr dentro de la carpeta storm
	
	storm jar target/storm-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.itba.g2.storm.StormTopology topologyName sqlDBUrl table mainField countField user pass columnFamily

donde ``topologyName`` es el nombre que se le quiere dar a la topología de storm que se correrá, ``sqlDBUrl``
es la url de la base de datos MYSQL donde se escribirán los resultados, ``table`` es el nombre de la tabla 
donde se almacenarán los resultados, ``mainField`` y ``countField`` son los campos de la tabla que representan
el nombre de la agrupación y la cantidad de menciones, ``user`` y ``pass`` son el usuario y la contraseña para acceder
a la base de datos MYSQL, y ``columnFamily`` es le nombre de la columna de la tabla de hbase que contiene las palabras
a buscar.

Una vez submiteada una topología, si se desea volverla a correr (volver a submitear una topología con el mismo nombre 
que otra que ya se corriío/submiteó), se debe dar de baja previamente. Esto se logra corriendo

	storm kill topologyName

# Proyecto TweetsStream

Para compilar el proyecto del consumidor de streams de Twitter correr dentro de la carpeta tweetsStream

    mvn clean package

Para correr el consumidor de streams de Twitter correr dentro de la carpeta tweetsStream

    ???? <outputFolder> <terms>

    donde <outputFolder> es la carpeta en donde se crearan los archivos de salida. Esta carpeta debe existir previamente. Paralelamente <terms> es la lista de términos a buscar separados por comas (","). Un ejemplo de términos podría ser "termino1,termino2,termino de prueba".
