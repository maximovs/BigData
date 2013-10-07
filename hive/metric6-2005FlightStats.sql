drop table if exists flights;
create table flights (
	yearNum int,
	monthNum int,
	dayOfMonth int,
	dayOfWeek int,
	depTime string,
	crsDepTime string,
	arrTime string,
	crsArrTime string,
	carrierCode string,
	flightNum int,
	tailNum string,
	actualElapsedTime int,
	crsElapsedTime int,
	airTime int,
	arrDelay int,
	depDelay int,
	originIATA string,
	destIATA string,
	distance int,
	taxiIn int,
	taxiOut int,
	cancelled int,
	cancellationCode string,
	diverted int,
    carrierDelay int,
	weatherDelay int,
    nasDelay int,
	securityDelay int,
	LateAircraftDelay int
)
row format delimited fields terminated by ','
stored as textfile;

LOAD DATA LOCAL INPATH '${hiveconf:flightsPath}' into table flights;

add jar PadInteger.jar;
create temporary function padInt as 'com.example.hive.udf.PadInteger';

add jar NonNegatives.jar;
create temporary function nonNegatives as 'com.example.hive.udf.NonNegatives';

add jar CancelByWeather.jar;
create temporary function cancelByWeather as 'com.example.hive.udf.CancelByWeather';

add jar CheckVariable.jar;
create temporary function checkVariable as 'com.example.hive.udf.CheckVariable';

INSERT OVERWRITE DIRECTORY '${hiveconf:output}' select tmp_table.fc, count(tmp_table.totalDelay > 0), sum(tmp_table.totalDelay), sum(tmp_table.cancelled), sum(diverted), sum(weatherCancels) from (
	select concat(yearNum,"-", padInt(monthNum), "-", padInt(dayOfMonth)) fc, nonNegatives(depDelay + arrDelay) totalDelay, cancelled, diverted, cancelByWeather(cancellationCode) weatherCancels, (depDelay + arrDelay > 0) wasDelayed from flights
    WHERE
    (
        checkVariable('${hiveconf:airport}') = false
        OR originIATA = '${hiveconf:airport}'
        OR destIATA ='${hiveconf:airport}'
    )
	order by fc
) tmp_table group by tmp_table.fc;
