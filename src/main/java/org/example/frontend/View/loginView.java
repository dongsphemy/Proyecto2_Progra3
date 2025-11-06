package org.example.frontend.View;

import javax.swing.*;
import java.awt.*;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Usuario label y campo
        nombre = new JLabel("Usuario:");
        textField1 = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(nombre, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(textField1, gbc);
        row++;

        // Contraseña label y campo
        contra = new JLabel("Contraseña:");
        passwordField1 = new JPasswordField(15);
        passwordField1.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(contra, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField1, gbc);
        row++;

        // Botón Ingresar
        ingresarButton = new JButton("Ingresar");
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(ingresarButton, gbc);
        row++;

        // Link Registrarse
        registroLink = new JLabel("<html><a href=''>Registrarse</a></html>");
        registroLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registroLink, gbc);
        row++;

        // Link Recuperar contraseña
        recuperaPassword = new JLabel("<html><a href=''>¿Olvidaste tu contraseña?</a></html>");
        recuperaPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(recuperaPassword, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(null);

        // Listeners removidos: serán configurados desde el controlador (loginController)
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