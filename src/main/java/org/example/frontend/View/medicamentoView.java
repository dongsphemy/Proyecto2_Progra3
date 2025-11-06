package org.example.frontend.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class medicamentoView extends JPanel {
    DefaultTableModel model;

    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoPresentacion;

    private JButton guardarButton;
    private JButton borrarButton;
    private JButton buscarButton;
    private JButton modificarButton;

    private JTextField campoBusq;
    private JTable tablaMedicamentos;
    private JScrollPane scrollPane;

    public medicamentoView() {
        setLayout(new BorderLayout());
        addComponents();
    }

    public void modelAddRow(Object[] objects){
        model.addRow(objects);
    }

    public void clearAllText(){
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPresentacion.setText("");
    }

    public void clearTable(){
        model.setRowCount(0);
    }

    public JButton getGuardarButton() { return guardarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getModificarButton() { return modificarButton; }

    public String getCampoCodigo() { return campoCodigo.getText(); }
    public String getCampoNombre() { return campoNombre.getText(); }
    public String getCampoPresentacion() { return campoPresentacion.getText(); }
    public JTextField getCampoBusq() { return campoBusq; }

    public JTable getTablaMedicamentos() { return tablaMedicamentos; }

    public void setCampoCodigo(String codigo) { campoCodigo.setText(codigo); }
    public void setCampoNombre(String nombre) { campoNombre.setText(nombre); }
    public void setCampoPresentacion(String presentacion) { campoPresentacion.setText(presentacion); }

    public void addListener(ActionListener al){
        guardarButton.addActionListener(al);
        borrarButton.addActionListener(al);
        buscarButton.addActionListener(al);
        modificarButton.addActionListener(al);
    }

    private void addComponents(){
        createUIComponents();
        JPanel panel = new JPanel();
        panel.setLayout(null);

        Object[] columns = {"Código","Nombre","Presentación"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        tablaMedicamentos.setModel(model);

        // Labels
        JLabel labelCodigo = new JLabel("Código:");
        labelCodigo.setBounds(20, 20, 80, 30);
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setBounds(20, 60, 80, 30);
        JLabel labelPresentacion = new JLabel("Presentación:");
        labelPresentacion.setBounds(20, 100, 100, 30);

        // Campos
        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoPresentacion = new JTextField();
        campoBusq = new JTextField();
        campoCodigo.setBounds(110, 20, 150, 30);
        campoNombre.setBounds(110, 60, 150, 30);
        campoPresentacion.setBounds(110, 100, 150, 30);

        // Botones
        guardarButton = new JButton("Guardar");
        borrarButton = new JButton("Borrar");
        modificarButton = new JButton("Modificar");
        buscarButton = new JButton("Buscar");
        guardarButton.setBounds(300, 20, 100, 30);
        borrarButton.setBounds(300, 60, 100, 30);
        modificarButton.setBounds(300, 100, 100, 30);
        buscarButton.setBounds(300, 140, 100, 30);

        campoBusq.setBounds(420, 20, 200, 30);
        scrollPane.setBounds(20, 200, 850, 150);

        // Agregar labels y campos
        panel.add(labelCodigo);
        panel.add(campoCodigo);
        panel.add(labelNombre);
        panel.add(campoNombre);
        panel.add(labelPresentacion);
        panel.add(campoPresentacion);
        panel.add(guardarButton);
        panel.add(borrarButton);
        panel.add(modificarButton);
        panel.add(buscarButton);
        panel.add(campoBusq);
        panel.add(scrollPane);

        add(panel, BorderLayout.CENTER);
    }

    private void createUIComponents() {
        tablaMedicamentos = new JTable();
        tablaMedicamentos.setRowHeight(30);
        scrollPane = new JScrollPane(tablaMedicamentos);
    }

    public int tableGetSelectedRow() {
        return tablaMedicamentos.getSelectedRow();
    }

    public void tableRemoveRow(int i) {
        model.removeRow(i);
    }

    public void mostrarError(String msj) {
        JOptionPane.showMessageDialog(this, msj, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
