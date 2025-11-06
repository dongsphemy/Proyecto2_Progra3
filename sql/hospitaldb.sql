
CREATE DATABASE IF NOT EXISTS hospitaldb;
USE hospitaldb;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Medico', 'Farmaceuta', 'Paciente') NOT NULL
);

CREATE TABLE medicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    especialidad VARCHAR(100),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE farmaceuticos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE pacientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    fecha_nacimiento DATE,
    telefono VARCHAR(50),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE medicamentos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    presentacion VARCHAR(100)
);

CREATE TABLE recetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_receta VARCHAR(50) UNIQUE NOT NULL,
    medico_id INT NOT NULL,
    paciente_id INT NOT NULL,
    fecha_confeccion DATE NOT NULL,
    fecha_retiro DATE,
    estado ENUM('en_proceso','confeccionada','entregada') DEFAULT 'en_proceso',
    FOREIGN KEY (medico_id) REFERENCES medicos(id),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

CREATE TABLE detalle_medicamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    receta_id INT NOT NULL,
    codigo_medicamento VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL,
    indicaciones VARCHAR(255),
    duracion_dias INT,
    FOREIGN KEY (receta_id) REFERENCES recetas(id),
    FOREIGN KEY (codigo_medicamento) REFERENCES medicamentos(codigo)
);