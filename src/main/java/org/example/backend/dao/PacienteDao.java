package org.example.backend.dao;

import org.example.backend.database.ConexionDB;
import org.example.common.Paciente;
import org.example.common.wrappers.pacienteWrapper;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    // 🔹 Agregar nuevo paciente
    public boolean addPaciente(Paciente paciente) {
        String sqlUsuario = "INSERT INTO usuarios (name, username, password, role) VALUES (?, ?, ?, 'Paciente')";
        String sqlPaciente = "INSERT INTO pacientes (usuario_id, fecha_nacimiento, telefono) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            int usuarioId = -1;

            // 1️⃣ Insertar en usuarios
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, paciente.getName());
                stmt.setString(2, paciente.getId());
                stmt.setString(3, paciente.getPassword());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuarioId = rs.getInt(1);
                        System.out.println("ID generado para usuario: " + usuarioId);
                    } else throw new SQLException("No se generó ID de usuario.");
                }
            }

            // 2️⃣ Insertar en pacientes
            try (PreparedStatement stmt = conn.prepareStatement(sqlPaciente)) {
                stmt.setInt(1, usuarioId);
                try {
                    stmt.setDate(2, Date.valueOf(paciente.getFechaNacimiento()));
                } catch (IllegalArgumentException ex) {
                    System.err.println("Formato de fecha inválido: " + paciente.getFechaNacimiento());
                    JOptionPane.showMessageDialog(null, "La fecha debe tener formato yyyy-MM-dd", "Error de registro", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                    return false;
                }
                stmt.setString(3, paciente.getTelefono());
                stmt.executeUpdate();
                System.out.println("Paciente insertado con usuario_id: " + usuarioId);
            }

            conn.commit();
            System.out.println("✅ Paciente agregado correctamente: " + paciente.getName());
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("⚠️ Ya existe un paciente con el ID: " + paciente.getId());
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Buscar paciente por username
    public Paciente searchPacienteById(String username) {
        String sql = """
            SELECT u.name, u.username, u.password, p.fecha_nacimiento, p.telefono
            FROM usuarios u
            JOIN pacientes p ON u.id = p.usuario_id
            WHERE u.username = ?;
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Paciente(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("username"),
                        rs.getString("fecha_nacimiento"),
                        rs.getString("telefono")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 Eliminar paciente
    public boolean removePacienteById(String username) {
        String sqlFindUserId = "SELECT id FROM usuarios WHERE username = ?";
        String sqlDeletePaciente = "DELETE FROM pacientes WHERE usuario_id = ?";
        String sqlDeleteUsuario = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            int userId = -1;

            // Buscar id de usuario
            try (PreparedStatement stmt = conn.prepareStatement(sqlFindUserId)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) userId = rs.getInt("id");
            }

            if (userId == -1) {
                System.out.println("⚠️ No se encontró paciente con username: " + username);
                return false;
            }

            // Eliminar de pacientes
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeletePaciente)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Eliminar de usuarios
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteUsuario)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("🗑️ Paciente eliminado correctamente.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Obtener todos los pacientes
    public List<Paciente> getAllPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT u.name, u.username, u.password, p.fecha_nacimiento, p.telefono " +
                     "FROM usuarios u JOIN pacientes p ON u.id = p.usuario_id " +
                     "WHERE u.role = 'Paciente'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                Date fechaNacimientoSql = rs.getDate("fecha_nacimiento");
                String telefono = rs.getString("telefono");
                String fechaNacimiento = fechaNacimientoSql != null ? fechaNacimientoSql.toString() : "";
                Paciente paciente = new Paciente(name, password, username, fechaNacimiento, telefono);
                pacientes.add(paciente);
            }
            System.out.println("Pacientes encontrados: " + pacientes.size());
            for (Paciente p : pacientes) {
                System.out.println("Paciente: " + p.getName() + " | " + p.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    // 🔹 Cargar pacientes en wrapper
    public pacienteWrapper loadPacientes() {
        pacienteWrapper wrapper = new pacienteWrapper();
        List<Paciente> list = getAllPacientes();
        wrapper.setPacientes(list);
        return wrapper;
    }

    // Obtener el id numérico de usuario (usuarios.id) por username
    public int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Obtener el id autoincremental de la tabla pacientes usando el username
    public int getPacienteIdByUsername(String username) {
        String sql = "SELECT p.id FROM pacientes p JOIN usuarios u ON p.usuario_id = u.id WHERE u.username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}