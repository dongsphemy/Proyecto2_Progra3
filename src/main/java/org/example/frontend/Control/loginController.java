package org.example.frontend.Control;

import org.example.common.*;
import org.example.backend.dao.*;
import org.example.common.wrappers.userWrapper;
import org.example.frontend.View.*;

import javax.swing.*;

public class loginController {

    private loginView view;
    private UsersDao usersDao;

    public loginController(loginView view, UsersDao usersDao) {
        this.view = view;
        this.usersDao = usersDao;
        initController();
    }

    private void initController() {
        view.getBtnLogin().addActionListener(_ -> login());
        view.getRegistroLink().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openRegisterView();
            }
        });
    }

    private void openRegisterView() {
        registroView registerView = new registroView();

        // Instanciar DAOs específicos para el registro
        PacienteDao pacienteDao = new PacienteDao();
        MedicoDao medicoDao = new MedicoDao();
        FarmaceuticoDao farmaceuticoDao = new FarmaceuticoDao();

        // Crear el controlador del registro
        new registroController(
                registerView,
                usersDao,
                pacienteDao,
                medicoDao,
                farmaceuticoDao
        );

        registerView.setVisible(true);
        view.dispose();
    }

    private void login() {
        String username = view.getTxtName().getText();
        String password = new String(view.getTxtPassword().getPassword());
        PacienteDao pacienteDao = new PacienteDao();
        MedicoDao medicoDao = new MedicoDao();
        FarmaceuticoDao farmaceuticoDao = new FarmaceuticoDao();
        MedicamentoDao medicamentoDao = new MedicamentoDao();
        RecetaDao recetaDao = new RecetaDao();

        try {
            // Cargar todos los usuarios desde UsersDao
            userWrapper wrapper = usersDao.loadUsers();
            AbstractUser user = usersDao.searchUserById(wrapper, username);

            if (user != null && user.validatePassword(password)) {
                JOptionPane.showMessageDialog(view, "¡Inicio de sesión exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();

                // Abrir la ventana principal según el rol
                switch (user.getRole().toLowerCase()) {
                    case "admin":
                        adminView adminPanel = new adminView();
                        new adminViewController(
                                medicoDao,                          // 1️ MedicoDao
                                farmaceuticoDao,                    // 2️ FarmaceuticoDao
                                adminPanel.getFarmaceuticoPanel(),  // 3️ FarmaceuticoView
                                adminPanel.getMedicoPanel(),        // 4️ MedicoView
                                adminPanel.getPacientePanel(),      // 5️ PacienteView
                                pacienteDao,                         // 6 PacienteDao
                                recetaDao,                          // 7️ RecetaDao
                                adminPanel.getRecetaView(),         // 8️ RecetaView
                                medicamentoDao,                     // 9️ MedicamentoDao
                                adminPanel.getMedicamentoView()     // 10️ MedicamentoView
                        );
                        adminPanel.setVisible(true);
                        System.out.println("Abrir vista de admin");
                        break;
                    case "medico":
                        System.out.println("Abrir vista de médico");
                        medicoPanel medicoPanel = new medicoPanel();
                        org.example.backend.service.PrescripcionService prescripcionController = new org.example.backend.service.PrescripcionService(pacienteDao, medicoDao, new RecetaDao(), medicamentoDao);
                        // instantiate controller without assignment to avoid unused-variable warning
                        new medicoViewController(medicoPanel.getPrescribir(), prescripcionController, recetaDao, medicoPanel.getRecetaView(), username);
                        medicoPanel.setVisible(true);
                        break;
                    case "paciente":
                        System.out.println("Abrir vista de paciente");
                        break;
                    case "farmaceuta":
                        System.out.println("Abrir vista de farmacéutico");
                        farmaceutaView farmaceutaPanel = new farmaceutaView();
                        recetaView recetaView = farmaceutaPanel.getRecetaView();
                        org.example.backend.service.DespachoService despachoService = new org.example.backend.service.DespachoService(recetaDao);
                        new farmaceutaViewController(farmaceutaPanel, despachoService, recetaDao, recetaView);
                        farmaceutaPanel.setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(view, "Rol de usuario desconocido.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(view, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al cargar usuarios", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
