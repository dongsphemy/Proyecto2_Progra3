package org.example.frontend.Control;

import org.example.common.*;
import org.example.backend.dao.*;
import org.example.common.wrappers.userWrapper;
import org.example.frontend.View.*;
import org.example.frontend.proxy.BackendProxy;
import org.example.frontend.service.RemotePrescripcionService;
import org.example.frontend.service.RemoteRecetaService;

import javax.swing.*;

public class loginController {

    private loginView view;
    private UsersDao usersDao;
    private final BackendProxy backendProxy;

    public loginController(loginView view, UsersDao usersDao, BackendProxy backendProxy) {
        this.view = view;
        this.usersDao = usersDao;
        this.backendProxy = backendProxy;
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

        // Crear el controlador del registro con el proxy
        new registroController(
                registerView,
                usersDao,
                pacienteDao,
                medicoDao,
                farmaceuticoDao,
                backendProxy
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
                // Conectar al backend y enviar LOGIN via proxy
                try {
                    if (!backendProxy.isConnected()) backendProxy.connect();
                    org.example.common.wrappers.userWrapper uw = new org.example.common.wrappers.userWrapper(user.getId(), user.getName(), user.getRole());
                    backendProxy.login(uw);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "No se pudo conectar al backend: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(view, "¡Inicio de sesión exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();

                // Servicios remotos via proxy
                RemotePrescripcionService remotePresc = new RemotePrescripcionService(backendProxy);
                RemoteRecetaService remoteReceta = new RemoteRecetaService(backendProxy);

                // Abrir la ventana principal según el rol
                switch (user.getRole().toLowerCase()) {
                    case "admin":
                        adminView adminPanel = new adminView();
                        new adminViewController(
                                medicoDao,
                                farmaceuticoDao,
                                adminPanel.getFarmaceuticoPanel(),
                                adminPanel.getMedicoPanel(),
                                adminPanel.getPacientePanel(),
                                pacienteDao,
                                recetaDao,
                                adminPanel.getRecetaView(),
                                medicamentoDao,
                                adminPanel.getMedicamentoView()
                        );
                        adminPanel.setVisible(true);
                        break;
                    case "medico":
                        medicoPanel medicoPanel = new medicoPanel();
                        new medicoViewController_Remote(medicoPanel.getPrescribir(), remotePresc, remoteReceta, username);
                        new recetaController_Remote(medicoPanel.getRecetaView(), remoteReceta);
                        medicoPanel.setVisible(true);
                        break;
                    case "paciente":
                        JOptionPane.showMessageDialog(view,
                            "Bienvenido, " + user.getName(),
                            "Bienvenida",
                            JOptionPane.INFORMATION_MESSAGE);
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
