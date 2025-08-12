/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Hp
 * Created: 8/08/2025
 */

-- -----------------------------------------------------
-- Schema practica1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `practica1` DEFAULT CHARACTER SET utf8 ;
USE `practica1` ;

-- -----------------------------------------------------
-- Table `practica1`.`participante`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`participante` (
  `id` INT NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `tipo` VARCHAR(45) NULL,
  `institucion_procedencia` VARCHAR(150) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `practica1`.`evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`evento` (
  `codigo` VARCHAR(45) NOT NULL,
  `fecha` DATE NULL,
  `tipo` VARCHAR(45) NULL,
  `titulo_evento` VARCHAR(200) NULL,
  `ubicacion` VARCHAR(150) NULL,
  `cupo_maximo` INT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `practica1`.`actividad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`actividad` (
  `codigo` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(45) NULL,
  `titulo` VARCHAR(200) NULL,
  `hora_inicio` TIME NULL,
  `hora_fin` TIME NULL,
  `cupo_maximo` INT NULL,
  `id_participante_encargado` INT NOT NULL,
  `evento_codigo_evento` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `fk_actividad_id_encargado`
    FOREIGN KEY (`id_participante_encargado`)
    REFERENCES `practica1`.`participante` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_actividad_codigo_evento`
    FOREIGN KEY (`evento_codigo_evento`)
    REFERENCES `practica1`.`evento` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `practica1`.`pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`pago` (
  `id_pago` INT NOT NULL,
  `tipo_pago` VARCHAR(45) NULL,
  `monto` DECIMAL(10,2) NULL,
  `evento_codigo` VARCHAR(45) NOT NULL,
  `participante_id` INT NOT NULL,
  PRIMARY KEY (`id_pago`),
  CONSTRAINT `fk_pago_codigo_evento`
    FOREIGN KEY (`evento_codigo`)
    REFERENCES `practica1`.`evento` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pago_id_participante`
    FOREIGN KEY (`participante_id`)
    REFERENCES `practica1`.`participante` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `practica1`.`asistencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`asistencia` (
  `codigo_actividad` VARCHAR(45) NOT NULL,
  `id_participante` INT NOT NULL,
  PRIMARY KEY (`codigo_actividad`, `id_participante`),
  CONSTRAINT `fk_asistencia_codigo_actividad`
    FOREIGN KEY (`codigo_actividad`)
    REFERENCES `practica1`.`actividad` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asistencia_id_participante`
    FOREIGN KEY (`id_participante`)
    REFERENCES `practica1`.`participante` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `practica1`.`inscripcion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`inscripcion` (
  `codigo_evento` VARCHAR(45) NOT NULL,
  `id_participante` INT NOT NULL,
  `tipo` VARCHAR(45) NULL,
  `estatus` VARCHAR(45) NULL,
  PRIMARY KEY (`codigo_evento`, `id_participante`),
  CONSTRAINT `fk_inscripcion_codigo_evento`
    FOREIGN KEY (`codigo_evento`)
    REFERENCES `practica1`.`evento` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_inscripcion_id_participante`
    FOREIGN KEY (`id_participante`)
    REFERENCES `practica1`.`participante` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `practica1`.`certificado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica1`.`certificado` (
  `id_participante` INT NOT NULL,
  `codigo_evento` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_participante`, `codigo_evento`),
  CONSTRAINT `fk_certificado_id_participante`
    FOREIGN KEY (`id_participante`)
    REFERENCES `practica1`.`participante` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_certificado_codigo_evento`
    FOREIGN KEY (`codigo_evento`)
    REFERENCES `practica1`.`evento` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

