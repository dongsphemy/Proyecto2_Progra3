package org.example.backend.dao;

import org.example.backend.database.ConexionDB;
import org.example.common.Medico;
import org.example.common.wrappers.medicoWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {

    // 🔹 Agregar nuevo médico
    public boolean addMedico(Medico medico) {
        String sqlUsuario = "INSERT INTO usuarios (name, username, password, role) VALUES (?, ?, ?, 'Medico')";
        String sqlMedico = "INSERT INTO medicos (usuario_id, especialidad) VALUES (?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción

            int usuarioId;

            // 1️⃣ Insertar en usuarios
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, medico.getName());
                stmt.setString(2, medico.getId()); // el username
                stmt.setString(3, medico.getPassword());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) usuarioId = rs.getInt(1);
                    else throw new SQLException("No se generó ID de usuario para el médico.");
                }
            }

            // 2️⃣ Insertar en medicos
            try (PreparedStatement stmt = conn.prepareStatement(sqlMedico)) {
                stmt.setInt(1, usuarioId);
                stmt.setString(2, medico.getEspecialidad());
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Médico agregado correctamente: " + medico.getName());
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("⚠️ Ya existe un médico con el ID: " + medico.getId());
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Buscar médico por username (id lógico)
    public Medico searchMedicoById(String username) {
        String sql = """
            SELECT u.name, u.username, u.password, m.especialidad
            FROM usuarios u
            JOIN medicos m ON u.id = m.usuario_id
            WHERE u.username = ?;
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Medico(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("username"),
                        rs.getString("especialidad")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 Eliminar médico por username
    public boolean removeMedicoById(String username) {
        String sqlFindUserId = "SELECT id FROM usuarios WHERE username = ?";
        String sqlDeleteMedico = "DELETE FROM medicos WHERE usuario_id = ?";
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
                System.out.println("⚠️ No se encontró médico con ID: " + username);
                return false;
            }

            // Eliminar de medicos
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteMedico)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Eliminar de usuarios
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteUsuario)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("🗑️ Médico eliminado correctamente.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Obtener todos los médicos
    public List<Medico> getAllMedicos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = """
            SELECT u.name, u.username, u.password, m.especialidad
            FROM usuarios u
            JOIN medicos m ON u.id = m.usuario_id;
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("username"),
                        rs.getString("especialidad")
                );
                medicos.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicos;
    }

    // 🔹 Cargar médicos en wrapper
    public medicoWrapper loadMedicos() {
        medicoWrapper wrapper = new medicoWrapper();
        List<Medico> list = getAllMedicos();
        wrapper.setMedicos(list);
        return wrapper;
    }

    // 🔹 Obtener el id numérico de usuario (usuarios.id) por username
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
}
