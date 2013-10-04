REGISTER ../contrib/piggybank/java/piggybank.jar;
flights = LOAD '/user/hadoop/ITBA/TP1/INPUT/SAMPLE/data' USING org.apache.pig.piggybank.storage.CSVLoader() AS (year:int, month:int ,dayofMonth:int,dayOfWeek:chararray, DepTime:int, CRSDepTime:int, ArrTime:chararray ,CRSArrTime:chararray, UniqueCarrier:chararray, FlightNum:chararray, TailNum:chararray, ActualElapsedTime:chararray, CRSElapsedTime:chararray, AirTime:chararray, ArrDelay:chararray, DepDelay:chararray, Origin:chararray, Dest:chararray, Distance:chararray, TaxiIn:chararray, TaxiOut:chararray, Cancelled:int, CancellationCode:chararray, Diverted:chararray, CarrierDelay:chararray, WeatherDelay:chararray, NASDelay:chararray, SecurityDelay:chararray, LateAircraftDelay:chararray);

flights = FILTER flights BY (year == 2001);
flights = FOREACH flights GENERATE CONCAT((chararray)(year),CONCAT('-',CONCAT((chararray)(month),CONCAT('-',(chararray) (dayofMonth))))) AS fullDate, Origin, ((DepTime/100 - CRSDepTime/100)*60 + (DepTime%100 - CRSDepTime%100)) AS DepDelayInMin:int;

flights = FOREACH flights GENERATE fullDate, ((DepDelayInMin>0) ? DepDelayInMin : 0) AS DepDelayInMin;
grouped = GROUP flights BY fullDate;

result = FOREACH grouped GENERATE group, SUM(flights.DepDelayInMin)/60 As DepTimeHourDelay;

DUMP result;
