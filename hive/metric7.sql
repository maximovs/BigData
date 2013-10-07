DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS airports;
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

LOAD DATA LOCAL INPATH '${hiveconf:airportsPath}' into table airports;

drop table if exists tmp_table_delayed_dep;
create table tmp_table_delayed_dep(IATA string, name string, quantity int);

insert overwrite table tmp_table_delayed_dep
select a.IATA,
a.name,
count(*)
FROM airports a
INNER JOIN flights dep ON a.IATA = dep.originIATA
WHERE
(
    (dep.year = 2005 AND dep.month = 10 AND dep.dayOfMonth >= 15 AND dep.dayOfMonth <= 26)
    OR
    (dep.year = 2005 AND dep.month = 8 AND dep.dayOfMonth >= 23 AND dep.dayOfMonth <= 30)
    OR
    (dep.year = 1998 AND dep.month = 10 AND dep.dayOfMonth >= 22)
    OR
    (dep.year = 1998 AND dep.month = 11 AND dep.dayOfMonth <= 5)
)
AND dep.depDelay > 0
GROUP BY a.IATA, a.name;

drop table if exists tmp_table_all_dep;
create table tmp_table_all_dep(IATA string, name string, quantity int);

insert overwrite table tmp_table_all_dep
select a.IATA,
a.name,
count(*)
FROM airports a
INNER JOIN flights dep ON a.IATA = dep.originIATA
WHERE
(
    (dep.year = 2005 AND dep.month = 10 AND dep.dayOfMonth >= 15 AND dep.dayOfMonth <= 26)
    OR
    (dep.year = 2005 AND dep.month = 8 AND dep.dayOfMonth >= 23 AND dep.dayOfMonth <= 30)
    OR
    (dep.year = 1998 AND dep.month = 10 AND dep.dayOfMonth >= 22)
    OR
    (dep.year = 1998 AND dep.month = 11 AND dep.dayOfMonth <= 5)
)
GROUP BY a.IATA, a.name;

drop table if exists tmp_table_delayed_arr;
create table tmp_table_delayed_arr(IATA string, name string, quantity int);

insert overwrite table tmp_table_delayed_arr
select a.IATA,
a.name,
count(*)
FROM airports a
INNER JOIN flights arr ON a.IATA = arr.originIATA
WHERE
(
    (arr.year = 2005 AND arr.month = 10 AND arr.dayOfMonth >= 15 AND arr.dayOfMonth <= 26)
    OR
    (arr.year = 2005 AND arr.month = 8 AND arr.dayOfMonth >= 23 AND arr.dayOfMonth <= 30)
    OR
    (arr.year = 1998 AND arr.month = 10 AND arr.dayOfMonth >= 22)
    OR
    (arr.year = 1998 AND arr.month = 11 AND arr.dayOfMonth <= 5)
)
AND arr.arrDelay > 0
GROUP BY a.IATA, a.name;

drop table if exists tmp_table_all_arr;
create table tmp_table_all_arr(IATA string, name string, quantity int);

insert overwrite table tmp_table_all_arr
select a.IATA,
a.name,
count(*)
FROM airports a
INNER JOIN flights arr ON a.IATA = arr.originIATA
WHERE
(
    (arr.year = 2005 AND arr.month = 10 AND arr.dayOfMonth >= 15 AND arr.dayOfMonth <= 26)
    OR
    (arr.year = 2005 AND arr.month = 8 AND arr.dayOfMonth >= 23 AND arr.dayOfMonth <= 30)
    OR
    (arr.year = 1998 AND arr.month = 10 AND arr.dayOfMonth >= 22)
    OR
    (arr.year = 1998 AND arr.month = 11 AND arr.dayOfMonth <= 5)
)
GROUP BY a.IATA, a.name;


add jar Position.jar;
create temporary function position as 'com.example.hive.udf.Position';

drop table if exists tmp_results;
create table tmp_results(IATA string, name string, average double);

insert overwrite table tmp_results
SELECT delarr.IATA, delarr.name, (100 * (delarr.quantity + deldep.quantity) / (allarr.quantity + alldep.quantity)) average FROM
tmp_table_delayed_arr delarr
INNER JOIN tmp_table_all_arr allarr ON delarr.IATA = allarr.IATA
INNER JOIN tmp_table_delayed_dep deldep ON deldep.IATA = delarr.IATA
INNER JOIN tmp_table_all_dep alldep ON alldep.IATA = delarr.IATA
ORDER BY average DESC;

select * from (select name, position(average) position, average from tmp_results) a where position <= 5;
