CREATE TABLE `GroupsCount` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`GroupId` varchar(255) NOT NULL DEFAULT '',
`Ammount` int(11) DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE KEY `GroupId` (`GroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=latin1;