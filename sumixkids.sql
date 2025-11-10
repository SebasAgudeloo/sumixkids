-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.12.0.7122
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for sumixkids
CREATE DATABASE IF NOT EXISTS `sumixkids` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `sumixkids`;

-- Dumping structure for table sumixkids.acompañantes
CREATE TABLE IF NOT EXISTS `acompañantes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `relacion_estudiante` enum('PADRE','MADRE','ABUELO','ABUELA','TIO','TIA','HERMANO','TUTOR_LEGAL','OTRO') NOT NULL,
  `telefono_principal` varchar(15) DEFAULT NULL,
  `telefono_secundario` varchar(15) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `ocupacion` varchar(100) DEFAULT NULL,
  `empresa_trabajo` varchar(150) DEFAULT NULL,
  `nivel_educativo` varchar(50) DEFAULT NULL,
  `recibir_notificaciones` tinyint(1) DEFAULT 1,
  `horario_contacto` varchar(100) DEFAULT NULL,
  `autorizacion_recoger` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `acompañantes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.cargas_masivas
CREATE TABLE IF NOT EXISTS `cargas_masivas` (
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

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.dispositivos_reconocidos
CREATE TABLE IF NOT EXISTS `dispositivos_reconocidos` (
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.docentes
CREATE TABLE IF NOT EXISTS `docentes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `numero_empleado` varchar(20) DEFAULT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `grados_asignados` varchar(50) DEFAULT NULL,
  `anos_experiencia` int(11) DEFAULT NULL,
  `fecha_contratacion` date DEFAULT NULL,
  `titulo_academico` varchar(100) DEFAULT NULL,
  `institucion_titulo` varchar(150) DEFAULT NULL,
  `telefono_trabajo` varchar(15) DEFAULT NULL,
  `horario_disponible` text DEFAULT NULL,
  `estado_laboral` enum('ACTIVO','LICENCIA','VACACIONES','INACTIVO') DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_empleado` (`numero_empleado`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `docentes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.docente_estudiante
CREATE TABLE IF NOT EXISTS `docente_estudiante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_docente` int(11) NOT NULL COMMENT 'ID del usuario con rol docente',
  `id_estudiante` int(11) NOT NULL COMMENT 'ID del usuario con rol estudiante',
  `id_grado_escolar` int(11) DEFAULT NULL COMMENT 'Grado y grupo específico',
  `fecha_asignacion` datetime DEFAULT current_timestamp(),
  `fecha_finalizacion` datetime DEFAULT NULL COMMENT 'Cuando termina la asignación',
  `estado` enum('ACTIVA','FINALIZADA','SUSPENDIDA') DEFAULT 'ACTIVA',
  `observaciones` text DEFAULT NULL COMMENT 'Notas del docente sobre el estudiante',
  `asignado_por` int(11) DEFAULT NULL COMMENT 'Admin que realizó la asignación',
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_docente_estudiante_activo` (`id_docente`,`id_estudiante`,`estado`),
  KEY `idx_docente` (`id_docente`),
  KEY `idx_estudiante` (`id_estudiante`),
  KEY `idx_estado` (`estado`),
  KEY `idx_grado_escolar` (`id_grado_escolar`),
  KEY `idx_asignado_por` (`asignado_por`),
  KEY `idx_docente_estado` (`id_docente`,`estado`),
  KEY `idx_estudiante_estado` (`id_estudiante`,`estado`),
  CONSTRAINT `fk_docente_estudiante_asignador` FOREIGN KEY (`asignado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_docente_estudiante_docente` FOREIGN KEY (`id_docente`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_docente_estudiante_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_docente_estudiante_grado` FOREIGN KEY (`id_grado_escolar`) REFERENCES `grados_escolares` (`id_grado`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Relación entre docentes y estudiantes asignados';

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.escenarios
CREATE TABLE IF NOT EXISTS `escenarios` (
  `id_escenario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_escenario` varchar(100) NOT NULL COMMENT 'Nombre del escenario',
  `categoria` enum('ANIMALES','FRUTAS','DEPORTES','ESCUELA','CASA','PARQUE','TIENDA','OTROS') DEFAULT 'OTROS',
  `contexto` text NOT NULL COMMENT 'JSON completo con toda la info del escenario',
  `imagen_url` varchar(255) DEFAULT NULL COMMENT 'URL o path de imagen del escenario',
  `nivel_recomendado` int(11) DEFAULT NULL COMMENT 'Nivel de dificultad recomendado',
  `grado_recomendado` varchar(10) DEFAULT NULL COMMENT 'Grado escolar recomendado',
  `popularidad` int(11) DEFAULT 0 COMMENT 'Contador de veces usado',
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` int(11) DEFAULT NULL COMMENT 'ID del docente/admin que lo creó',
  PRIMARY KEY (`id_escenario`),
  KEY `idx_categoria` (`categoria`),
  KEY `idx_nivel_recomendado` (`nivel_recomendado`),
  KEY `idx_grado_recomendado` (`grado_recomendado`),
  KEY `idx_activo` (`activo`),
  KEY `idx_creado_por` (`creado_por`),
  CONSTRAINT `fk_escenario_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_escenario_nivel` FOREIGN KEY (`nivel_recomendado`) REFERENCES `niveles_dificultad` (`id_nivel_dificultad`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Escenarios y contextos para problemas matemáticos';

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.estudiantes
CREATE TABLE IF NOT EXISTS `estudiantes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `numero_estudiante` varchar(20) DEFAULT NULL,
  `grado_actual` varchar(10) NOT NULL,
  `seccion` varchar(5) DEFAULT NULL,
  `promedio_general` decimal(3,2) DEFAULT NULL,
  `nivel_lectura` varchar(20) DEFAULT NULL,
  `nivel_matematicas` varchar(20) DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `tutor_principal_id` int(11) DEFAULT NULL,
  `estado_academico` enum('ACTIVO','INACTIVO','GRADUADO','RETIRADO') DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_estudiante` (`numero_estudiante`),
  KEY `usuario_id` (`usuario_id`),
  KEY `tutor_principal_id` (`tutor_principal_id`),
  CONSTRAINT `estudiantes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `estudiantes_ibfk_2` FOREIGN KEY (`tutor_principal_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.estudiante_acompañante
CREATE TABLE IF NOT EXISTS `estudiante_acompañante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `estudiante_id` int(11) NOT NULL,
  `acompañante_id` int(11) NOT NULL,
  `tipo_relacion` enum('PRINCIPAL','SECUNDARIO','EMERGENCIA') DEFAULT 'PRINCIPAL',
  `fecha_vinculacion` datetime DEFAULT current_timestamp(),
  `activo` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_principal` (`estudiante_id`,`tipo_relacion`),
  KEY `acompañante_id` (`acompañante_id`),
  CONSTRAINT `estudiante_acompañante_ibfk_1` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `estudiante_acompañante_ibfk_2` FOREIGN KEY (`acompañante_id`) REFERENCES `acompañantes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.grados_escolares
CREATE TABLE IF NOT EXISTS `grados_escolares` (
  `id_grado` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_grado` varchar(10) NOT NULL COMMENT 'Ej: 3°, 4°, 5°, 6°',
  `nombre_grupo` varchar(5) NOT NULL COMMENT 'Ej: A, B, C',
  `descripcion` varchar(200) DEFAULT NULL COMMENT 'Descripción adicional del grado y grupo',
  `nivel_educativo` enum('PREESCOLAR','PRIMARIA','SECUNDARIA') DEFAULT 'PRIMARIA',
  `capacidad_maxima` int(11) DEFAULT 30 COMMENT 'Número máximo de estudiantes',
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_grado`),
  UNIQUE KEY `unique_grado_grupo` (`nombre_grado`,`nombre_grupo`),
  KEY `idx_grado` (`nombre_grado`),
  KEY `idx_grupo` (`nombre_grupo`),
  KEY `idx_activo` (`activo`),
  KEY `idx_grado_activo` (`nombre_grado`,`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Grados escolares y grupos/secciones disponibles';

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.log_auditoria
CREATE TABLE IF NOT EXISTS `log_auditoria` (
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
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.niveles_dificultad
CREATE TABLE IF NOT EXISTS `niveles_dificultad` (
  `id_nivel_dificultad` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_nivel` varchar(50) NOT NULL COMMENT 'Ej: Básico, Intermedio, Avanzado',
  `orden` int(11) NOT NULL DEFAULT 0 COMMENT 'Orden de dificultad (1=más fácil)',
  `descripcion` text NOT NULL COMMENT 'JSON con configuración del nivel',
  `color_hex` varchar(7) DEFAULT '#4CAF50' COMMENT 'Color representativo del nivel',
  `icono` varchar(50) DEFAULT '⭐' COMMENT 'Emoji o icono del nivel',
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  PRIMARY KEY (`id_nivel_dificultad`),
  UNIQUE KEY `unique_nombre_nivel` (`nombre_nivel`),
  KEY `idx_orden` (`orden`),
  KEY `idx_activo` (`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Niveles de dificultad para ejercicios y juegos educativos';

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.password_resets_codes
CREATE TABLE IF NOT EXISTS `password_resets_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `code` varchar(10) NOT NULL,
  `expires_at` datetime NOT NULL,
  `used` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `fk_password_resets_user` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=1454 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.two_factor_codes
CREATE TABLE IF NOT EXISTS `two_factor_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `code` varchar(10) NOT NULL,
  `expires_at` datetime NOT NULL,
  `used` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `two_factor_codes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table sumixkids.usuario_roles
CREATE TABLE IF NOT EXISTS `usuario_roles` (
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

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
