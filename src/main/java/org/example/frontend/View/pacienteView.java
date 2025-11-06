package org.example.frontend.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class pacienteView extends JPanel {
    private JTextField campoId;
    private JTextField campoNombre;
    private JPasswordField campoPassword;
    private JTextField campoFechaNacimiento;
    private JTextField campoTelefono;

    private JButton guardarButton;
    private JButton borrarButton;
    private JButton buscarButton;

    private JTable tablaPacientes;
    private DefaultTableModel tableModel;

    public pacienteView() {
        setLayout(new BorderLayout());
        createUIComponents();

        JPanel panelPaciente = new JPanel();
        panelPaciente.setLayout(null);

        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(20, 20, 100, 30);
        campoId.setBounds(120, 20, 150, 30);
        panelPaciente.add(labelId);
        panelPaciente.add(campoId);

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setBounds(20, 60, 100, 30);
        campoNombre.setBounds(120, 60, 150, 30);
        panelPaciente.add(labelNombre);
        panelPaciente.add(campoNombre);

        JLabel labelPassword = new JLabel("Contraseña:");
        labelPassword.setBounds(20, 100, 100, 30);
        campoPassword.setBounds(120, 100, 150, 30);
        panelPaciente.add(labelPassword);
        panelPaciente.add(campoPassword);

        JLabel labelFechaNacimiento = new JLabel("Fecha de nacimiento:");
        labelFechaNacimiento.setBounds(20, 140, 120, 30);
        campoFechaNacimiento.setBounds(140, 140, 130, 30);
        panelPaciente.add(labelFechaNacimiento);
        panelPaciente.add(campoFechaNacimiento);

        JLabel labelTelefono = new JLabel("Teléfono:");
        labelTelefono.setBounds(20, 180, 100, 30);
        campoTelefono.setBounds(120, 180, 150, 30);
        panelPaciente.add(labelTelefono);
        panelPaciente.add(campoTelefono);

        guardarButton.setBounds(300, 20, 100, 30);
        borrarButton.setBounds(300, 60, 100, 30);
        buscarButton.setBounds(300, 100, 100, 30);
        panelPaciente.add(guardarButton);
        panelPaciente.add(borrarButton);
        panelPaciente.add(buscarButton);

        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        scrollPane.setBounds(20, 230, 600, 150);
        panelPaciente.add(scrollPane);

        panelPaciente.setPreferredSize(new Dimension(650, 400));
        add(panelPaciente, BorderLayout.CENTER);
    }

    private void createUIComponents() {
        campoId = new JTextField();
        campoNombre = new JTextField();
        campoPassword = new JPasswordField();
        campoFechaNacimiento = new JTextField();
        campoTelefono = new JTextField();
        guardarButton = new JButton("Guardar");
        borrarButton = new JButton("Borrar");
        buscarButton = new JButton("Buscar");
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Fecha Nacimiento", "Teléfono"}, 0);
        tablaPacientes = new JTable(tableModel);
        tablaPacientes.setRowHeight(30);
    }

    public String getCampoId() { return campoId.getText(); }
    public String getCampoNombre() { return campoNombre.getText(); }
    public String getCampoPassword() { return new String(campoPassword.getPassword()); }
    public String getCampoFechaNacimiento() { return campoFechaNacimiento.getText(); }
    public String getCampoTelefono() { return campoTelefono.getText(); }

    public void clearAllText() {
        campoId.setText("");
        campoNombre.setText("");
        campoPassword.setText("");
        campoFechaNacimiento.setText("");
        campoTelefono.setText("");
    }

    public JButton getGuardarButton() { return guardarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getBuscarButton() { return buscarButton; }

    public JTable getTablaPacientes() { return tablaPacientes; }

    public void modelAddRow(Object[] row) { tableModel.addRow(row); }
    public void tableRemoveRow(int index) { tableModel.removeRow(index); }
    public int tableGetSelectedRow() { return tablaPacientes.getSelectedRow(); }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addListener(ActionListener listener) {
        guardarButton.addActionListener(listener);
        borrarButton.addActionListener(listener);
        buscarButton.addActionListener(listener);
    }

    public void clearTable() {
        tableModel.setRowCount(0);
    }
}