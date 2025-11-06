package org.example.frontend.View;

import javax.swing.*;

public class loginView extends JFrame {
    private JPanel panel;
    private JLabel nombre;
    private JTextField textField1;
    private JButton ingresarButton;
    private JPasswordField passwordField1;
    private JLabel registroLink;
    private JLabel recuperaPassword;
    private JLabel contra;

    public loginView() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel();
        nombre = new JLabel("Usuario:");
        textField1 = new JTextField(12);
        ingresarButton = new JButton("Ingresar");
        passwordField1 = new JPasswordField(12);
        registroLink = new JLabel();
        recuperaPassword = new JLabel();
        contra = new JLabel("Contraseña:");

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(nombre);
        panel.add(textField1);
        panel.add(contra);
        panel.add(passwordField1);
        panel.add(ingresarButton);
        panel.add(registroLink);
        panel.add(recuperaPassword);

        // Agregado para el margen
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panel);
        passwordField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));


        // Hacer que el registroLink se vea como hipervínculo
        registroLink.setText("<html><a href=''>Registrarse</a></html>");
        registroLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        recuperaPassword.setText("<html><a href=''>¿Olvidaste tu contraseña?</a></html>");
        recuperaPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        registroLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                registroView registroView = new registroView();
                registroView.setVisible(true);
            }
        });

        ingresarButton.addActionListener(e -> {
            String username = textField1.getText();
            String password = new String(passwordField1.getPassword());
            org.example.backend.dao.UsersDao dao = new org.example.backend.dao.UsersDao();
            org.example.common.AbstractUser user = dao.searchUserById(username);
            if (user != null && user.getPassword().equals(password)) {
                String rol = user.getRole();
                dispose();
                switch (rol.toLowerCase()) {
                    case "paciente":
                        JOptionPane.showMessageDialog(null, "Bienvenido Paciente");
                        break;
                    case "medico":
                        new medicoPanel().setVisible(true);
                        break;
                    case "farmaceutico":
                        new farmaceutaView().setVisible(true);
                        break;
                    case "admin":
                        new adminView().setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Rol desconocido");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de login", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loginView loginView = new loginView();
            // loginView.setVisible(true); // Eliminada para que solo Main.java controle la visibilidad
        });
    }
    public JTextField getTxtName() { return textField1; }
    public JPasswordField getTxtPassword() { return passwordField1; }
    public JButton getBtnLogin() { return ingresarButton; }
    public JLabel getRegistroLink() { return registroLink; }
    public JLabel getRecuperaPassword() { return recuperaPassword; }

}