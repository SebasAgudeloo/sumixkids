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
-- Table structure for table `dispositivos_reconocidos`
--

DROP TABLE IF EXISTS `dispositivos_reconocidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dispositivos_reconocidos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `device_id` varchar(64) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `fecha_ultimo_2fa` datetime NOT NULL,
  `contador_2fa` int(11) DEFAULT 1,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_id` (`usuario_id`,`device_id`),
  CONSTRAINT `dispositivos_reconocidos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivos_reconocidos`
--

LOCK TABLES `dispositivos_reconocidos` WRITE;
/*!40000 ALTER TABLE `dispositivos_reconocidos` DISABLE KEYS */;
INSERT INTO `dispositivos_reconocidos` VALUES (1,25,'3615a70d-aa1b-432f-9ff2-8f4a4b6075f1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36','2025-09-28 20:19:23',1,'2025-09-28 20:19:23'),(2,25,'fe5f69a1-614e-4cb1-9eac-4ab237d12ec2','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36','2025-09-28 20:33:17',1,'2025-09-28 20:33:17'),(3,25,'654321f7-c9f2-4c91-93bd-e63ea47c3e21','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36','2025-09-28 20:42:56',1,'2025-09-28 20:42:56'),(4,1,'654321f7-c9f2-4c91-93bd-e63ea47c3e21','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36','2025-09-28 20:43:23',1,'2025-09-28 20:43:23'),(5,25,'8cfc2ee5-cef8-40b8-98ab-5311b5659018','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36','2025-09-28 20:49:45',1,'2025-09-28 20:49:45');
/*!40000 ALTER TABLE `dispositivos_reconocidos` ENABLE KEYS */;
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
/*!40000 ALTER TABLE `log_acceso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_auditoria`
--

DROP TABLE IF EXISTS `log_auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_auditoria` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  `accion` varchar(100) NOT NULL,
  `tabla_afectada` varchar(50) DEFAULT NULL,
  `valor_anterior` text DEFAULT NULL,
  `valor_nuevo` text DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `fecha_hora` datetime DEFAULT current_timestamp(),
  `ip_usuario` varchar(45) DEFAULT NULL,
  `aprobado_por_admin_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`id_usuario`),
  KEY `aprobado_por_admin_id` (`aprobado_por_admin_id`),
  CONSTRAINT `log_auditoria_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `log_auditoria_ibfk_2` FOREIGN KEY (`aprobado_por_admin_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_auditoria`
--

LOCK TABLES `log_auditoria` WRITE;
/*!40000 ALTER TABLE `log_auditoria` DISABLE KEYS */;
INSERT INTO `log_auditoria` VALUES (1,2,'lumela','ELIMINAR_USUARIO','usuarios','ID: 6 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-16 22:50:47','0:0:0:0:0:0:0:1',2),(3,2,'lumela','ELIMINAR_USUARIO','usuarios','ID: 8 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-17 08:31:17','0:0:0:0:0:0:0:1',2),(4,2,'lumela','ELIMINAR_USUARIO','usuarios','ID: 9 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-17 08:36:34','0:0:0:0:0:0:0:1',2),(5,2,'lumela','ELIMINACION_FALLIDA','usuarios','ID: 4 - Intento fallido',NULL,'Contraseña incorrecta','FALLIDO','2025-09-17 08:55:08','0:0:0:0:0:0:0:1',2),(9,2,'lumela','ELIMINAR_USUARIO','usuarios','ID: 10 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-18 00:35:43','0:0:0:0:0:0:0:1',2),(10,2,'lumela','ELIMINACION_FALLIDA','usuarios','ID: 11 - Intento fallido',NULL,'Contraseña incorrecta','FALLIDO','2025-09-18 10:29:01','0:0:0:0:0:0:0:1',2),(13,1,'sebasagudelo','ELIMINACION_FALLIDA','usuarios','ID: 12 - Intento fallido',NULL,'Contraseña incorrecta','FALLIDO','2025-09-28 14:26:22','0:0:0:0:0:0:0:1',1),(14,1,'sebasagudelo','ELIMINACION_FALLIDA','usuarios','ID: 12 - Intento fallido',NULL,'Contraseña incorrecta','FALLIDO','2025-09-28 14:33:34','0:0:0:0:0:0:0:1',1),(15,1,'sebasagudelo','ELIMINACION_FALLIDA','usuarios','ID: 12 - Intento fallido',NULL,'Contraseña incorrecta','FALLIDO','2025-09-28 14:35:24','0:0:0:0:0:0:0:1',1),(16,1,'sebasagudelo','ELIMINACION_FALLIDA','usuarios','ID: 12 - Intento fallido',NULL,'Contraseña de administrador incorrecta','FALLIDO','2025-09-28 14:45:22','0:0:0:0:0:0:0:1',1),(17,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 12 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-28 14:45:37','0:0:0:0:0:0:0:1',1),(18,1,'sebasagudelo','ERROR_ELIMINACION','usuarios','ID: 13 - Error en eliminación',NULL,'Error: Cannot delete or update a parent row: a foreign key constraint fails (`sumixkids`.`dispositivos_reconocidos`, CONSTRAINT `dispositivos_reconocidos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`))','ERROR','2025-09-28 14:48:59','0:0:0:0:0:0:0:1',1),(19,1,'sebasagudelo','ERROR_ELIMINACION','usuarios','ID: 13 - Error en eliminación',NULL,'Error: Cannot delete or update a parent row: a foreign key constraint fails (`sumixkids`.`dispositivos_reconocidos`, CONSTRAINT `dispositivos_reconocidos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`))','ERROR','2025-09-28 14:49:38','0:0:0:0:0:0:0:1',1),(22,1,'sebasagudelo','ELIMINACION_FALLIDA','usuarios','ID: 15 - Intento fallido',NULL,'Contraseña de administrador incorrecta','FALLIDO','2025-09-28 15:20:50','0:0:0:0:0:0:0:1',1),(23,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 15 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-28 15:20:58','0:0:0:0:0:0:0:1',1),(24,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 16 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-28 15:40:49','0:0:0:0:0:0:0:1',1),(27,1,'sebasagudelo','LIMPIAR_REGISTROS','multiple','ID: 17 - Limpieza de registros tipo: todos',NULL,'Registros limpiados exitosamente para usuario: karen12','EXITOSO','2025-09-28 17:25:40','0:0:0:0:0:0:0:1',1),(28,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 17 - Usuario eliminado',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-28 17:25:56','0:0:0:0:0:0:0:1',1),(29,1,'sebasagudelo','ERROR_ELIMINACION','usuarios','ID: 21 - Error en eliminación',NULL,'Error: El usuario tiene registros dependientes que deben eliminarse primero:\\n- 1 código(s) de recuperación de contraseña\\nUse los botones de limpieza para eliminar estos registros antes de eliminar el usuario.','ERROR','2025-09-28 17:28:29','0:0:0:0:0:0:0:1',1),(41,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 24 - Usuario: juancuellar',NULL,'Preparando eliminación de usuario','INICIADO','2025-09-28 17:56:44','0:0:0:0:0:0:0:1',1),(43,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 13 - Usuario: sebas',NULL,'Preparando eliminación de usuario','INICIADO','2025-09-28 18:44:55','127.0.0.1',1),(44,1,'sebasagudelo','ERROR_ELIMINACION','usuarios','ID: 1 - Error eliminando usuario ID: 13',NULL,'Error: El usuario tiene registros dependientes que deben eliminarse primero:\\n- 1 registro(s) de auditoría\\nUse los botones de limpieza para eliminar estos registros antes de eliminar el usuario.','ERROR','2025-09-28 18:44:55','127.0.0.1',1),(45,1,'sebasagudelo','LIMPIAR_REGISTROS','multiple','ID: 13 - Limpieza de registros tipo: todos',NULL,'Registros limpiados exitosamente para usuario: sebas','EXITOSO','2025-09-28 18:45:01','127.0.0.1',1),(47,1,'sebasagudelo','ERROR_ELIMINACION','usuarios','ID: 1 - Error eliminando usuario ID: 13',NULL,'Error: Cannot delete or update a parent row: a foreign key constraint fails (`sumixkids`.`dispositivos_reconocidos`, CONSTRAINT `dispositivos_reconocidos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`))','ERROR','2025-09-28 18:45:11','127.0.0.1',1),(48,1,'sebasagudelo','ELIMINAR_USUARIO','usuarios','ID: 13 - Usuario: sebas',NULL,'Preparando eliminación de usuario','INICIADO','2025-09-28 18:56:12','0:0:0:0:0:0:0:1',1),(49,1,'sebasagudelo','ELIMINAR_USUARIO_EXITOSO','usuarios','ID: 1 - Usuario eliminado: sebas',NULL,'Usuario eliminado correctamente','EXITOSO','2025-09-28 18:56:15','0:0:0:0:0:0:0:1',1),(51,25,'sebasoquendo','ERROR_ELIMINACION','usuarios','ID: 25 - Error eliminando usuario ID: 25',NULL,'Error: Cannot delete or update a parent row: a foreign key constraint fails (`sumixkids`.`log_auditoria`, CONSTRAINT `log_auditoria_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`))','ERROR','2025-09-28 18:58:49','0:0:0:0:0:0:0:1',25),(52,25,'sebasoquendo','ELIMINAR_USUARIO','usuarios','ID: 25 - Usuario: sebasoquendo',NULL,'Preparando eliminación de usuario','INICIADO','2025-09-28 19:22:07','0:0:0:0:0:0:0:1',25),(53,25,'sebasoquendo','ERROR_ELIMINACION','usuarios','ID: 25 - Error eliminando usuario ID: 25',NULL,'Error: El usuario tiene registros dependientes que deben eliminarse primero:\\n- 2 registro(s) de auditoría\\nUse los botones de limpieza para eliminar estos registros antes de eliminar el usuario.','ERROR','2025-09-28 19:22:07','0:0:0:0:0:0:0:1',25);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_resets_codes`
--

LOCK TABLES `password_resets_codes` WRITE;
/*!40000 ALTER TABLE `password_resets_codes` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1246 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `grado` varchar(10) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'sebasagudelo','$2a$10$utiqSrtrshAIu1ACHO4rJOQPUQhgaDEtvLdB8lxJzg2bCNqJFEy8e','agudelosebastian726@gmail.com','Sebastian Steven','Agudelo Oquendo','',1,'2025-09-11 19:37:21','2025-09-29 22:28:31',0,0,NULL),(2,'lumela','$2a$10$pXgY28V/M0JsnynCQ9Zlhum/Enat5GHDsPp/8XhKrEC5zHKCfBTzq','lanbrrr153@gmail.com','Luis Eduardo','Mercado Laza','',1,'2025-09-12 20:44:10','2025-09-18 10:28:28',0,0,NULL),(25,'sebasoquendo','$2a$10$8xz4Qzscw39CvK91zq5cJuwYz.OLVaBNh/NDBUttwmnwVcu4l5SJO','sebasagu312@gmail.com','Sebastian Steven','Oquendo','5',3,'2025-09-28 18:56:51','2025-09-28 21:12:02',0,0,NULL);
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

-- Dump completed on 2025-09-29 22:39:57
