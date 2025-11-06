-- script de prueba
USE hospitaldb;
INSERT INTO usuarios (name, username, password, role) VALUES
('Admin1', 'admin2', 'admin3', 'Admin4'),

('Dr. Carlos García', 'dr.garcia', 'medico123', 'Medico'),
('Dra. Ana López', 'dr.lopez', 'medico123', 'Medico'),
('Dr. Roberto Martínez', 'dr.martinez', 'medico123', 'Medico'),

('María Fernández', 'farm.maria', 'farm123', 'Farmaceuta'),
('José Ramírez', 'farm.jose', 'farm123', 'Farmaceuta'),

('Juan Pérez', 'juan.perez', 'paciente123', 'Paciente'),
('María González', 'maria.gonzalez', 'paciente123', 'Paciente'),
('Pedro Rodríguez', 'pedro.rodriguez', 'paciente123', 'Paciente'),
('Laura Sánchez', 'laura.sanchez', 'paciente123', 'Paciente'),
('Carlos Díaz', 'carlos.diaz', 'paciente123', 'Paciente');

INSERT INTO medicos (usuario_id, especialidad) VALUES
(2, 'Medicina General'),
(3, 'Pediatría'),
(4, 'Cardiología');

INSERT INTO farmaceuticos (usuario_id) VALUES
(5),
(6);

INSERT INTO pacientes (usuario_id, fecha_nacimiento, telefono) VALUES
(7, '1985-03-15', '555-0001'),
(8, '1990-07-22', '555-0002'),
(9, '1978-11-05', '555-0003'),
(10, '1995-02-18', '555-0004'),
(11, '1982-09-30', '555-0005');

INSERT INTO medicamentos (codigo, nombre, presentacion) VALUES
-- Analgésicos
('MED001', 'Paracetamol', 'Tabletas 500mg'),
('MED002', 'Ibuprofeno', 'Tabletas 400mg'),
('MED003', 'Aspirina', 'Tabletas 100mg'),

INSERT INTO recetas (id_receta, medico_id, paciente_id, fecha_confeccion, fecha_retiro, estado) VALUES
('R-2024001', 1, 1, '2024-01-15', '2024-01-17', 'confeccionada'),
('R-2024002', 2, 2, '2024-01-16', '2024-01-18', 'confeccionada'),
('R-2024003', 1, 3, '2024-01-17', '2024-01-19', 'confeccionada'),

('R-2024004', 3, 4, '2024-01-10', '2024-01-12', 'entregada'),
('R-2024005', 2, 5, '2024-01-12', '2024-01-14', 'entregada'),

('R-2024006', 1, 1, '2024-01-20', NULL, 'en_proceso'),
('R-2024007', 3, 3, '2024-01-21', NULL, 'en_proceso');

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(1, 'MED001', 10, 'Tomar 1 tableta cada 8 horas con las comidas', 5),
(1, 'MED002', 14, 'Tomar 1 cápsula cada 12 horas', 7);

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(2, 'MED003', 30, 'Tomar 1 tableta al día por la mañana', 30),
(2, 'MED001', 30, 'Tomar 1 tableta al día por la noche si es necesario', 30);

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(3, 'MED002', 20, 'Tomar 1 tableta cada 8 horas después de las comidas', 10),
(3, 'MED003', 15, 'Tomar 1 tableta cada 12 horas con alimentos', 7);

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(4, 'MED001', 30, 'Tomar 1 tableta al día en ayunas', 30),
(4, 'MED002', 30, 'Tomar 1 cápsula al día antes del desayuno', 30);

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(5, 'MED001', 5, 'Tomar 1 tableta al día', 5),
(5, 'MED002', 15, 'Tomar 1 tableta cada 8 horas si hay fiebre', 5);

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(6, 'MED003', 60, 'Tomar 1 cápsula antes del desayuno', 30),
(6, 'MED002', 60, 'Tomar 1 tableta antes de cenar', 30);

INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES
(7, 'MED001', 2, 'Usar 2 inhalaciones cada 12 horas', 30);

SELECT 'datos de prueba insertados exitosamente' AS Mensaje;

