package org.example.frontend.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class recetaView extends JPanel {

    private JTable tablaRecetas;
    private DefaultTableModel tableModel;
    private JTextField campoBusqueda;
    private JButton buscarButton;
    private JButton verDetalleButton;

    public recetaView() {
        setLayout(new BorderLayout());

        // Panel de búsqueda
        JPanel searchPanel = new JPanel();
        campoBusqueda = new JTextField(15);
        buscarButton = new JButton("Buscar");
        verDetalleButton = new JButton("Ver Detalle");
        searchPanel.add(new JLabel("Buscar por ID o Paciente:"));
        searchPanel.add(campoBusqueda);
        searchPanel.add(buscarButton);
        searchPanel.add(verDetalleButton);

        add(searchPanel, BorderLayout.NORTH);

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"ID Receta", "Paciente", "Médico", "Fecha", "Estado"}, 0);
        tablaRecetas = new JTable(tableModel);
        add(new JScrollPane(tablaRecetas), BorderLayout.CENTER);
    }

    public JTable getTablaRecetas() { return tablaRecetas; }
    public String getCampoBusqueda() { return campoBusqueda.getText(); }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getVerDetalleButton() { return verDetalleButton; }

    public void modelAddRow(Object[] row) { tableModel.addRow(row); }
    public void clearTable() { tableModel.setRowCount(0); }
    public int tableGetSelectedRow() { return tablaRecetas.getSelectedRow(); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
}