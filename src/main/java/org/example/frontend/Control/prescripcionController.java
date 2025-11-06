package org.example.frontend.Control;

import org.example.common.Receta;
import org.example.backend.service.PrescripcionService;
import org.example.backend.dao.PacienteDao;
import org.example.common.Paciente;
import org.example.backend.dao.MedicamentoDao;
import org.example.common.Medicamento;
import org.example.frontend.View.prescribirView;

import javax.swing.*;
import java.time.LocalDate;

public class prescripcionController {

    private final prescribirView view;
    private final PrescripcionService service;
    private Receta recetaActual;
    private final PacienteDao pacienteDao = new PacienteDao();
    private final MedicamentoDao medicamentoDao = new MedicamentoDao();
    private final String medicoUsername;
    private final recetaController recetaController;

    public prescripcionController(prescribirView view, PrescripcionService service, String medicoUsername, recetaController recetaController) {
        this.view = view;
        this.service = service;
        this.medicoUsername = medicoUsername;
        this.recetaController = recetaController;
        cargarPacientesCombo();
        cargarMedicamentosCombo();
        initController();
    }

    // Inicializar eventos
    private void initController() {
        view.getBtnBuscarPaciente().addActionListener(e -> buscarPaciente());
        view.getBtnAgregar().addActionListener(e -> agregarMedicamento());
        view.getBtnEliminar().addActionListener(e -> eliminarMedicamento());
        view.getBtnRegistrar().addActionListener(e -> registrarReceta());
    }

    private void cargarPacientesCombo() {
        JComboBox<String> combo = view.getComboPacientes();
        combo.removeAllItems();
        for (Paciente p : pacienteDao.loadPacientes().getPacientes()) {
            combo.addItem(p.getId() + " - " + p.getName());
        }
    }

    private void cargarMedicamentosCombo() {
        JComboBox<String> combo = view.getComboMedicamentos();
        combo.removeAllItems();
        for (Medicamento m : medicamentoDao.loadMedicamentos().getMedicamentos()) {
            combo.addItem(m.getCodigo() + " - " + m.getNombre() + " (" + m.getPresentacion() + ")");
        }
    }

    private void buscarPaciente() {
        String selectedPaciente = (String) view.getComboPacientes().getSelectedItem();
        if (selectedPaciente == null || selectedPaciente.isEmpty()) {
            view.showMessage("Seleccione un paciente");
            return;
        }
        String idPaciente = selectedPaciente.split(" - ")[0];
        try {
            recetaActual = service.iniciarReceta(idPaciente);
            recetaActual = service.guardarReceta(recetaActual, medicoUsername);
            view.setTableModel(recetaActual.getMedicamentos());
            view.showMessage("Receta iniciada para paciente: " + idPaciente);
        } catch (Exception ex) {
            view.showMessage("Error: " + ex.getMessage());
        }
    }

    private void agregarMedicamento() {
        if (recetaActual == null) {
            view.showMessage("Primero selecciona un paciente");
            return;
        }
        String selectedMedicamento = (String) view.getComboMedicamentos().getSelectedItem();
        if (selectedMedicamento == null || selectedMedicamento.isEmpty()) {
            view.showMessage("Seleccione un medicamento");
            return;
        }
        String codigo = selectedMedicamento.split(" - ")[0];
        try {
            int cantidad = Integer.parseInt(view.getTxtCantidad().getText().trim());
            String indicaciones = view.getTxtIndicaciones().getText().trim();
            int dias = Integer.parseInt(view.getTxtDias().getText().trim());

            // Crear el detalle y agregarlo a la receta en memoria
            org.example.common.DetalleMedicamento detalle = new org.example.common.DetalleMedicamento(codigo, cantidad, indicaciones, dias);
            recetaActual.agregarMedicamento(detalle);
            service.actualizarReceta(recetaActual); // Actualiza la receta en la base de datos

            // Actualizar la tabla de la vista con los medicamentos actuales
            view.updateTable(recetaActual.getMedicamentos());

            // Limpiar los textfields
            view.getTxtCantidad().setText("");
            view.getTxtIndicaciones().setText("");
            view.getTxtDias().setText("");

        } catch (NumberFormatException e) {
            view.showMessage("Cantidad y días deben ser números");
        } catch (Exception e) {
            view.showMessage("Error: " + e.getMessage());
        }
    }

    private void eliminarMedicamento() {
        if (recetaActual == null) {
            view.showMessage("Primero selecciona un paciente");
            return;
        }

        int fila = view.getTblMedicamentos().getSelectedRow();
        if (fila == -1) {
            view.showMessage("Seleccione un medicamento de la tabla");
            return;
        }

        String codigo = (String) view.getTblMedicamentos().getValueAt(fila, 0);

        try {
            service.eliminarMedicamento(recetaActual.getIdReceta(), codigo);

            recetaActual = service.buscarRecetaPorId(recetaActual.getIdReceta());
            view.updateTable(recetaActual.getMedicamentos());

            view.showMessage("Medicamento eliminado correctamente");
        } catch (Exception e) {
            view.showMessage("Error: " + e.getMessage());
        }
    }

    private void registrarReceta() {
        if (recetaActual == null) {
            view.showMessage("No hay receta iniciada");
            return;
        }
        try {
            LocalDate fechaRetiro = LocalDate.now().plusDays(2); // ejemplo de fecha de retiro
            service.registrarReceta(recetaActual, fechaRetiro);
            view.showMessage("Receta registrada correctamente");
            recetaController.cargarRecetasAsync(); // Actualiza la tabla de recetas
        } catch (Exception e) {
            view.showMessage("Error: " + e.getMessage());
        }
    }
}