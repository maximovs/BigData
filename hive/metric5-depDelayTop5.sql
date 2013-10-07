DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS airports;
DROP TABLE IF EXISTS tmp_table;
create table flights (
	year int,
	month int,
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

LOAD DATA INPATH '${hiveconf:flightsPath}' into table flights;

create table airports (
	IATA string,
	name string,
	city string,
	state string,
	country string,
	latitude double,
	longintude double
)
row format delimited fields terminated by ','
stored as textfile;

LOAD DATA INPATH '${hiveconf:airportsPath}' into table airports;

add jar Rank.jar;
create temporary function rank as 'com.example.hive.udf.Rank';

create table tmp_table (year int, name string, suma double);
insert overwrite table tmp_table select year, regexp_replace(name, '\"', ''), sum(depDelay) suma from flights join airports where originIATA = regexp_replace(IATA, '\"', '') group by year, regexp_replace(name, '\"', '') sort by year, suma desc;

INSERT OVERWRITE DIRECTORY '${hiveconf:output}' select * from (select year, rank(year) ranking, name, suma from tmp_table) a where ranking <= 5;
