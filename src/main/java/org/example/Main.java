package org.example;

import org.example.frontend.Control.loginController;
import org.example.frontend.Control.medicoController;
import org.example.common.AbstractUser;
import org.example.backend.dao.FarmaceuticoDao;
import org.example.backend.dao.MedicoDao;
import org.example.backend.dao.PacienteDao;
import org.example.backend.dao.UsersDao;
import org.example.common.wrappers.userWrapper;
import org.example.frontend.View.loginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Crear DAOs
        UsersDao usersDao = new UsersDao();
        PacienteDao pacienteDao = new PacienteDao();
        MedicoDao medicoDao = new MedicoDao();
        FarmaceuticoDao farmaceuticoDao = new FarmaceuticoDao();

        // Crear la vista de login
        loginView loginView = new loginView();

        // Crear el controlador de login
        loginController loginController = new loginController(loginView, usersDao);

        // Mostrar la ventana de login
        SwingUtilities.invokeLater(() -> loginView.setVisible(true));

        // new medicoController(); // Eliminar esta línea, solo debe instanciarse cuando el usuario médico inicia sesión
    }
}