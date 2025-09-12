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
-- Table structure for table `asignaciones_docente`
--

DROP TABLE IF EXISTS `asignaciones_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asignaciones_docente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docente_id` int(11) NOT NULL,
  `estudiante_id` int(11) NOT NULL,
  `nivel_especifico_id` int(11) DEFAULT NULL,
  `ejercicios_personalizados_json` text DEFAULT NULL,
  `fecha_asignacion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `docente_id` (`docente_id`),
  KEY `estudiante_id` (`estudiante_id`),
  KEY `nivel_especifico_id` (`nivel_especifico_id`),
  CONSTRAINT `asignaciones_docente_ibfk_1` FOREIGN KEY (`docente_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `asignaciones_docente_ibfk_2` FOREIGN KEY (`estudiante_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `asignaciones_docente_ibfk_3` FOREIGN KEY (`nivel_especifico_id`) REFERENCES `niveles_dificultad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignaciones_docente`
--

LOCK TABLES `asignaciones_docente` WRITE;
/*!40000 ALTER TABLE `asignaciones_docente` DISABLE KEYS */;
/*!40000 ALTER TABLE `asignaciones_docente` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Table structure for table `configuracion_padre`
--

DROP TABLE IF EXISTS `configuracion_padre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracion_padre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `padre_id` int(11) NOT NULL,
  `estudiante_id` int(11) NOT NULL,
  `tiempo_limite_diario_min` int(11) DEFAULT NULL,
  `inicio_pausa` time DEFAULT NULL,
  `fin_pausa` time DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `padre_id` (`padre_id`),
  KEY `estudiante_id` (`estudiante_id`),
  CONSTRAINT `configuracion_padre_ibfk_1` FOREIGN KEY (`padre_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `configuracion_padre_ibfk_2` FOREIGN KEY (`estudiante_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_padre`
--

LOCK TABLES `configuracion_padre` WRITE;
/*!40000 ALTER TABLE `configuracion_padre` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracion_padre` ENABLE KEYS */;
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
-- Table structure for table `ejercicios`
--

DROP TABLE IF EXISTS `ejercicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ejercicios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_operacion` varchar(20) NOT NULL,
  `nivel_dificultad` int(11) NOT NULL,
  `num1` int(11) NOT NULL,
  `num2` int(11) NOT NULL,
  `respuesta_correcta` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ejercicios`
--

LOCK TABLES `ejercicios` WRITE;
/*!40000 ALTER TABLE `ejercicios` DISABLE KEYS */;
/*!40000 ALTER TABLE `ejercicios` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
-- Table structure for table `niveles_dificultad`
--

DROP TABLE IF EXISTS `niveles_dificultad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `niveles_dificultad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `rango_num1_min` int(11) DEFAULT NULL,
  `rango_num1_max` int(11) DEFAULT NULL,
  `rango_num2_min` int(11) DEFAULT NULL,
  `rango_num2_max` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `niveles_dificultad`
--

LOCK TABLES `niveles_dificultad` WRITE;
/*!40000 ALTER TABLE `niveles_dificultad` DISABLE KEYS */;
/*!40000 ALTER TABLE `niveles_dificultad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progreso_estudiante`
--

DROP TABLE IF EXISTS `progreso_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `progreso_estudiante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `estudiante_id` int(11) NOT NULL,
  `nivel_actual` int(11) DEFAULT NULL,
  `ejercicios_resueltos_correctos` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `estudiante_id` (`estudiante_id`),
  KEY `nivel_actual` (`nivel_actual`),
  CONSTRAINT `progreso_estudiante_ibfk_1` FOREIGN KEY (`estudiante_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `progreso_estudiante_ibfk_2` FOREIGN KEY (`nivel_actual`) REFERENCES `niveles_dificultad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progreso_estudiante`
--

LOCK TABLES `progreso_estudiante` WRITE;
/*!40000 ALTER TABLE `progreso_estudiante` DISABLE KEYS */;
/*!40000 ALTER TABLE `progreso_estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recompensas`
--

DROP TABLE IF EXISTS `recompensas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recompensas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `imagen_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recompensas`
--

LOCK TABLES `recompensas` WRITE;
/*!40000 ALTER TABLE `recompensas` DISABLE KEYS */;
/*!40000 ALTER TABLE `recompensas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recompensas_obtenidas`
--

DROP TABLE IF EXISTS `recompensas_obtenidas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recompensas_obtenidas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `estudiante_id` int(11) NOT NULL,
  `recompensa_id` int(11) NOT NULL,
  `fecha_obtencion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `estudiante_id` (`estudiante_id`),
  KEY `recompensa_id` (`recompensa_id`),
  CONSTRAINT `recompensas_obtenidas_ibfk_1` FOREIGN KEY (`estudiante_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `recompensas_obtenidas_ibfk_2` FOREIGN KEY (`recompensa_id`) REFERENCES `recompensas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recompensas_obtenidas`
--

LOCK TABLES `recompensas_obtenidas` WRITE;
/*!40000 ALTER TABLE `recompensas_obtenidas` DISABLE KEYS */;
/*!40000 ALTER TABLE `recompensas_obtenidas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `respuestas_estudiante`
--

DROP TABLE IF EXISTS `respuestas_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `respuestas_estudiante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `estudiante_id` int(11) NOT NULL,
  `ejercicio_id` int(11) NOT NULL,
  `respuesta_dada` int(11) NOT NULL,
  `es_correcta` tinyint(1) DEFAULT NULL,
  `fecha_respuesta` datetime DEFAULT current_timestamp(),
  `tiempo_empleado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `estudiante_id` (`estudiante_id`),
  KEY `ejercicio_id` (`ejercicio_id`),
  CONSTRAINT `respuestas_estudiante_ibfk_1` FOREIGN KEY (`estudiante_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `respuestas_estudiante_ibfk_2` FOREIGN KEY (`ejercicio_id`) REFERENCES `ejercicios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `respuestas_estudiante`
--

LOCK TABLES `respuestas_estudiante` WRITE;
/*!40000 ALTER TABLE `respuestas_estudiante` DISABLE KEYS */;
/*!40000 ALTER TABLE `respuestas_estudiante` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
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

-- Dump completed on 2025-09-11 11:43:04
