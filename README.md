BigData
=======

Instrucciones para correr las métricas:

 # MapReduce Java
	
	## Métrica 1 - Promedio de demora de despegue por mes por estado
      `hadoop jar bigdata-tp1-jar-with-dependencies.jar -inPath 'input\_path' -outPath 'output\_path' -avgTakeOffDelay`

    ## Métrica 2 - Vuelos Cancelados por aerolínea
       `hadoop jar bigdata-tp1-jar-with-dependencies.jar -inPath 'input\_path' -outPath 'output\_path' -cancelledFlights -carriersPath 'carriers\_path'`
    
	## Métrica 3 - Millas voladas por aerolínea por año
       `hadoop jar bigdata-tp1-jar-with-dependencies.jar -inPath 'input\_path' -outPath 'output\_path' -milesFlown -carriersPath 'carriers\_path'`
        
	## Métrica 4 - Horas de vuelo por fabricante
        `hadoop jar bigdata-tp1-jar-with-dependencies.jar -inPath 'input\_path' -outPath 'output\_path' -flightHours -manufacturer 'target\_manufacturer\_name'`
    
	## Métrica 13(OPCIONAL) - Cantidad de vuelos por aerolínea
        `hadoop jar bigdata-tp1-jar-with-dependencies.jar -inPath 'input\_path' -outPath 'output\_path' -flightCount -carriersPath 'carriers\_path'`
        
	## Métrica 14(OPCIONAL) - Proporción de vuelos cancelados por aerolínea
        `hadoop jar bigdata-tp1-jar-with-dependencies.jar -inPath 'input\_path' -outPath 'output\_path' -propCancelledFLights -carriersPath 'carriers\_path'`

 # Hive.
    ## Métrica 5 - Top 5 Aeropuertos con demora de despegue por año
        `hive -S -f metric5-depDelayTop5.sql -hiveconf flightsPath='input\_flights\_path' -hiveconf airportsPath='input\_airports\_path' -hiveconf output='output\_path'`
	
	## Métrica 6 - Imprevistos 2005
		`hive -S -f metric6-2005FlightStats.sql -hiveconf flightsPath='input\_flights\_path' [-hiveconf airport='airport\_IATA'] -hiveconf output='output\_path'`
	
	## Métrica 7 - Top 5 Aeropuertos con mayor promedio de demoras}
		`hive -S -f metric7.sql -hiveconf flightsPath='input\_flights\_path' -hiveconf airportsPath='input\_airports\_path' -hiveconf output='output\_path'`

	## Métrica 8 - Huracanes con más cancelaciones
		`hive -S -f metric8.sql -hiveconf flightsPath='input\_flights\_path' -hiveconf output='output\_path'`

 # Pig
    ## Métrica 9 - Rutas más voladas por año
        `pig -param flights=FLIGHTS\_PATH -param airports=AIRPORTS\_PATH -param output=OUTPUT\_PATH ej9.pig`

    ## Métrica 10 - Cantidad de vuelos cancelados y no cancelados en septiembre de 2011
        `pig -param flights=FLIGHTS\_PATH -param output=OUTPUT\_PATH ej10.pig`
    
	## Métrica 11 - Hora partida del último vuelo del 9/11 para cada aeropuerto
        `pig -param flights=FLIGHTS\_PATH -param output=OUTPUT\_PATH ej11.pig`
        
	## Métrica 12 - Promedio diario de demora de despegue en el año 2001
        `pig -param flights=FLIGHTS\_PATH -param output=OUTPUT\_PATH ej12.pig`