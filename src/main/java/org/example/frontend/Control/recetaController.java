package org.example.frontend.Control;

import org.example.common.Receta;
import org.example.backend.dao.RecetaDao;
import org.example.frontend.View.recetaView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

public class recetaController {

    private recetaView view;
    private RecetaDao dao;

    public recetaController(recetaView view, RecetaDao dao) {
        this.view = view;
        this.dao = dao;
        cargarRecetasAsync();
        view.getBuscarButton().addActionListener(new ButtonListener());
        view.getVerDetalleButton().addActionListener(new ButtonListener());
    }

    public void cargarRecetasAsync() {
        Thread thread = new Thread(() -> {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Receta[] recetas = dao.getAll();
            SwingUtilities.invokeLater(() -> {
                view.clearTable();
                for (Receta r : recetas) {
                    Object[] row = {
                            r.getIdReceta(),
                            r.getPaciente() != null ? r.getPaciente().getName() : "N/A",
                            (r.getMedico() != null && r.getMedico().getName() != null) ? r.getMedico().getName() : "N/A",
                            r.getFechaConfeccion() != null ? r.getFechaConfeccion().format(fmt) : "",
                            r.getEstado()
                    };
                    view.modelAddRow(row);
                }
            });
        });
        thread.start();
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == view.getBuscarButton()) {
                String filtro = view.getCampoBusqueda().trim();
                if (filtro.isEmpty()) {
                    cargarRecetasAsync();
                } else {
                    view.clearTable();
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    dao.findAll().stream()
                            .filter(r ->
                                    r.getIdReceta().equalsIgnoreCase(filtro)
                                            || (r.getPaciente() != null && r.getPaciente().getId().equalsIgnoreCase(filtro))
                            )
                            .forEach(r -> {
                                Object[] row = {
                                        r.getIdReceta(),
                                        r.getPaciente() != null ? r.getPaciente().getName() : "N/A",
                                        (r.getMedico() != null && r.getMedico().getName() != null) ? r.getMedico().getName() : "N/A",
                                        r.getFechaConfeccion() != null ? r.getFechaConfeccion().format(fmt) : "",
                                        r.getEstado()
                                };
                                view.modelAddRow(row);
                            });
                }
            } else if (e.getSource() == view.getVerDetalleButton()) {
                int i = view.tableGetSelectedRow();
                if (i >= 0) {
                    String id = (String) view.getTablaRecetas().getValueAt(i, 0);
                    dao.findById(id).ifPresentOrElse(r -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append("ID: ").append(r.getIdReceta()).append("\n");
                        sb.append("Paciente: ").append(r.getPaciente() != null ? r.getPaciente().getName() : "N/A").append("\n");
                        sb.append("Médico: ").append((r.getMedico() != null && r.getMedico().getName() != null) ? r.getMedico().getName() : "N/A").append("\n");
                        sb.append("Fecha Confección: ").append(r.getFechaConfeccion()).append("\n");
                        sb.append("Fecha Retiro: ").append(r.getFechaRetiro() != null ? r.getFechaRetiro() : "N/A").append("\n");
                        sb.append("Estado: ").append(r.getEstado()).append("\n");
                        sb.append("Medicamentos: \n");
                        r.getMedicamentos().forEach(m -> sb.append("- ").append(m.getNombreMedicamento()).append(" x").append(m.getCantidad()).append("\n"));
                        JOptionPane.showMessageDialog(view, sb.toString(), "Detalle Receta", JOptionPane.INFORMATION_MESSAGE);
                    }, () -> view.mostrarError("Receta no encontrada"));
                } else {
                    view.mostrarError("Seleccione una receta en la tabla");
                }
            }
        }
    }


    public recetaView getView() {
        return view;
    }
}
