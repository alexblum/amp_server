CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50),
  `password` varchar(255),
  `email` varchar(255),
  `createDate` timestamp,
  `lastLogin` timestamp,
  `active` tinyint(1),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;