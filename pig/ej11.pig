REGISTER ../contrib/piggybank/java/piggybank.jar;
flights = LOAD '$flights' USING org.apache.pig.piggybank.storage.CSVLoader() AS (year:int, month:int ,dayofMonth:int,dayOfWeek:chararray, DepTime:int, CRSDepTime:chararray, ArrTime:chararray ,CRSArrTime:chararray, UniqueCarrier:chararray, FlightNum:chararray, TailNum:chararray, ActualElapsedTime:chararray, CRSElapsedTime:chararray, AirTime:chararray, ArrDelay:chararray, DepDelay:chararray, Origin:chararray, Dest:chararray, Distance:chararray, TaxiIn:chararray, TaxiOut:chararray, Cancelled:int, CancellationCode:chararray, Diverted:chararray, CarrierDelay:chararray, WeatherDelay:chararray, NASDelay:chararray, SecurityDelay:chararray, LateAircraftDelay:chararray);

flights = FILTER flights BY (month == 9) AND (year == 2001) AND (dayofMonth == 11);
flights = FOREACH flights GENERATE dayofMonth, month, year, Origin, DepTime;

grouped = GROUP flights BY (Origin, dayofMonth);

result = FOREACH grouped GENERATE group.Origin, MAX(flights.DepTime) As DepTime;
result = FOREACH result GENERATE Origin, CONCAT((chararray) (DepTime/100),CONCAT(':',(chararray) (DepTime%100)));

STORE result INTO '$output' USING PigStorage (',');
