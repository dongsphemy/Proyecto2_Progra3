package org.example.frontend.View;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard panel for displaying medication and prescription statistics.
 * Includes combo boxes for medication and month selection, and chart panels.
 */
public class dashboardView extends JPanel {
    // Combo boxes for user selection
    private final JComboBox<String> comboMedicamento;
    private final JComboBox<String> comboMesInicio;
    private final JComboBox<String> comboMesFin;
    // Button to confirm selection
    private final JButton confirmarButton;
    // Panels for charts
    private final JPanel lineChartPanel;
    private final JPanel pieChartPanel;

    public dashboardView() {
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout());
        comboMedicamento = new JComboBox<>();
        comboMesInicio = new JComboBox<>();
        comboMesFin = new JComboBox<>();
        confirmarButton = new JButton("Confirmar");
        topPanel.add(new JLabel("Medicamento:"));
        topPanel.add(comboMedicamento);
        topPanel.add(new JLabel("Mes inicio:"));
        topPanel.add(comboMesInicio);
        topPanel.add(new JLabel("Mes fin:"));
        topPanel.add(comboMesFin);
        topPanel.add(confirmarButton);
        add(topPanel, BorderLayout.NORTH);
        lineChartPanel = new JPanel(new BorderLayout());
        pieChartPanel = new JPanel(new BorderLayout());
        JPanel chartsPanel = new JPanel(new GridLayout(1,2));
        chartsPanel.add(lineChartPanel);
        chartsPanel.add(pieChartPanel);
        add(chartsPanel, BorderLayout.CENTER);
    }

    // Getters for controller access
    public JComboBox<String> getComboMedicamento() { return comboMedicamento; }
    public JComboBox<String> getComboMesInicio() { return comboMesInicio; }
    public JComboBox<String> getComboMesFin() { return comboMesFin; }
    public JButton getConfirmarButton() { return confirmarButton; }
    public JPanel getLineChartPanel() { return lineChartPanel; }
    public JPanel getPieChartPanel() { return pieChartPanel; }
}