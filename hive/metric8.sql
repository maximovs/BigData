add jar PadInteger.jar;
create temporary function padInt as 'com.example.hive.udf.PadInteger';

DROP TABLE IF EXISTS flights;
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

LOAD DATA LOCAL INPATH '${hiveconf:flightsPath}' into table flights;

--add jar Rank.jar;
--create temporary function rank as 'com.example.hive.udf.Rank';

DROP TABLE IF EXISTS tmp_table_katrina;
DROP TABLE IF EXISTS tmp_table_mitch;
DROP TABLE IF EXISTS tmp_table_wilma;

create table tmp_table_katrina (year int, month int, dayOfMonth int, cantidad int);
create table tmp_table_mitch (year int, month int, dayOfMonth int, cantidad int);
create table tmp_table_wilma (year int, month int, dayOfMonth int, cantidad int);

insert overwrite table tmp_table_katrina select year, month, dayOfMonth, count(*) from flights
where cancelled = 1
AND
(
    (year = 2005 AND month = 8 AND dayOfMonth >=23 AND dayOfMonth <= 30)
)
group by year, month, dayOfMonth;

insert overwrite table tmp_table_wilma select year, month, dayOfMonth, count(*) from flights
where cancelled = 1
AND cancellationCode = "B"
AND
(
    (year = 2005 AND month = 10 AND dayOfMonth >=15 AND dayOfMonth <= 26)
)
group by year, month, dayOfMonth;

insert overwrite table tmp_table_mitch select year, month, dayOfMonth, count(*) from flights
where cancelled = 1
AND
(
    (year = 1998 AND month = 10 AND dayOfMonth >=22)
    OR (year = 1998 AND month = 11 AND dayOfMonth <= 5)
)
group by year, month, dayOfMonth;

SELECT * FROM (

    SELECT "Katrina", concat(year,"-", padInt(month), "-", padInt(dayOfMonth)), cantidad FROM tmp_table_katrina ORDER BY cantidad DESC LIMIT 1

    UNION ALL

    SELECT "Mitch", concat(year,"-", padInt(month), "-", padInt(dayOfMonth)), cantidad FROM tmp_table_mitch ORDER BY cantidad DESC LIMIT 1

    UNION ALL

    SELECT "Wilma", concat(year,"-", padInt(month), "-", padInt(dayOfMonth)), cantidad FROM tmp_table_wilma ORDER BY cantidad DESC LIMIT 1
) final;
