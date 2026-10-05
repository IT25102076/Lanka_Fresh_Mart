CREATE DATABASE  IF NOT EXISTS `lanka_fresh_mart` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `lanka_fresh_mart`;
-- MySQL dump 10.13  Distrib 8.0.45, for macos15 (arm64)
--
-- Host: localhost    Database: lanka_fresh_mart
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

-- SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'ace03952-29ca-11f1-a194-132a9981ba21:1-845';

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `price_at_time` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `cart_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (450.00,1,1,1,1);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`),
  CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES ('2026-10-05 13:46:49.564188',1,'2026-10-05 13:46:49.564213',1);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliveries`
--

DROP TABLE IF EXISTS `deliveries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deliveries` (
  `created_at` datetime(6) DEFAULT NULL,
  `delivery_route_id` bigint DEFAULT NULL,
  `driver_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `scheduled_date` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `delivery_address` text NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk36n9p5v7dd96hpgkwybvbogt` (`order_id`),
  KEY `FK9of70qwnr29wofes9i04sxoi8` (`delivery_route_id`),
  KEY `FK8ateuqis5a8je9rimyvt1nmps` (`driver_id`),
  CONSTRAINT `FK7isx0rnbgqr1dcofd5putl6jw` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FK8ateuqis5a8je9rimyvt1nmps` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`id`),
  CONSTRAINT `FK9of70qwnr29wofes9i04sxoi8` FOREIGN KEY (`delivery_route_id`) REFERENCES `delivery_routes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_routes`
--

DROP TABLE IF EXISTS `delivery_routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_routes` (
  `scheduled_date` date NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `driver_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `route_name` varchar(255) NOT NULL,
  `status` enum('COMPLETED','IN_PROGRESS','PENDING') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn1odygdg1bw489d0o2um2wlhq` (`driver_id`),
  CONSTRAINT `FKn1odygdg1bw489d0o2um2wlhq` FOREIGN KEY (`driver_id`) REFERENCES `drivers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_routes`
--

LOCK TABLES `delivery_routes` WRITE;
/*!40000 ALTER TABLE `delivery_routes` DISABLE KEYS */;
/*!40000 ALTER TABLE `delivery_routes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drivers`
--

DROP TABLE IF EXISTS `drivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drivers` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `vehicle_number` varchar(255) DEFAULT NULL,
  `vehicle_type` varchar(255) DEFAULT NULL,
  `status` enum('AVAILABLE','OFF_DUTY','ON_DELIVERY') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk8g8tftyclmpgp3a5l0ni1nhk` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drivers`
--

LOCK TABLES `drivers` WRITE;
/*!40000 ALTER TABLE `drivers` DISABLE KEYS */;
/*!40000 ALTER TABLE `drivers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `amount` decimal(10,2) NOT NULL,
  `date_added` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_alerts`
--

DROP TABLE IF EXISTS `inventory_alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_alerts` (
  `is_resolved` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `resolved_at` datetime(6) DEFAULT NULL,
  `alert_message` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtek1thexo0kmtar9fop5clyyl` (`product_id`),
  CONSTRAINT `FKtek1thexo0kmtar9fop5clyyl` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_alerts`
--

LOCK TABLES `inventory_alerts` WRITE;
/*!40000 ALTER TABLE `inventory_alerts` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory_alerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `price_at_time` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `total_amount` decimal(12,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `price` decimal(10,2) NOT NULL,
  `quantity_on_hand` int NOT NULL,
  `reorder_level` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` text,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `product_code` varchar(255) NOT NULL,
  `unit` varchar(255) NOT NULL,
  `availability` enum('AVAILABLE','UNAVAILABLE') NOT NULL,
  `category` enum('BAKERY','BEVERAGES','DAIRY','FRUITS','HOUSEHOLD','MEAT','PERSONAL_CARE','SEAFOOD','SNACKS','VEGETABLES') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK922x4t23nx64422orei4meb2y` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (450.00,100,10,'2026-10-05 04:13:10.027540',1,'2026-10-05 15:39:05.136387','Freshly picked organic carrots.','https://img.drz.lazcdn.com/static/lk/p/67bb24344cf0628e2b10a95b79adcb59.jpg_720x720q80.jpg','Fresh Organic Carrots','PRD-001','kg','AVAILABLE','VEGETABLES'),(900.00,50,10,'2026-10-05 04:13:10.029776',2,'2026-10-05 15:40:06.389226','Sweet and crunchy red apples.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic924007.jpg','Red Apples','PRD-002','kg','AVAILABLE','FRUITS'),(500.00,30,10,'2026-10-05 04:13:10.032644',3,'2026-10-05 15:41:22.015764','Full cream fresh milk.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic13698.jpg','Fresh Milk','PRD-003','kg','AVAILABLE','DAIRY'),(1200.00,40,10,'2026-10-05 04:13:10.034492',4,'2026-10-05 15:41:50.716303','Premium skinless chicken breast.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic935008.jpg','Chicken Breast','PRD-004','kg','AVAILABLE','MEAT'),(3500.00,20,10,'2026-10-05 04:13:10.036169',5,'2026-10-05 15:43:27.413776','Premium long grain basmati rice.','https://objectstorage.ap-mumbai-1.oraclecloud.com/n/softlogicbicloud/b/cdn/o/products/600-600/125292--01--1755495351.jpeg','Basmati Rice','PRD-005','kg','AVAILABLE','HOUSEHOLD'),(300.00,25,10,'2026-10-05 04:13:10.037725',6,'2026-10-05 15:44:23.496501','Freshly baked whole wheat brown bread.','https://upload.wikimedia.org/wikipedia/commons/8/8b/Kommissbrot.jpg?utm_source=en.wikipedia.org&utm_campaign=index&utm_content=original','Brown Bread','PRD-006','kg','AVAILABLE','BAKERY'),(450.00,60,10,'2026-10-05 04:13:10.040010',7,'2026-10-05 15:45:02.099530','Farm fresh organic eggs.','https://plate.libpx.com/prod1-img/1e4c6b6962/_MMK7352-kwetters-web_lr.png?width=3100&mode=crop&format=jpeg&signature=04ebe7c8b44725c6751159e1415c539c9692890e','Organic Eggs','PRD-007','kg','AVAILABLE','DAIRY'),(1500.00,15,10,'2026-10-05 04:13:10.041705',8,'2026-10-05 15:46:53.042445','Aged cheddar cheese block.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic120932_20230203092435.jpg','Cheddar Cheese','PRD-008','kg','AVAILABLE','DAIRY'),(2500.00,20,10,'2026-10-05 04:13:10.043467',9,'2026-10-05 15:47:44.189181','Premium fresh salmon fillets.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic941157.jpg','Fresh Salmon','PRD-009','kg','AVAILABLE','SEAFOOD'),(800.00,40,10,'2026-10-05 04:13:10.045743',10,'2026-10-05 15:50:53.677523','Organic green tea bags.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic104232.jpg','Green Tea','PRD-010','kg','AVAILABLE','BEVERAGES'),(400.00,75,10,'2026-10-05 04:13:10.047611',11,'2026-10-05 15:51:22.551534','Crispy salted potato chips.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic121843.jpg','Potato Chips','PRD-011','kg','AVAILABLE','SNACKS'),(950.00,35,10,'2026-10-05 04:13:10.049358',12,'2026-10-05 15:51:46.726220','100% natural orange juice.','https://www.coca-cola.com/content/dam/onexp/us/en/brands/minute-maid/products/orange-juice/orange-juice-with-calcium-and-vitamin-d-packshot.png','Orange Juice','PRD-012','kg','AVAILABLE','BEVERAGES'),(350.00,80,10,'2026-10-05 04:13:10.052419',13,'2026-10-05 15:52:10.916568','Fresh red vine tomatoes.','https://objectstorage.ap-mumbai-1.oraclecloud.com/n/softlogicbicloud/b/cdn/o/products/600-600/310125--01--1559720635.jpeg','Tomatoes','PRD-013','kg','AVAILABLE','VEGETABLES'),(250.00,100,10,'2026-10-05 04:13:10.054226',14,'2026-10-05 15:54:35.479671','Sweet Cavendish bananas.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic923005.jpg','Bananas','PRD-014','kg','AVAILABLE','FRUITS'),(1100.00,45,10,'2026-10-05 04:13:10.056250',15,'2026-10-05 15:55:29.150436','Lean ground beef.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic931006.jpg','Ground Beef','PRD-015','kg','AVAILABLE','MEAT'),(1800.00,25,10,'2026-10-05 04:13:10.058064',16,'2026-10-05 15:55:59.074129','Large fresh shrimp.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic941162_20241220085332.jpg','Shrimp','PRD-016','kg','AVAILABLE','SEAFOOD'),(600.00,30,10,'2026-10-05 04:13:10.059779',17,'2026-10-05 15:50:24.847662','Freshly baked cookies.','https://theeburgerdude.com/wp-content/uploads/2023/12/Cookie-New-01-1024x1024.jpg','Chocolate Chip Cookies','PRD-017','kg','AVAILABLE','BAKERY'),(1200.00,50,10,'2026-10-05 04:13:10.061184',18,'2026-10-05 15:49:33.895689','Nourishing hair shampoo.','https://essstr.blob.core.windows.net/essimg/ItemAsset/Pic128335_20260212093828.jpg','Shampoo','PRD-018','kg','AVAILABLE','PERSONAL_CARE'),(350.00,60,10,'2026-10-05 04:13:10.062646',19,'2026-10-05 15:48:50.360300','Lemon scented dish washing liquid.','https://images.ctfassets.net/rpbo86nkmwf5/jS7NZzyhFMtzPnAz23V0y/0d388c40223242e629e96685b8129c4c/00037000976141_C1N1_8_oz.jpg?fm=jpg&fl=progressive','Dish Soap','PRD-019','kg','AVAILABLE','HOUSEHOLD'),(1500.00,20,10,'2026-10-05 04:13:10.064057',20,'2026-10-05 15:48:17.080068','Roasted mixed nuts.','https://static-01.daraz.lk/p/456493ae67d399063bee23864f26a519.jpg','Mixed Nuts','PRD-020','kg','AVAILABLE','SNACKS');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refunds`
--

DROP TABLE IF EXISTS `refunds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refunds` (
  `amount` decimal(12,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `processed_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgotjk25w6sr9rf3ikc0mrove5` (`order_id`),
  CONSTRAINT `FKsk9rqm7f6y8b1g0qob018hdm7` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refunds`
--

LOCK TABLES `refunds` WRITE;
/*!40000 ALTER TABLE `refunds` DISABLE KEYS */;
/*!40000 ALTER TABLE `refunds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_tickets`
--

DROP TABLE IF EXISTS `support_tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_tickets` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `admin_reply` varchar(1000) DEFAULT NULL,
  `message` varchar(1000) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `status` enum('IN_PROGRESS','OPEN','RESOLVED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4reg1h2465c00bg6dmqlv7ujv` (`user_id`),
  CONSTRAINT `FK4reg1h2465c00bg6dmqlv7ujv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_tickets`
--

LOCK TABLES `support_tickets` WRITE;
/*!40000 ALTER TABLE `support_tickets` DISABLE KEYS */;
/*!40000 ALTER TABLE `support_tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reset_otp_expiry` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `reset_otp` varchar(255) DEFAULT NULL,
  `role` enum('CUSTOMER','CUSTOMER_RELATIONS_OFFICER','DELIVERY_COORDINATOR','FINANCE_EXECUTIVE','OPERATIONS_MANAGER','STORE_SUPERVISOR') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('2026-10-05 04:13:09.992388',1,NULL,'2026-10-05 04:13:09.992402','LFM Headquarters','customer@test.com','Customer','User','$2a$10$T1dKFqRe1vyXjPukhp2vM.ebv7ka0opt88eJ2Fl8p6ff95WAKbIdi','0712345678',NULL,'CUSTOMER'),('2026-10-05 04:13:10.008066',2,NULL,'2026-10-05 04:13:10.008074','LFM Headquarters','supervisor@test.com','Supervisor','User','$2a$10$T1dKFqRe1vyXjPukhp2vM.ebv7ka0opt88eJ2Fl8p6ff95WAKbIdi','0712345678',NULL,'STORE_SUPERVISOR'),('2026-10-05 04:13:10.010208',3,NULL,'2026-10-05 04:13:10.010214','LFM Headquarters','delivery@test.com','Delivery','User','$2a$10$T1dKFqRe1vyXjPukhp2vM.ebv7ka0opt88eJ2Fl8p6ff95WAKbIdi','0712345678',NULL,'DELIVERY_COORDINATOR'),('2026-10-05 04:13:10.011986',4,NULL,'2026-10-05 04:13:10.011991','LFM Headquarters','finance@test.com','Finance','User','$2a$10$T1dKFqRe1vyXjPukhp2vM.ebv7ka0opt88eJ2Fl8p6ff95WAKbIdi','0712345678',NULL,'FINANCE_EXECUTIVE'),('2026-10-05 04:13:10.013757',5,NULL,'2026-10-05 04:13:10.013762','LFM Headquarters','support@test.com','Support','User','$2a$10$T1dKFqRe1vyXjPukhp2vM.ebv7ka0opt88eJ2Fl8p6ff95WAKbIdi','0712345678',NULL,'CUSTOMER_RELATIONS_OFFICER'),('2026-10-05 04:13:10.015297',6,NULL,'2026-10-05 04:13:10.015301','LFM Headquarters','operations@test.com','Operations','User','$2a$10$T1dKFqRe1vyXjPukhp2vM.ebv7ka0opt88eJ2Fl8p6ff95WAKbIdi','0712345678',NULL,'OPERATIONS_MANAGER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 22:44:51
