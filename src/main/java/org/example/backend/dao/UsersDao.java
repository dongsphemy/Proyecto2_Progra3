package org.example.backend.dao;

import org.example.backend.database.ConexionDB;
import org.example.common.AbstractUser;
import org.example.common.wrappers.userWrapper;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDao {

    // agrega un usaurio
    public void addUser(AbstractUser user) {
        String sql = "INSERT INTO usuarios (name, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getId());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();

            System.out.println("Usuario agregado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // busca usuairo por id (BD)
    public AbstractUser searchUserById(String id) {
        String sql = "SELECT name, username, password, role FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                AbstractUser user = new AbstractUser() {}; // clase anónima, porque es abstracta
                user.setName(rs.getString("name"));
                user.setId(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // buscar usuario dentro de un wrapper cargado en memoria
    public AbstractUser searchUserById(userWrapper wrapper, String id) {
        if (wrapper == null || wrapper.getUsers() == null) return null;
        for (AbstractUser u : wrapper.getUsers()) {
            if (u.getId() != null && u.getId().equals(id)) return u;
        }
        return null;
    }

    // eliminar usuiaro por id
    public boolean removeUserById(String id) {
        String sql = "DELETE FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // listar todos los usuarios
    public List<AbstractUser> getAllUsers() {
        List<AbstractUser> list = new ArrayList<>();
        String sql = "SELECT name, username, password, role FROM usuarios";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AbstractUser user = new AbstractUser() {};
                user.setName(rs.getString("name"));
                user.setId(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // cargar todos los usuarios en un wrapper (para el frontend / proxy)
    public userWrapper loadUsers() {
        userWrapper wrapper = new userWrapper();
        List<AbstractUser> list = getAllUsers();
        wrapper.setUsers(list);
        return wrapper;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}