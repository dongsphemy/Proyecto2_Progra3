package org.example.backend.dao;

import org.example.backend.database.ConexionDB;
import org.example.common.DetalleMedicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleMedicamentoDao {

    public void addDetalle(int recetaId, DetalleMedicamento detalle) {
        String sql = "INSERT INTO detalle_medicamento (receta_id, codigo_medicamento, cantidad, indicaciones, duracion_dias) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recetaId);
            stmt.setString(2, detalle.getCodigoMedicamento());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setString(4, detalle.getIndicaciones());
            stmt.setInt(5, detalle.getDuracionDias());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DetalleMedicamento> getDetallesByRecetaId(int recetaId) {
        List<DetalleMedicamento> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_medicamento WHERE receta_id = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recetaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DetalleMedicamento d = new DetalleMedicamento();
                d.setId(rs.getInt("id"));
                d.setCodigoMedicamento(rs.getString("codigo_medicamento"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setIndicaciones(rs.getString("indicaciones"));
                d.setDuracionDias(rs.getInt("duracion_dias"));
                detalles.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detalles;
    }
}
