package org.example.frontend.View;

import javax.swing.*;

public class farmaceutaView extends JFrame {
    private despachoView despachoView;
    private recetaView recetaView;

    public farmaceutaView() {
        setTitle("Panel Farmacéutico");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        JTabbedPane tabbedPane = new JTabbedPane();


        despachoView = new despachoView();
        recetaView = new recetaView();
        tabbedPane.addTab("Despacho", despachoView);
        tabbedPane.addTab("Historial de Recetas",recetaView );

        dashboardView dashboardView = new dashboardView();
        new org.example.frontend.Control.dashboardController(dashboardView);
        tabbedPane.addTab("Indicadores", dashboardView);


        add(tabbedPane);
        setVisible(true);
    }

    public despachoView getDespachoPanel() {
        return despachoView;
    }
    public recetaView getRecetaView() {
        return recetaView;
    }
}