CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50),
  `password` varchar(255),
  `email` varchar(255),
  `createDate` timestamp,
  `lastLogin` timestamp,
  `active` tinyint(1),
  `user_group` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `app_state` (
  `name` VARCHAR(128) NOT NULL,
  `value` VARCHAR(1024) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8