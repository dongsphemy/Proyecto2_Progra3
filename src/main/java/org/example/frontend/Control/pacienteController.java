package org.example.frontend.Control;

import org.example.common.Paciente;
import org.example.backend.dao.PacienteDao;
import org.example.frontend.View.pacienteView;
import org.example.common.wrappers.pacienteWrapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class pacienteController {
    private pacienteView view;
    private PacienteDao dao;

    public pacienteController(pacienteView view, PacienteDao dao) {
        this.view = view;
        this.dao = dao;
        cargarPacientesAsync();
        view.addListener(new ButtonListener());
    }

    public pacienteView getView() {
        return view;
    }

    private void cargarPacientes() {
        pacienteWrapper wrapper = dao.loadPacientes();
        for (Paciente p : wrapper.getPacientes()) {
            Object[] row = { p.getId(), p.getName(), p.getFechaNacimiento(), p.getTelefono() };
            view.modelAddRow(row);
        }
    }

    private void cargarPacientesAsync() {
        Thread thread = new Thread(() -> {
            pacienteWrapper wrapper = dao.loadPacientes();
            SwingUtilities.invokeLater(() -> {
                for (Paciente p : wrapper.getPacientes()) {
                    Object[] row = { p.getId(), p.getName(), p.getFechaNacimiento(), p.getTelefono() };
                    view.modelAddRow(row);
                }
            });
        });
        thread.start();
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Guardar paciente
            if (e.getSource().equals(view.getGuardarButton())) {
                String id = view.getCampoId();
                String nombre = view.getCampoNombre();
                String password = view.getCampoPassword();
                String fechaNacimiento = view.getCampoFechaNacimiento();
                String telefono = view.getCampoTelefono();

                if (id.isEmpty() || nombre.isEmpty() || password.isEmpty() || fechaNacimiento.isEmpty() || telefono.isEmpty()) {
                    view.mostrarError("Debe llenar todos los campos");
                    return;
                }

                Paciente paciente = new Paciente(nombre, password, id, fechaNacimiento, telefono);
                dao.addPaciente(paciente);

                Object[] row = { id, nombre, fechaNacimiento, telefono };
                view.modelAddRow(row);
                view.clearAllText();
                // Recargar la tabla desde la base de datos para reflejar todos los pacientes
                view.clearTable();
                cargarPacientes();
            }

            // Borrar paciente
            else if (e.getSource().equals(view.getBorrarButton())) {
                int i = view.tableGetSelectedRow();
                if (i >= 0) {
                    String id = (String) view.getTablaPacientes().getValueAt(i, 0);

                    boolean removed = dao.removePacienteById(id);
                    if (removed) {
                        view.tableRemoveRow(i);
                    } else {
                        view.mostrarError("No se pudo eliminar el paciente con ID: " + id);
                    }
                } else {
                    view.mostrarError("Seleccione un paciente en la tabla");
                }
            }
        }
    }
}
