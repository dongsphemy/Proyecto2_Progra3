package org.example.backend.dao;
import org.example.backend.database.ConexionDB;
import org.example.common.Medicamento;
import org.example.common.wrappers.medicamentoWrapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicamentoDao {

    // 🔹 Agregar nuevo medicamento
    public boolean addMedicamento(Medicamento medicamento) {
        String sql = "INSERT INTO medicamentos (codigo, nombre, presentacion) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicamento.getCodigo());
            stmt.setString(2, medicamento.getNombre());
            stmt.setString(3, medicamento.getPresentacion());

            int rows = stmt.executeUpdate();
            System.out.println("✅ Medicamento agregado: " + medicamento.getCodigo());
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("⚠️ Ya existe un medicamento con el código: " + medicamento.getCodigo());
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Eliminar medicamento por código
    public boolean removeMedicamentoByCodigo(String codigo) {
        String sql = "DELETE FROM medicamentos WHERE codigo = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("🗑️ Medicamento eliminado: " + codigo);
                return true;
            } else {
                System.out.println("⚠️ No se encontró medicamento con código: " + codigo);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Buscar medicamento por código
    public Optional<Medicamento> findByCodigo(String codigo) {
        String sql = "SELECT * FROM medicamentos WHERE codigo = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Medicamento m = new Medicamento(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("presentacion")
                );
                return Optional.of(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // 🔹 Buscar medicamentos por coincidencia (código o nombre)
    public List<Medicamento> search(String query) {
        List<Medicamento> list = new ArrayList<>();
        String sql = "SELECT * FROM medicamentos WHERE codigo LIKE ? OR nombre LIKE ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + query + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medicamento m = new Medicamento(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("presentacion")
                );
                list.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 Obtener todos los medicamentos
    public List<Medicamento> getAllMedicamentos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT codigo, nombre, presentacion FROM medicamentos";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Medicamento medicamento = new Medicamento();
                medicamento.setCodigo(rs.getString("codigo"));
                medicamento.setNombre(rs.getString("nombre"));
                medicamento.setPresentacion(rs.getString("presentacion"));
                medicamentos.add(medicamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicamentos;
    }

    // wrapper con medicametno
    public medicamentoWrapper loadMedicamentos() {
        medicamentoWrapper wrapper = new medicamentoWrapper();
        List<Medicamento> list = getAllMedicamentos();
        wrapper.setMedicamentos(list);
        return wrapper;
    }

    // actualizar medicamento
    public boolean updateMedicamento(Medicamento medicamento) {
        String sql = "UPDATE medicamentos SET nombre = ?, presentacion = ? WHERE codigo = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, medicamento.getNombre());
            stmt.setString(2, medicamento.getPresentacion());
            stmt.setString(3, medicamento.getCodigo());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}