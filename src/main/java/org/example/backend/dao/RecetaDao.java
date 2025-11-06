package org.example.backend.dao;

import org.example.backend.database.ConexionDB;
import org.example.common.DetalleMedicamento;
import org.example.common.Receta;
import org.example.common.wrappers.recetaWrapper;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecetaDao {

    // 🔹 Crear nueva receta
    public Receta saveNew(Receta receta, int medicoId, int pacienteId) {
        String sql = """
            INSERT INTO recetas (id_receta, medico_id, paciente_id, fecha_confeccion, estado)
            VALUES (?, ?, ?, ?, ?);
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (receta.getIdReceta() == null || receta.getIdReceta().isEmpty()) {
                receta.setIdReceta("R-" + System.currentTimeMillis());
            }

            stmt.setString(1, receta.getIdReceta());
            stmt.setInt(2, medicoId);
            stmt.setInt(3, pacienteId);
            stmt.setDate(4, Date.valueOf(receta.getFechaConfeccion() != null ?
                    receta.getFechaConfeccion() : LocalDate.now()));
            stmt.setString(5, receta.getEstado());

            stmt.executeUpdate();

            // Obtener ID autogenerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int recetaId = rs.getInt(1);

                    // Guardar los detalles si existen
                    if (receta.getMedicamentos() != null && !receta.getMedicamentos().isEmpty()) {
                        DetalleMedicamentoDao detalleDao = new DetalleMedicamentoDao();
                        for (DetalleMedicamento d : receta.getMedicamentos()) {
                            detalleDao.addDetalle(recetaId, d);
                        }
                    }

                    System.out.println("✅ Receta guardada: " + receta.getIdReceta());
                }
            }

            return receta;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 Actualizar receta (estado, fecha de retiro, etc.)
    public boolean update(Receta receta) {
        String sql = "UPDATE recetas SET estado = ?, fecha_retiro = ? WHERE id_receta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, receta.getEstado());
            if (receta.getFechaRetiro() != null) {
                stmt.setDate(2, Date.valueOf(receta.getFechaRetiro()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, receta.getIdReceta());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper: obtener id interno (autoincremental) usando id_receta lógico
    public int getInternalId(String idReceta) {
        String sql = "SELECT id FROM recetas WHERE id_receta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idReceta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Buscar receta por ID lógico (carga también detalles)
    public Optional<Receta> findById(String idReceta) {
        String sql = "SELECT * FROM recetas WHERE id_receta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idReceta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getString("id_receta"));
                receta.setFechaConfeccion(rs.getDate("fecha_confeccion").toLocalDate());
                receta.setEstado(rs.getString("estado"));
                receta.setFechaRetiro(rs.getDate("fecha_retiro") != null ? rs.getDate("fecha_retiro").toLocalDate() : null);
                // Cargar medicamentos desde detalle_medicamento usando id interno
                int internalId = rs.getInt("id");
                DetalleMedicamentoDao detalleDao = new DetalleMedicamentoDao();
                receta.setMedicamentos(detalleDao.getDetallesByRecetaId(internalId));
                return Optional.of(receta);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // 🔹 Obtener todas las recetas
    public List<Receta> findAll() {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT * FROM recetas";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getString("id_receta"));
                receta.setFechaConfeccion(rs.getDate("fecha_confeccion").toLocalDate());
                Date fechaRetiro = rs.getDate("fecha_retiro");
                if (fechaRetiro != null) receta.setFechaRetiro(fechaRetiro.toLocalDate());
                receta.setEstado(rs.getString("estado"));

                // Cargar detalles
                DetalleMedicamentoDao detalleDao = new DetalleMedicamentoDao();
                receta.setMedicamentos(detalleDao.getDetallesByRecetaId(rs.getInt("id")));

                recetas.add(receta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

    // 🔹 Filtrar por estado
    public List<Receta> findByEstado(String estado) {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT * FROM recetas WHERE estado = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getString("id_receta"));
                receta.setFechaConfeccion(rs.getDate("fecha_confeccion").toLocalDate());
                receta.setEstado(rs.getString("estado"));
                recetas.add(receta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

    // 🔹 Buscar recetas por paciente
    public List<Receta> findByPaciente(int pacienteId) {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT * FROM recetas WHERE paciente_id = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getString("id_receta"));
                receta.setFechaConfeccion(rs.getDate("fecha_confeccion").toLocalDate());
                receta.setEstado(rs.getString("estado"));
                recetas.add(receta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

    public recetaWrapper loadRecetas() {
        recetaWrapper wrapper = new recetaWrapper();
        List<Receta> recetas = new ArrayList<>();

        String sql = "SELECT * FROM recetas";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getString("id_receta"));
                receta.setFechaConfeccion(rs.getDate("fecha_confeccion").toLocalDate());

                Date fechaRetiro = rs.getDate("fecha_retiro");
                if (fechaRetiro != null) {
                    receta.setFechaRetiro(fechaRetiro.toLocalDate());
                }

                receta.setEstado(rs.getString("estado"));

                // 🔹 Si quieres también cargar los detalles (medicamentos)
                DetalleMedicamentoDao detalleDao = new DetalleMedicamentoDao();
                receta.setMedicamentos(detalleDao.getDetallesByRecetaId(rs.getInt("id")));

                recetas.add(receta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        wrapper.setRecetas(recetas);
        return wrapper;
    }

    public Receta[] getAll() {
        List<Receta> list = loadRecetas().getRecetas();
        return list.toArray(new Receta[0]);
    }
}
