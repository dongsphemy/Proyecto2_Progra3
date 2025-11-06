package org.example.frontend.View;

import javax.swing.*;

public class adminView extends JFrame {
    MedicoView medicoView;
    farmaceuticoView farmaceuticoView;
    pacienteView pacienteView;
    recetaView recetaView;
    medicamentoView medicamentoView;

    public adminView() {
        setTitle("Administración Hospitalaria");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane();

        medicoView = new MedicoView();
        farmaceuticoView = new farmaceuticoView();
        pacienteView = new pacienteView();
        recetaView = new recetaView();
        medicamentoView = new medicamentoView();
        dashboardView dashboardView = new dashboardView();
        new org.example.frontend.Control.dashboardController(dashboardView);

        tabbedPane.addTab("Farmacéuticos", farmaceuticoView);
        tabbedPane.addTab("Médicos", medicoView);
        tabbedPane.addTab("Pacientes",pacienteView);
        tabbedPane.addTab("Historial de Recetas",recetaView);
        tabbedPane.addTab("Medicamentos",medicamentoView);
        tabbedPane.addTab("Indicadores", dashboardView);

        add(tabbedPane);
        setVisible(true);
    }

    public farmaceuticoView getFarmaceuticoPanel() {
        return this.farmaceuticoView;
    }

    public MedicoView getMedicoPanel() {
        return this.medicoView;
    }

    public pacienteView getPacientePanel() {
        return this.pacienteView;
    }

    public recetaView getRecetaView() {
        return this.recetaView;
    }

    public medicamentoView getMedicamentoView() {
        return this.medicamentoView;
    }


}