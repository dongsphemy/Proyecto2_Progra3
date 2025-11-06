package org.example.frontend.View;

import javax.swing.*;
import java.awt.*;

public class registroView extends JFrame{
    private JPanel panel;
    private JLabel apellidos;
    private JLabel nombre;
    private JLabel especialidad;
    private JTextField campoNombre;
    private JButton ingresarButton;
    private JPasswordField campoContra;
    private JTextField campoId;
    private JTextField campoEspecialidad;
    private JPasswordField campoConfirmaContra;
    private JButton registrarseButton;
    private JLabel registroLink;
    private JComboBox comboBoxRol;
    private JTextField campoFecha;
    private JLabel recuperaPassword;
    private JLabel contraRegistro;
    private JLabel loginHyperlink;
    private JLabel fecha;
    private JLabel confirma;
    private JLabel idLabel;

    public registroView() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel(new GridBagLayout());
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel rolLabel = new JLabel("Rol:");
        comboBoxRol = new JComboBox(new String[]{"Paciente", "Medico", "Farmaceutico", "Admin"});
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(rolLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comboBoxRol, gbc);
        row++;

        nombre = new JLabel("Nombre:");
        campoNombre = new JTextField(12);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(nombre, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoNombre, gbc);
        row++;

        idLabel = new JLabel("Id:");
        campoId = new JTextField(12);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(idLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoId, gbc);
        row++;

        especialidad = new JLabel("Especialidad:");
        campoEspecialidad = new JTextField(12);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(especialidad, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoEspecialidad, gbc);
        row++;

        fecha = new JLabel("Fecha de nacimiento:");
        campoFecha = new JTextField(12);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(fecha, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoFecha, gbc);
        row++;

        contraRegistro = new JLabel("Contraseña:");
        campoContra = new JPasswordField(12);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(contraRegistro, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoContra, gbc);
        row++;

        JLabel confirmaLabel = new JLabel("Confirmar contraseña:");
        campoConfirmaContra = new JPasswordField(12);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(confirmaLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoConfirmaContra, gbc);
        row++;

        registrarseButton = new JButton("Registrarse");
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(registrarseButton, gbc);
        row++;

        loginHyperlink = new JLabel("<html><a href=''>Volver al Login</a></html>");
        loginHyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginHyperlink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                org.example.frontend.View.loginView loginView = new org.example.frontend.View.loginView();
                loginView.setVisible(true);
                dispose();
            }
        });
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginHyperlink, gbc);

        add(panel);

        // Comportamiento dinámico por rol
        comboBoxRol.addActionListener(e -> {
            String rol = comboBoxRol.getSelectedItem().toString();
            if (rol.equals("Paciente")) {
                campoEspecialidad.setEnabled(false);
                campoFecha.setEnabled(true);
            } else if (rol.equals("Medico")) {
                campoEspecialidad.setEnabled(true);
                campoFecha.setEnabled(false);
            } else {
                campoEspecialidad.setEnabled(false);
                campoFecha.setEnabled(false);
            }
        });
        // Inicializar el estado de los campos según el rol por defecto
        String rolInicial = comboBoxRol.getSelectedItem().toString();
        if (rolInicial.equals("Paciente")) {
            campoEspecialidad.setEnabled(false);
            campoFecha.setEnabled(true);
        } else if (rolInicial.equals("Medico")) {
            campoEspecialidad.setEnabled(true);
            campoFecha.setEnabled(false);
        } else {
            campoEspecialidad.setEnabled(false);
            campoFecha.setEnabled(false);
        }

        registrarseButton.addActionListener(e -> {
            String rol = comboBoxRol.getSelectedItem().toString();
            String nombre = campoNombre.getText();
            String id = campoId.getText();
            String especialidad = campoEspecialidad.getText();
            String fecha = campoFecha.getText();
            String password1 = new String(campoContra.getPassword());
            String password2 = new String(campoConfirmaContra.getPassword());

            if (!password1.equals(password2)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nombre.isEmpty() || id.isEmpty() || password1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben estar completos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean registrado = false;
            if (rol.equals("Paciente")) {
                if (fecha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Fecha es obligatoria para paciente", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    java.sql.Date.valueOf(fecha);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "La fecha debe tener formato yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                org.example.common.Paciente paciente = new org.example.common.Paciente(nombre, password1, id, fecha, "1111");
                org.example.backend.dao.PacienteDao pacienteDao = new org.example.backend.dao.PacienteDao();
                registrado = pacienteDao.addPaciente(paciente);
            } else if (rol.equals("Medico")) {
                if (especialidad.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Especialidad es obligatoria para médico", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                org.example.common.Medico medico = new org.example.common.Medico(nombre, password1, id, especialidad);
                org.example.backend.dao.MedicoDao medicoDao = new org.example.backend.dao.MedicoDao();
                registrado = medicoDao.addMedico(medico);
            } else if (rol.equals("Farmaceutico")) {
                org.example.common.Farmaceutico farmaceutico = new org.example.common.Farmaceutico(nombre, password1, id);
                org.example.backend.dao.FarmaceuticoDao farmaceuticoDao = new org.example.backend.dao.FarmaceuticoDao();
                farmaceuticoDao.addFarmaceutico(farmaceutico);
                registrado = true; // Asume éxito, puedes mejorar con manejo de errores si el método retorna boolean
            } else if (rol.equals("Admin")) {
                org.example.common.Admin admin = new org.example.common.Admin(nombre, password1, id);
                org.example.backend.dao.UsersDao dao = new org.example.backend.dao.UsersDao();
                dao.addUser(admin);
                registrado = true;
            }
            if (registrado) {
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Registro", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new org.example.frontend.View.loginView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario. Verifica los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            org.example.frontend.View.registroView registerView = new org.example.frontend.View.registroView();
            registerView.setVisible(true);
        });
    }

    public AbstractButton getRegistrarseButton() {
        return registrarseButton;
    }



    public JComponent getRegistroLink() {
        return registroLink;
    }
    public JTextField getNombreTextfield() {
        return campoNombre;
    }
    public JTextField getIdtextField() {
        return campoId;
    }
    public JTextField getEspecialidadtextField() {
        return campoEspecialidad;
    }
    public JButton getIngresarButton() {
        return ingresarButton;
    }
    public JPasswordField getPasswordField1() {
        return campoContra;
    }
    public JPasswordField getPasswordField2() {
        return campoConfirmaContra;
    }
    public String getComboBoxRol() {
        return comboBoxRol.getSelectedItem().toString();
    }
    public JTextField getFecha() {
        return campoFecha;
    }


    public JTextField getTxtConfirmPassword() {
        // Devolver el campo de confirmación de contraseña para compatibilidad
        return campoConfirmaContra;
    }

    public JComboBox getComboBoxRolComponent() {
        return comboBoxRol;
    }
}

