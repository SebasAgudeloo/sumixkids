-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: sumixkids
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `sumixkids`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sumixkids` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `sumixkids`;

--
-- Table structure for table `cargas_masivas`
--

DROP TABLE IF EXISTS `cargas_masivas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cargas_masivas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `tipo_carga` varchar(50) DEFAULT NULL,
  `nombre_archivo` varchar(255) DEFAULT NULL,
  `fecha_carga` datetime DEFAULT current_timestamp(),
  `estado` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `cargas_masivas_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargas_masivas`
--

LOCK TABLES `cargas_masivas` WRITE;
/*!40000 ALTER TABLE `cargas_masivas` DISABLE KEYS */;
/*!40000 ALTER TABLE `cargas_masivas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_sistema`
--

DROP TABLE IF EXISTS `configuracion_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracion_sistema` (
  `nombre_param` varchar(50) NOT NULL,
  `valor_param` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nombre_param`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_sistema`
--

LOCK TABLES `configuracion_sistema` WRITE;
/*!40000 ALTER TABLE `configuracion_sistema` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracion_sistema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_acceso`
--

DROP TABLE IF EXISTS `log_acceso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_acceso` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `fecha_hora` datetime DEFAULT current_timestamp(),
  `exito` tinyint(1) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `log_acceso_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_acceso`
--

LOCK TABLES `log_acceso` WRITE;
/*!40000 ALTER TABLE `log_acceso` DISABLE KEYS */;
INSERT INTO `log_acceso` VALUES (1,1,'2025-09-14 22:20:45',1,'0:0:0:0:0:0:0:1'),(2,4,'2025-09-14 22:22:34',1,'0:0:0:0:0:0:0:1');
/*!40000 ALTER TABLE `log_acceso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_auditoria`
--

DROP TABLE IF EXISTS `log_auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_auditoria` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `accion` varchar(100) NOT NULL,
  `detalles` text DEFAULT NULL,
  `fecha_hora` datetime DEFAULT current_timestamp(),
  `ip_address` varchar(45) DEFAULT NULL,
  `aprobado_por_admin_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `aprobado_por_admin_id` (`aprobado_por_admin_id`),
  CONSTRAINT `log_auditoria_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `log_auditoria_ibfk_2` FOREIGN KEY (`aprobado_por_admin_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_auditoria`
--

LOCK TABLES `log_auditoria` WRITE;
/*!40000 ALTER TABLE `log_auditoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mantenimiento_programado`
--

DROP TABLE IF EXISTS `mantenimiento_programado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mantenimiento_programado` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_fin` datetime NOT NULL,
  `creado_por` int(11) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `creado_por` (`creado_por`),
  CONSTRAINT `mantenimiento_programado_ibfk_1` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mantenimiento_programado`
--

LOCK TABLES `mantenimiento_programado` WRITE;
/*!40000 ALTER TABLE `mantenimiento_programado` DISABLE KEYS */;
/*!40000 ALTER TABLE `mantenimiento_programado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_resets_codes`
--

DROP TABLE IF EXISTS `password_resets_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_resets_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `code` varchar(10) NOT NULL,
  `expires_at` datetime NOT NULL,
  `used` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `fk_password_resets_user` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_resets_codes`
--

LOCK TABLES `password_resets_codes` WRITE;
/*!40000 ALTER TABLE `password_resets_codes` DISABLE KEYS */;
INSERT INTO `password_resets_codes` VALUES (1,1,'wnVW8w','2025-09-14 20:20:46',0),(3,1,'VFnSlA','2025-09-14 20:23:26',0);
/*!40000 ALTER TABLE `password_resets_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=362 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin'),(2,'docent'),(4,'parents'),(3,'student');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesiones_activas`
--

DROP TABLE IF EXISTS `sesiones_activas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sesiones_activas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `fecha_inicio` datetime DEFAULT current_timestamp(),
  `fecha_ultimo_acceso` datetime DEFAULT NULL,
  `activa` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `sesiones_activas_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesiones_activas`
--

LOCK TABLES `sesiones_activas` WRITE;
/*!40000 ALTER TABLE `sesiones_activas` DISABLE KEYS */;
/*!40000 ALTER TABLE `sesiones_activas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `two_factor_codes`
--

DROP TABLE IF EXISTS `two_factor_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `two_factor_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `code` varchar(10) NOT NULL,
  `expires_at` datetime NOT NULL,
  `used` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `two_factor_codes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `two_factor_codes`
--

LOCK TABLES `two_factor_codes` WRITE;
/*!40000 ALTER TABLE `two_factor_codes` DISABLE KEYS */;
/*!40000 ALTER TABLE `two_factor_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_roles`
--

DROP TABLE IF EXISTS `usuario_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL,
  `asignado_por` int(11) DEFAULT NULL,
  `fecha_asignacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `rol_id` (`rol_id`),
  KEY `asignado_por` (`asignado_por`),
  CONSTRAINT `usuario_roles_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `usuario_roles_ibfk_2` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `usuario_roles_ibfk_3` FOREIGN KEY (`asignado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_roles`
--

LOCK TABLES `usuario_roles` WRITE;
/*!40000 ALTER TABLE `usuario_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `rol_id` int(11) NOT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  `ultima_conexion` datetime DEFAULT NULL,
  `intentos_fallidos` int(11) DEFAULT 0,
  `bloqueado` tinyint(1) DEFAULT 0,
  `autenticacion_2fa` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `rol_id` (`rol_id`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'sebasagudelo','$2a$10$utiqSrtrshAIu1ACHO4rJOQPUQhgaDEtvLdB8lxJzg2bCNqJFEy8e','agudelosebastian726@gmail.com','Sebastian Steven','Agudelo Oquendo',1,'2025-09-11 19:37:21','2025-09-14 22:20:45',0,0,NULL),(2,'lumela','$2a$10$FoOzAmBZrWoypAePSmw/gewRrka5Mhya.Alj0NqDUUz.E3Qdfcu1y','lanbrrr153@gmail.com','Luis Eduardo','Mercado Laza',1,'2025-09-12 20:44:10','2025-09-12 21:20:27',0,0,NULL),(4,'glorialora','$2a$10$NA4qc4raaDgbg1UhoYvhpOwwCPE.geFdNNtaLr22QzrEdoJtzpCHi','sebasagu312@gmail.com','Gloria Amparo','Lora Patiño',2,'2025-09-14 21:25:07','2025-09-14 22:22:34',0,0,NULL),(5,'deisypaola','$2a$10$hcIiiseXyHlOvPy1g1Uu6ehePnnbdlvEnXsv1YD0zuyj70zMkmIl6','sebplay169@gmail.com','Deisy Paola','Restrepo Gomez',4,'2025-09-14 21:26:26',NULL,0,0,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-14 23:13:11
