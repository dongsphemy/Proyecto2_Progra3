package org.example.frontend.Control;

import org.example.common.*;
import org.example.backend.dao.*;
import org.example.frontend.View.loginView;
import org.example.frontend.View.registroView;

import javax.swing.*;

public class registroController {

    private registroView view;
    private UsersDao usersDao;
    private PacienteDao pacienteDao;
    private MedicoDao medicoDao;
    private FarmaceuticoDao farmaceuticoDao;

    public registroController(registroView view,
                              UsersDao usersDao,
                              PacienteDao pacienteDao,
                              MedicoDao medicoDao,
                              FarmaceuticoDao farmaceuticoDao) {
        this.view = view;
        this.usersDao = usersDao;
        this.pacienteDao = pacienteDao;
        this.medicoDao = medicoDao;
        this.farmaceuticoDao = farmaceuticoDao;

        initController();
    }

    private void initController() {
        view.getRegistrarseButton().addActionListener(_ -> register());

        // Listener para el cambio de rol
        view.getComboBoxRolComponent().addActionListener(e -> {
            String rol = view.getComboBoxRol();

            switch (rol) {
                case "Paciente" -> {
                    view.getEspecialidadtextField().setEnabled(false);
                    view.getFecha().setEnabled(true);
                    view.getNombreTextfield().setEnabled(true);
                    view.getIdtextField().setEnabled(true);
                    view.getPasswordField1().setEnabled(true);
                    view.getPasswordField2().setEnabled(true);
                }
                case "Medico" -> {
                    view.getEspecialidadtextField().setEnabled(true);
                    view.getFecha().setEnabled(false);
                    view.getNombreTextfield().setEnabled(true);
                    view.getIdtextField().setEnabled(true);
                    view.getPasswordField1().setEnabled(true);
                    view.getPasswordField2().setEnabled(true);
                }
                case "Admin", "Farmaceutico" -> {
                    view.getEspecialidadtextField().setEnabled(false);
                    view.getFecha().setEnabled(false);
                    view.getNombreTextfield().setEnabled(true);
                    view.getIdtextField().setEnabled(true);
                    view.getPasswordField1().setEnabled(true);
                    view.getPasswordField2().setEnabled(true);
                }
            }
        });
    }

    private void register() {
        String username = view.getNombreTextfield().getText();
        String id = view.getIdtextField().getText();
        String especialidad = view.getEspecialidadtextField().getText();
        String password1 = new String(view.getPasswordField1().getPassword());
        String password2 = new String(view.getPasswordField2().getPassword());
        String role = view.getComboBoxRol();
        String fecha = view.getFecha().getText();

        if (!password1.equals(password2)) {
            JOptionPane.showMessageDialog(view, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar duplicado en UsersDao
        AbstractUser existingUser = usersDao.searchUserById(usersDao.loadUsers(), id);
        if (existingUser != null) {
            JOptionPane.showMessageDialog(view, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            AbstractUser newUser;

            switch (role) {
                case "Admin" -> {
                    newUser = new Admin(username, password1, id);
                }
                case "Medico" -> {
                    newUser = new Medico(username, password1, id, especialidad);
                    medicoDao.addMedico((Medico) newUser);
                }
                case "Paciente" -> {
                    newUser = new Paciente(username, password1, id, fecha, "1111");
                    pacienteDao.addPaciente((Paciente) newUser);
                }
                case "Farmaceutico" -> {
                    newUser = new Farmaceutico(username, password1, id);
                    farmaceuticoDao.addFarmaceutico((Farmaceutico) newUser);
                }
                default -> throw new IllegalStateException("Rol inesperado: " + role);
            }

            // Guardar en UsersDao para login
            usersDao.addUser(newUser);

            JOptionPane.showMessageDialog(view, "¡Registro exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
            loginView loginView = new loginView();
            new loginController(loginView, usersDao);
            loginView.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
