package org.example.frontend.View;

import javax.swing.*;

public class medicoPanel extends JFrame{
    prescribirView prescribirView;
    recetaView recetaView;
    public medicoPanel() {
        setTitle("Administración Hospitalaria");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        this.prescribirView = new prescribirView();
        this.recetaView = new recetaView();
        dashboardView dashboardView = new dashboardView();
        new org.example.frontend.Control.dashboardController(dashboardView);

        tabbedPane.addTab("Médicos", prescribirView);
        tabbedPane.addTab("Historial de Recetas", recetaView);
        tabbedPane.addTab("Indicadores", dashboardView);

        add(tabbedPane);
        setVisible(true);
    }
    public prescribirView getPrescribir() {
        return this.prescribirView;
    }

    public recetaView getRecetaView() {
        return this.recetaView;
    }
}