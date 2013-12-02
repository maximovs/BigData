CREATE TABLE `GroupsCount` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`GroupId` varchar(255) NOT NULL DEFAULT '',
`Ammount` int(11) DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE KEY `GroupId` (`GroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=latin1;


CREATE USER 'barry'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON * . * TO 'barry'@'localhost';

-- Metric 10 table

CREATE TABLE SeptemberFlights
(
  flightDate varchar(255),
  flights int,
  cancellations int,
PRIMARY KEY (flightDate)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table: timelastflightnineeleven

-- DROP TABLE timelastflightnineeleven;

CREATE TABLE timelastflightnineeleven
(
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  origin varchar(255) NOT NULL,
  "time" timestamp,
  PRIMARY KEY (`id`),
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=latin1;

CREATE TABLE totalflights
(
  date varchar(255) NOT NULL,
  total integer,
  cancelled integer,
  PRIMARY KEY (date)
)

