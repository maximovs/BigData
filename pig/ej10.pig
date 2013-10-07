REGISTER ../contrib/piggybank/java/piggybank.jar;

flights = LOAD '$flights' USING org.apache.pig.piggybank.storage.CSVLoader() AS (year:int, month:int ,dayofMonth:chararray,dayOfWeek:chararray, DepTime:chararray, CRSDepTime:chararray, ArrTime:chararray ,CRSArrTime:chararray, UniqueCarrier:chararray, FlightNum:chararray, TailNum:chararray, ActualElapsedTime:chararray, CRSElapsedTime:chararray, AirTime:chararray, ArrDelay:chararray, DepDelay:chararray, Origin:chararray, Dest:chararray, Distance:chararray, TaxiIn:chararray, TaxiOut:chararray, Cancelled:int, CancellationCode:chararray, Diverted:chararray, CarrierDelay:chararray, WeatherDelay:chararray, NASDelay:chararray, SecurityDelay:chararray, LateAircraftDelay:chararray);

flights = FILTER flights BY (month == 9) AND (year == 2001);
flights = FOREACH flights GENERATE dayofMonth, month, year, Cancelled;
cancelledFlights = FILTER flights BY Cancelled == 1;
regularFlights = FILTER flights BY Cancelled == 0;
cancelledFlightsByDay = GROUP cancelledFlights BY (dayofMonth, month, year);
cancelledFlightsByDay = FOREACH cancelledFlightsByDay GENERATE group.dayofMonth AS day, group.month, group.year, COUNT(cancelledFlights) AS CancelledFlights:long;
regularFlightsByDay = GROUP regularFlights BY (dayofMonth, month, year);

regularFlightsByDay = FOREACH regularFlightsByDay GENERATE group.dayofMonth, group.month, group.year, COUNT(regularFlights)AS RegularFlights:long;

both = JOIN cancelledFlightsByDay BY day FULL OUTER, regularFlightsByDay BY dayofMonth;
result = FOREACH both GENERATE ((dayofMonth is null)? CONCAT(day,'/9/2001'): CONCAT(dayofMonth,'/9/2001')), ((RegularFlights is null)? 0 : RegularFlights), ((CancelledFlights is null)? 0 : CancelledFlights);


STORE result INTO '$output' USING PigStorage (',');
