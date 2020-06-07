# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.29)
# Database: afi_billablehours
# Generation Time: 2020-06-07 18:50:35 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table clients
# ------------------------------------------------------------

DROP TABLE IF EXISTS `clients`;

CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `UK_6dotkFpbC5jb20iLCJpYXQiOb7` (`email`),
  UNIQUE KEY `UK_d4cCI6MTU2MzIyOTA1Nn0pe5s3` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;

INSERT INTO `clients` (`id`, `created_by`, `creation_date`, `last_modified_by`, `last_modified_date`, `email`, `name`, `address`, `phone`)
VALUES
	(10,NULL,NULL,NULL,NULL,'info@mtn.com.gh','MTN Ghana','Independence Avenue, West Ridge, Accra','0301234523'),
	(11,NULL,NULL,NULL,NULL,'jberger@gmail.com','Julius Berger','Box 1245','0244390987'),
	(22,NULL,NULL,NULL,NULL,'info@fidelity.com','Fidelity','Box NM 8790, Nima Street','0302344321');

/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table flyway_schema_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `flyway_schema_history`;

CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;

INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`)
VALUES
	(1,'1','init','SQL','V1__init.sql',172685148,'root','2020-06-06 03:16:26',55,1),
	(2,'2','Create users and user types tables','SQL','V2__Create_users_and_user_types_tables.sql',1257276645,'root','2020-06-06 03:16:27',107,1),
	(3,'3','Create grades clients and timesheets tables','SQL','V3__Create_grades_clients_and_timesheets_tables.sql',-1513805879,'root','2020-06-06 03:16:27',282,1),
	(4,'4','Alter timesheets table and Create invoices table','SQL','V4__Alter_timesheets_table_and_Create_invoices_table.sql',365794873,'root','2020-06-06 11:38:06',551,1),
	(5,'5','Alter invoices timesheets table','SQL','V5__Alter_invoices_timesheets_table.sql',300102947,'root','2020-06-06 12:23:05',64,1);

/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table grades
# ------------------------------------------------------------

DROP TABLE IF EXISTS `grades`;

CREATE TABLE `grades` (
  `id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `alias` varchar(255) NOT NULL,
  `rate` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `UK_6098hjkduy723ndk12390pe5s3` (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;

INSERT INTO `grades` (`id`, `created_by`, `creation_date`, `last_modified_by`, `last_modified_date`, `name`, `alias`, `rate`)
VALUES
	(5,NULL,NULL,NULL,NULL,'Grade A','GRADE_A',500),
	(6,NULL,NULL,NULL,NULL,'Grade B','GRADE_B',300),
	(7,NULL,NULL,NULL,NULL,'Grade C','GRADE_C',200),
	(21,NULL,NULL,NULL,NULL,'Grade D','GRADE_D',175.5);

/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table hibernate_sequence
# ------------------------------------------------------------

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(25);

/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table invoices
# ------------------------------------------------------------

DROP TABLE IF EXISTS `invoices`;

CREATE TABLE `invoices` (
  `id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `total_cost` double NOT NULL,
  `client_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK325a23popo450iuywv1993` (`client_id`),
  CONSTRAINT `FK325a23popo450iuywv1993` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;

INSERT INTO `invoices` (`id`, `created_by`, `creation_date`, `last_modified_by`, `last_modified_date`, `total_cost`, `client_id`)
VALUES
	(17,NULL,NULL,NULL,NULL,4250,11),
	(24,NULL,NULL,NULL,NULL,2791.6666666666665,10);

/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table invoices_timesheets
# ------------------------------------------------------------

DROP TABLE IF EXISTS `invoices_timesheets`;

CREATE TABLE `invoices_timesheets` (
  `invoice_id` bigint(20) NOT NULL,
  `timesheets_id` bigint(20) NOT NULL,
  PRIMARY KEY (`invoice_id`,`timesheets_id`),
  KEY `FK325a23450poi456b7683mdu0g` (`timesheets_id`),
  CONSTRAINT `FK325a23450poi456b7683mdu0g` FOREIGN KEY (`timesheets_id`) REFERENCES `timesheets` (`id`),
  CONSTRAINT `FKb7683mdu0g9JzdWIiOiRob5L3` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `invoices_timesheets` WRITE;
/*!40000 ALTER TABLE `invoices_timesheets` DISABLE KEYS */;

INSERT INTO `invoices_timesheets` (`invoice_id`, `timesheets_id`)
VALUES
	(24,12),
	(17,13),
	(17,14);

/*!40000 ALTER TABLE `invoices_timesheets` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table timesheets
# ------------------------------------------------------------

DROP TABLE IF EXISTS `timesheets`;

CREATE TABLE `timesheets` (
  `id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `invoiced` bit(1) NOT NULL,
  `rate` double NOT NULL,
  `cost` double NOT NULL,
  `hours` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32UzUxMiJ9JzdWIiOiRob255L3` (`user_id`),
  KEY `FK325a23450poi456ewasdfg1993` (`client_id`),
  CONSTRAINT `FK325a23450poi456ewasdfg1993` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`),
  CONSTRAINT `FK32UzUxMiJ9JzdWIiOiRob255L3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `timesheets` WRITE;
/*!40000 ALTER TABLE `timesheets` DISABLE KEYS */;

INSERT INTO `timesheets` (`id`, `created_by`, `creation_date`, `last_modified_by`, `last_modified_date`, `user_id`, `client_id`, `date`, `start_time`, `end_time`, `invoiced`, `rate`, `cost`, `hours`)
VALUES
	(12,NULL,NULL,NULL,NULL,8,10,'2020-06-06','09:00:00','14:35:00',b'1',500,2791.6666666666665,'5:35'),
	(13,NULL,NULL,NULL,NULL,8,11,'2020-06-07','10:00:00','14:30:00',b'1',500,2250,'4:30'),
	(14,NULL,NULL,NULL,NULL,8,11,'2020-06-08','08:00:00','12:00:00',b'1',500,2000,'4:0'),
	(23,NULL,NULL,NULL,NULL,8,22,'2020-06-08','09:00:00','11:00:00',b'0',500,1000,'2:0');

/*!40000 ALTER TABLE `timesheets` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_types
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_types`;

CREATE TABLE `user_types` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user_types` WRITE;
/*!40000 ALTER TABLE `user_types` DISABLE KEYS */;

INSERT INTO `user_types` (`id`, `name`)
VALUES
	(1,'ADMIN'),
	(2,'LAWYER'),
	(3,'FINANCE_USER');

/*!40000 ALTER TABLE `user_types` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(150) DEFAULT NULL,
  `phone` varchar(255) NOT NULL,
  `user_type_id` bigint(20) DEFAULT NULL,
  `has_changed_password` bit(1) NOT NULL,
  `grade_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_du5v5sr43g5bfnji4vb8hg5s3` (`phone`),
  KEY `FK1234567890poiuytrewasdfg` (`grade_id`),
  CONSTRAINT `FK1234567890poiuytrewasdfg` FOREIGN KEY (`grade_id`) REFERENCES `grades` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `created_by`, `creation_date`, `last_modified_by`, `last_modified_date`, `email`, `enabled`, `first_name`, `last_name`, `password`, `phone`, `user_type_id`, `has_changed_password`, `grade_id`)
VALUES
	(4,NULL,NULL,NULL,NULL,'admin@billinghours.com',b'1','Super1','Admin','$2a$10$w3kdcvvmX.r7upHY86bKiedRGIxerTK08L/rdUvA4pGZQAWuJCDFW','0207413037',1,b'1',NULL),
	(8,NULL,NULL,NULL,NULL,'lawyer@billinghours.com',b'1','Lawyer','2','$2a$10$w3kdcvvmX.r7upHY86bKiedRGIxerTK08L/rdUvA4pGZQAWuJCDFW','0542345110',2,b'0',5),
	(9,NULL,NULL,NULL,NULL,'finance@billinghours.com',b'1','Finance','User','$2a$10$HZ/kPAiH9/kId1th4zp1MOfZkk/3iIA2KFnvX0f4TEb8u6xzqNgpG','0263332925',3,b'0',NULL),
	(20,NULL,NULL,NULL,NULL,'james@gmail.com',b'1','James','Bonney','$2a$10$UoaDA4vuJ8L06/j34VQq0.fYJ66MvHbcCt7t4rupM7e22N9tkmfdm','0234567890',3,b'0',NULL);

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
