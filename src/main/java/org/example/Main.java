package org.example;

import org.example.frontend.Control.loginController;
import org.example.backend.dao.UsersDao;
import org.example.frontend.View.loginView;
import org.example.frontend.proxy.BackendProxy;
import org.example.frontend.utils.BackendBootstrapper;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UsersDao usersDao = new UsersDao();
        loginView loginView = new loginView();
        int port = 55555;
        BackendProxy backendProxy = new BackendProxy("localhost", port);
        try {
            backendProxy.connect();
        } catch (Exception e) {
            // try to start local server for dev
            BackendBootstrapper.ensureServer(port);
            try {
                Thread.sleep(500); // brief wait for server to bind
                backendProxy.connect();
            } catch (Exception ignored) {
                // leave disconnected; loginController will show error on login
            }
        }
        new loginController(loginView, usersDao, backendProxy);
        SwingUtilities.invokeLater(() -> loginView.setVisible(true));

    }
}