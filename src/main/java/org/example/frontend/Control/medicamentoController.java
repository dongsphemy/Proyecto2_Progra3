package org.example.frontend.Control;

import org.example.common.Medicamento;
import org.example.backend.dao.MedicamentoDao;
import org.example.frontend.View.medicamentoView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingUtilities;

public class medicamentoController {
    private medicamentoView view;
    private MedicamentoDao dao;

    public medicamentoController(medicamentoView view, MedicamentoDao dao) {
        this.view = view;
        this.dao = dao;
        cargarMedicamentosAsync();
        view.addListener(new ButtonListener());
    }

    public medicamentoView getView() {
        return view;
    }


    private void cargarMedicamentosAsync() {
        Thread thread = new Thread(() -> {
            List<Medicamento> medicamentos = dao.loadMedicamentos().getMedicamentos();
            SwingUtilities.invokeLater(() -> {
                for (Medicamento m : medicamentos) {
                    Object[] row = { m.getCodigo(), m.getNombre(), m.getPresentacion() };
                    view.modelAddRow(row);
                }
            });
        });
        thread.start();
    }


    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Guardar medicamento
            if (e.getSource().equals(view.getGuardarButton())) {
                String codigo = view.getCampoCodigo();
                String nombre = view.getCampoNombre();
                String presentacion = view.getCampoPresentacion();

                if (codigo.isEmpty() || nombre.isEmpty() || presentacion.isEmpty()) {
                    view.mostrarError("Debe llenar todos los campos");
                    return;
                }

                // Crear y guardar medicamento
                Medicamento medicamento = new Medicamento(codigo, nombre, presentacion);
                dao.addMedicamento(medicamento);

                // Agregar a la tabla
                Object[] row = { codigo, nombre, presentacion };
                view.modelAddRow(row);
                view.clearAllText();
            }

            // Borrar medicamento
            else if (e.getSource().equals(view.getBorrarButton())) {
                int i = view.tableGetSelectedRow();
                if (i >= 0) {
                    String codigo = (String) view.getTablaMedicamentos().getValueAt(i, 0);

                    boolean removed = dao.removeMedicamentoByCodigo(codigo);
                    if (removed) {
                        view.tableRemoveRow(i);
                    } else {
                        view.mostrarError("No se pudo eliminar el medicamento con código: " + codigo);
                    }
                } else {
                    view.mostrarError("Seleccione un medicamento en la tabla");
                }
            }

            // Buscar medicamento por código o nombre
            else if (e.getSource().equals(view.getBuscarButton())) {
                String busqueda = view.getCampoBusq().getText();
                view.clearTable();

                if (busqueda.isEmpty()) {
                    cargarMedicamentosAsync();
                } else {
                    Thread thread = new Thread(() -> {
                        // buscar por código
                        dao.findByCodigo(busqueda).ifPresent(m -> {
                            SwingUtilities.invokeLater(() -> {
                                Object[] row = { m.getCodigo(), m.getNombre(), m.getPresentacion() };
                                view.modelAddRow(row);
                            });
                        });
                        // Buscar por nombre
                        List<Medicamento> encontrados = dao.search(busqueda);
                        SwingUtilities.invokeLater(() -> {
                            for (Medicamento m : encontrados) {
                                Object[] row = { m.getCodigo(), m.getNombre(), m.getPresentacion() };
                                view.modelAddRow(row);
                            }
                        });
                    });
                    thread.start();
                }
            }
        }
    }
}
