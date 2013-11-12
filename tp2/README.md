Estas instrucciones asumen que se cuenta con un entorno Unix, con ActiveMQ instalado y corriendo en el puerto default (61616), y Flume
instalado y agregado a la variable de entorno PATH. Esto último se puede hacer de la siguiente manera:
	export PATH=absolute-path/to/flumeDir/bin:$PATH

Para verificar que flume está agregado al PATH se deberia poder correr desde la consola el comando
	flume-ng 

Para compilar el proyecto flume con el custom sink para activeMQ correr dentro de la carpeta flume
	mvn clean package

Para correr el agente de flume correr dentro de la carpeta flume
	sh run-flume.sh

La configuración del agente de flume se encuentra en la carpeta flume/conf.

Para compilar el proyecto storm con la topología correr dentro de la carpeta storm
	mvn clean package

Para correr la topología de storm correr dentro de la carpeta storm
	TODO
