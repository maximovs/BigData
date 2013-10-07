REGISTER ../contrib/piggybank/java/piggybank.jar;
flights = LOAD '$flights' USING org.apache.pig.piggybank.storage.CSVLoader() AS (year:chararray, month:chararray ,dayofMonth:chararray,dayOfWeek:chararray, DepTime:chararray, CRSDepTime:chararray, ArrTime:chararray ,CRSArrTime:chararray, UniqueCarrier:chararray, FlightNum:chararray, TailNum:chararray, ActualElapsedTime:chararray, CRSElapsedTime:chararray, AirTime:chararray, ArrDelay:chararray, DepDelay:chararray, Origin:chararray, Dest:chararray, Distance:chararray, TaxiIn:chararray, TaxiOut:chararray, Cancelled:chararray, CancellationCode:chararray, Diverted:chararray, CarrierDelay:chararray, WeatherDelay:chararray, NASDelay:chararray, SecurityDelay:chararray, LateAircraftDelay:chararray);

airports = LOAD '$airports'
           USING org.apache.pig.piggybank.storage.CSVLoader()
           AS (ID:chararray, airport:chararray);

flights = FOREACH flights GENERATE year, Origin, Dest, FlightNum;

grouped = GROUP flights BY (year, Origin, Dest);

counted = FOREACH grouped GENERATE group.year,group.Origin,group.Dest , COUNT(flights.FlightNum) AS totalFlights:long;
countedWithOrigin = JOIN counted BY Origin, airports BY ID;
  countedWithOrigin = FOREACH countedWithOrigin GENERATE year, airport AS Origin:chararray, Dest, totalFlights;
  countedWithDest = JOIN countedWithOrigin BY Dest, airports BY ID;
  countedWithDest = FOREACH countedWithDest GENERATE year, Origin, airport AS Dest:chararray, totalFlights;

countedByYear = GROUP countedWithDest BY year;

results = FOREACH countedByYear {
    sorted = ORDER countedWithDest BY totalFlights desc;
  top10 = LIMIT sorted 10;
  GENERATE flatten(top10);
};


STORE results INTO '$output' USING PigStorage (',');
