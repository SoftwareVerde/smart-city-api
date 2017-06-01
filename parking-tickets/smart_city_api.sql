-- MySQL dump 10.13  Distrib 5.5.55, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: smart_city_api
-- ------------------------------------------------------
-- Server version	5.5.55-0+deb7u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `license_plates`
--

DROP TABLE IF EXISTS `license_plates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `license_plates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `number` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_plates_uq1` (`number`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `license_plates`
--

LOCK TABLES `license_plates` WRITE;
/*!40000 ALTER TABLE `license_plates` DISABLE KEYS */;
/*!40000 ALTER TABLE `license_plates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_meters`
--

DROP TABLE IF EXISTS `parking_meters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parking_meters` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `meter_id` varchar(255) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `max_dwell_duration` int(10) unsigned DEFAULT NULL,
  `is_handicap` tinyint(1) NOT NULL DEFAULT '0',
  `rate` decimal(14,4) DEFAULT NULL,
  `is_charging_station` tinyint(1) NOT NULL DEFAULT '0',
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parking_meters_uq1` (`meter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_meters`
--

LOCK TABLES `parking_meters` WRITE;
/*!40000 ALTER TABLE `parking_meters` DISABLE KEYS */;
/*!40000 ALTER TABLE `parking_meters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_tickets`
--

DROP TABLE IF EXISTS `parking_tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parking_tickets` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `ticket_number` varchar(255) NOT NULL,
  `license_plate_id` int(10) unsigned NOT NULL,
  `parking_meter_id` int(10) unsigned NOT NULL,
  `violation_code` varchar(255) DEFAULT NULL,
  `fine_amount` decimal(14,4) DEFAULT NULL,
  `paid_amount` decimal(14,4) DEFAULT NULL,
  `due_amount` decimal(14,4) DEFAULT NULL,
  `disposition` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parking_tickets_uq1` (`ticket_number`),
  KEY `parking_tickets_fq1` (`license_plate_id`),
  KEY `parking_tickets_fq2` (`parking_meter_id`),
  CONSTRAINT `parking_tickets_fq1` FOREIGN KEY (`license_plate_id`) REFERENCES `license_plates` (`id`),
  CONSTRAINT `parking_tickets_fq2` FOREIGN KEY (`parking_meter_id`) REFERENCES `parking_meters` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_tickets`
--

LOCK TABLES `parking_tickets` WRITE;
/*!40000 ALTER TABLE `parking_tickets` DISABLE KEYS */;
/*!40000 ALTER TABLE `parking_tickets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-01 15:48:36
