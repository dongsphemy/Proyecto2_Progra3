package org.example.frontend.View;

import org.example.common.DetalleMedicamento;
import org.example.common.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class prescribirView extends JPanel {

    // Campos de entrada
    private JComboBox<String> comboPacientes;
    private JComboBox<String> comboMedicamentos;
    private JTextField txtCantidad;
    private JTextField txtIndicaciones;
    private JTextField txtDias;


    // Botones
    private JButton btnBuscarPaciente;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnRegistrar;

    // Tabla de medicamentos
    private JTable tblMedicamentos;
    private DefaultTableModel tableModel;

    public prescribirView() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        // Campos de formulario
        comboPacientes = new JComboBox<>();
        comboMedicamentos = new JComboBox<>();
        txtCantidad = new JTextField();
        txtIndicaciones = new JTextField();
        txtDias = new JTextField();

        formPanel.add(new JLabel("Paciente:"));
        formPanel.add(comboPacientes);
        formPanel.add(new JLabel("Medicamento:"));
        formPanel.add(comboMedicamentos);
        formPanel.add(new JLabel("Cantidad:"));
        formPanel.add(txtCantidad);

        formPanel.add(new JLabel("Indicaciones:"));
        formPanel.add(txtIndicaciones);

        formPanel.add(new JLabel("Duración (días):"));
        formPanel.add(txtDias);

        // Botones
        btnBuscarPaciente = new JButton("Buscar Paciente");
        btnAgregar = new JButton("Agregar Medicamento");
        btnEliminar = new JButton("Eliminar Medicamento");
        btnRegistrar = new JButton("Registrar Receta");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.add(btnBuscarPaciente);
        btnPanel.add(btnAgregar);
        btnPanel.add(btnEliminar);

        // Tabla de medicamentos
        String[] columnNames = {"Código", "Cantidad", "Indicaciones", "Duración (días)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblMedicamentos = new JTable(tableModel);

        // Panel principal
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(tblMedicamentos), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(btnPanel, BorderLayout.CENTER);
        southPanel.add(btnRegistrar, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    // Métodos que usará el controlador

    public JTextField getTxtCantidad() { return txtCantidad; }
    public JTextField getTxtIndicaciones() { return txtIndicaciones; }
    public JTextField getTxtDias() { return txtDias; }


    public JButton getBtnBuscarPaciente() { return btnBuscarPaciente; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnRegistrar() { return btnRegistrar; }

    public JTable getTblMedicamentos() { return tblMedicamentos; }
    public JComboBox<String> getComboPacientes() { return comboPacientes; }
    public JComboBox<String> getComboMedicamentos() { return comboMedicamentos; }

    public void setTableModel(List<DetalleMedicamento> items) {
        tableModel.setRowCount(0); // limpia tabla
        for (DetalleMedicamento d : items) {
            tableModel.addRow(new Object[]{
                    d.getCodigoMedicamento(),
                    d.getCantidad(),
                    d.getIndicaciones(),
                    d.getDuracionDias()
            });
        }
    }

    public void updateTable(List<DetalleMedicamento> items) {
        setTableModel(items);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Estos métodos permiten poblar los combos desde el controlador (remoto)
    public void setPacientesComboItems(List<String> items) {
        comboPacientes.removeAllItems();
        for (String s : items) comboPacientes.addItem(s);
    }

    public void setMedicamentosComboItems(List<String> items) {
        comboMedicamentos.removeAllItems();
        for (String s : items) comboMedicamentos.addItem(s);
    }
}