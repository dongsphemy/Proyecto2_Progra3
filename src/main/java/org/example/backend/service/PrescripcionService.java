package org.example.backend.service;

import org.example.backend.dao.MedicamentoDao;
import org.example.common.Receta;
import org.example.common.DetalleMedicamento;
import org.example.common.Paciente;
import org.example.backend.dao.RecetaDao;
import org.example.backend.dao.PacienteDao;
import org.example.backend.dao.MedicoDao;

import java.time.LocalDate;
import java.util.Optional;

public class PrescripcionService {

    private final PacienteDao pacienteDao;
    private final MedicoDao medicoDao;
    private final RecetaDao recetaDao;
    private final MedicamentoDao medicamentoDao;

    public PrescripcionService(PacienteDao pacienteDao, MedicoDao medicoDao, RecetaDao recetaDao, MedicamentoDao medicamentoDao) {
        this.pacienteDao = pacienteDao;
        this.medicoDao = medicoDao;
        this.recetaDao = recetaDao;
        this.medicamentoDao = medicamentoDao;
    }

    // Inicia una nueva receta para un paciente por un médico
    // Nota: este método solo crea el objeto Receta en memoria. Para persistirlo use guardarReceta(...)
    public Receta iniciarReceta(String idPaciente) {
        Paciente paciente = pacienteDao.searchPacienteById(idPaciente);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente no encontrado con ID: " + idPaciente);
        }

        // El constructor Receta(Paciente) no existe en la clase Receta, por eso
        // creamos la instancia con el constructor vacío y asignamos el paciente.
        Receta receta = new Receta();
        receta.setPaciente(paciente);
        receta.setFechaConfeccion(LocalDate.now());
        receta.setEstado("en_proceso");

        // Antes se intentaba llamar a recetaDao.saveNew(receta) pero la firma del DAO
        // requiere los IDs numéricos de medico y paciente. Dejamos que este método
        // solo cree la receta en memoria y proporcionamos guardarReceta(...) para
        // persistirla correctamente cuando se tengan los IDs necesarios.
        return receta;
    }

    // Persiste una receta creada (o modificada). Se necesita el username del médico
    // (username usado en tabla usuarios) para resolver el id numérico en la BD.
    public Receta guardarReceta(Receta receta, String medicoUsername) {
        if (receta == null) throw new IllegalArgumentException("Receta nula");
        if (receta.getPaciente() == null || receta.getPaciente().getId() == null) {
            throw new IllegalArgumentException("Receta debe tener un paciente con username (id)");
        }

        int pacienteId = pacienteDao.getPacienteIdByUsername(receta.getPaciente().getId());
        if (pacienteId == -1) throw new IllegalArgumentException("No se encontró el paciente en la base de datos: " + receta.getPaciente().getId());

        int medicoId = medicoDao.getUserIdByUsername(medicoUsername);
        if (medicoId == -1) throw new IllegalArgumentException("No se encontró el médico en la base de datos: " + medicoUsername);

        return recetaDao.saveNew(receta, medicoId, pacienteId);
    }

    // Agrega un medicamento a la receta
    public void agregarMedicamento(String idReceta, String codigoMedicamento, int cantidad, String indicaciones, int duracionDias) {
        Receta receta = recetaDao.findById(idReceta).orElseThrow(() ->
                new IllegalArgumentException("Receta no encontrada")
        );

        var medicamento = medicamentoDao.findByCodigo(codigoMedicamento)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado con código: " + codigoMedicamento));

        // Asocia el medicamento real al detalle
        DetalleMedicamento detalle = new DetalleMedicamento(
                medicamento.getCodigo(),
                cantidad,
                indicaciones,
                duracionDias
        );

        receta.agregarMedicamento(detalle);
        recetaDao.update(receta);
    }

    // Elimina un medicamento de la receta
    public void eliminarMedicamento(String idReceta, String detalle) {
        Receta receta = recetaDao.findById(idReceta).orElseThrow(() ->
                new IllegalArgumentException("Receta no encontrada")
        );

        receta.eliminarMedicamento(detalle);
        recetaDao.update(receta);
    }

    // Registra la receta definitiva con fecha de retiro
    public void registrarReceta(Receta receta, LocalDate fechaRetiro) {
        if (receta == null) {
            throw new IllegalArgumentException("Receta no encontrada");
        }
        if (receta.getMedicamentos() == null || receta.getMedicamentos().isEmpty()) {
            throw new IllegalStateException("No se puede registrar una receta sin medicamentos");
        }
        receta.registrar(fechaRetiro);
        recetaDao.update(receta);
    }

    // Obtener receta por ID
    public Optional<Receta> obtenerReceta(String idReceta) {
        return recetaDao.findById(idReceta);
    }

    public Receta buscarRecetaPorId(String idReceta) {
        return recetaDao.findById(idReceta).orElse(null);
    }

    public void actualizarReceta(Receta receta) {
        recetaDao.update(receta);
    }
}
