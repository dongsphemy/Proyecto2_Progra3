package org.example.frontend.Control;

import org.example.backend.dao.FarmaceuticoDao;
import org.example.common.Farmaceutico;
import org.example.common.wrappers.FarmaceuticosWrapper;
import org.example.frontend.View.farmaceuticoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class farmaceuticoController implements ActionListener {

    private farmaceuticoView view;
    private FarmaceuticoDao dao;

    public farmaceuticoController(farmaceuticoView view, FarmaceuticoDao dao) {
        this.view = view;
        this.dao = dao;
        this.view.addListener(this);
        cargarTablaAsync();
    }

    private void cargarTablaAsync() {
        Thread thread = new Thread(() -> {
            FarmaceuticosWrapper wrapper = dao.loadFarmaceuticos();
            SwingUtilities.invokeLater(() -> {
                for (Farmaceutico f : wrapper.getFarmaceuticos()) {
                    view.modelAddRow(new Object[]{
                            f.getId(),
                            f.getName(),
                            f.getPassword()
                    });
                }
            });
        });
        thread.start();
    }

    private void limpiarTabla() {
        while (view.getTablaFarmaceuticos().getRowCount() > 0) {
            ((javax.swing.table.DefaultTableModel) view.getTablaFarmaceuticos().getModel()).removeRow(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == view.getGuardarButton()) {
            String id = view.getCampoId();
            String nombre = view.getCampoNombre();
            String clave = view.getCampoClave();

            if (id.isEmpty() || nombre.isEmpty()) {
                view.mostrarError("ID y Nombre son obligatorios");
                return;
            }

            // Al crear un farmaceutico, la clave por defecto será igual al ID
            if (clave.isEmpty()) {
                clave = id;
            }

            Farmaceutico f = new Farmaceutico(nombre, clave, id);
            dao.addFarmaceutico(f);

            limpiarTabla();
            cargarTablaAsync();
            view.clearAllText();
        }

        if (src == view.getBorrarButton()) {
            int row = view.tableGetSelectedRow();
            if (row >= 0) {
                String id = (String) view.getTablaFarmaceuticos().getValueAt(row, 0);
                dao.removeFarmaceuticoById(id);

                view.tableRemoveRow(row);
            } else {
                view.mostrarError("Seleccione un farmaceuta para borrar");
            }
        }

        if (src == view.getBuscarButton()) {
            String criterio = view.getCampoBusqNombre().getText();
            if (criterio.isEmpty()) {
                view.mostrarError("Ingrese un ID o nombre para buscar");
                return;
            }

            limpiarTabla();
            List<Farmaceutico> lista = dao.loadFarmaceuticos().getFarmaceuticos();
            for (Farmaceutico f : lista) {
                if (f.getId().equalsIgnoreCase(criterio) || f.getName().equalsIgnoreCase(criterio)) {
                    view.modelAddRow(new Object[]{f.getId(), f.getName(), f.getPassword()});
                }
            }
        }
    }
}
