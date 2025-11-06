package org.example.backend.dao;

import org.example.backend.database.ConexionDB;
import org.example.common.Farmaceutico;
import org.example.common.wrappers.FarmaceuticosWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FarmaceuticoDao {

    // insercion de un objeto farmaceutico en la base de datos
    public boolean addFarmaceutico(Farmaceutico farmaceutico) {
        String insertUsuarioSQL = "INSERT INTO usuarios (name, username, password, role) VALUES (?, ?, ?, 'Farmaceuta')";
        String insertFarmaceuticoSQL = "INSERT INTO farmaceuticos (usuario_id) VALUES (?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false); // inicia transacción

            int userId;
            try (PreparedStatement stmt = conn.prepareStatement(insertUsuarioSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, farmaceutico.getName());
                stmt.setString(2, farmaceutico.getId()); // usamos id como username
                stmt.setString(3, farmaceutico.getPassword());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) userId = rs.getInt(1);
                    else throw new SQLException("No se pudo obtener ID del usuario.");
                }
            }

            //Insertar en tabla farmaceuticos
            try (PreparedStatement stmt = conn.prepareStatement(insertFarmaceuticoSQL)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("⚠️ Ya existe un farmaceutico con el ID: " + farmaceutico.getId());
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // buscar farmaceutico por id
    public Farmaceutico searchFarmaceuticoById(String username) {
        String sql = """
            SELECT u.name, u.username, u.password, u.role
            FROM usuarios u
            JOIN farmaceuticos f ON u.id = f.usuario_id
            WHERE u.username = ?;
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Farmaceutico f = new Farmaceutico();
                f.setName(rs.getString("name"));
                f.setId(rs.getString("username"));
                f.setPassword(rs.getString("password"));
                f.setRole(rs.getString("role"));
                return f;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // eliminar objeto farmaceutico por id
    public boolean removeFarmaceuticoById(String username) {
        String deleteFarmaceuticoSQL = "DELETE FROM farmaceuticos WHERE usuario_id = (SELECT id FROM usuarios WHERE username = ?)";
        String deleteUsuarioSQL = "DELETE FROM usuarios WHERE username = ?"; // <-- faltaba esta declaración

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteFarmaceuticoSQL);
                 PreparedStatement stmt2 = conn.prepareStatement(deleteUsuarioSQL)) {

                stmt1.setString(1, username);
                stmt1.executeUpdate();

                stmt2.setString(1, username);
                int affected = stmt2.executeUpdate();

                conn.commit();
                return affected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //lista de farmaceuticos
    public List<Farmaceutico> getAllFarmaceuticos() {
        List<Farmaceutico> list = new ArrayList<>();

        String sql = """
            SELECT u.name, u.username, u.password, u.role
            FROM usuarios u
            JOIN farmaceuticos f ON u.id = f.usuario_id;
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Farmaceutico f = new Farmaceutico();
                f.setName(rs.getString("name"));
                f.setId(rs.getString("username"));
                f.setPassword(rs.getString("password"));
                f.setRole(rs.getString("role"));
                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // NEW: cargar farmaceuticos en wrapper
    public FarmaceuticosWrapper loadFarmaceuticos() {
        FarmaceuticosWrapper wrapper = new FarmaceuticosWrapper();
        List<Farmaceutico> list = getAllFarmaceuticos();
        wrapper.setFarmaceuticos(list);
        return wrapper;
    }
}