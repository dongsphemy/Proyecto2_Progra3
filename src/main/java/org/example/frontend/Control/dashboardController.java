package org.example.frontend.Control;

import org.example.frontend.View.dashboardView;
import org.example.backend.dao.MedicamentoDao;
import org.example.backend.dao.RecetaDao;
import org.example.common.Medicamento;
import org.example.common.Receta;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class dashboardController {
    private dashboardView view;
    private MedicamentoDao medicamentoDao;
    private RecetaDao recetaDao;

    public dashboardController(dashboardView view) {
        this.view = view;
        this.medicamentoDao = new MedicamentoDao();
        this.recetaDao = new RecetaDao();
        cargarMedicamentos();
        cargarMeses();
        view.getConfirmarButton().addActionListener(new ConfirmarListener());
        actualizarGraficas();
    }

    private void cargarMedicamentos() {
        JComboBox<String> combo = view.getComboMedicamento();
        combo.removeAllItems();
        for (Medicamento m : medicamentoDao.getAllMedicamentos()) {
            combo.addItem(m.getCodigo() + " - " + m.getNombre());
        }
        // opción para "Todos"
        combo.insertItemAt("Todos", 0);
        combo.setSelectedIndex(0);
    }

    private void cargarMeses() {
        JComboBox<String> comboInicio = view.getComboMesInicio();
        JComboBox<String> comboFin = view.getComboMesFin();
        comboInicio.removeAllItems();
        comboFin.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            String mes = LocalDate.of(2000, i, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            comboInicio.addItem(mes);
            comboFin.addItem(mes);
        }
        comboInicio.setSelectedIndex(0);
        comboFin.setSelectedIndex(11);
    }

    private void actualizarGraficas() {
        String medicamentoCodigo = null;
        int mesInicio = 1;
        int mesFin = 12;

        String selectedMedicamento = (String) view.getComboMedicamento().getSelectedItem();
        if (selectedMedicamento != null && !selectedMedicamento.equals("Todos") && selectedMedicamento.contains(" - ")) {
            medicamentoCodigo = selectedMedicamento.split(" - ")[0];
        }
        String mesInicioStr = (String) view.getComboMesInicio().getSelectedItem();
        String mesFinStr = (String) view.getComboMesFin().getSelectedItem();
        if (mesInicioStr != null && mesFinStr != null) {
            mesInicio = mesNombreToNumero(mesInicioStr);
            mesFin = mesNombreToNumero(mesFinStr);
            if (mesFin < mesInicio) { // normalizar rango
                int tmp = mesInicio; mesInicio = mesFin; mesFin = tmp;
            }
        }

        // Recargar recetas desde BD
        List<Receta> recetas = Arrays.asList(recetaDao.getAll());

        // Line chart acumulado: recetas (del medicamento o todas) hasta cada mes
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        long acumulado = 0;
        for (int m = mesInicio; m <= mesFin; m++) {
            final int mesLoop = m;
            final String codigoFiltro = medicamentoCodigo;
            long mesCount = recetas.stream()
                    .filter(r -> r.getFechaConfeccion() != null && r.getFechaConfeccion().getMonthValue() == mesLoop)
                    .filter(r -> codigoFiltro == null || r.getMedicamentos().stream()
                            .anyMatch(dm -> dm.getCodigoMedicamento() != null && dm.getCodigoMedicamento().equalsIgnoreCase(codigoFiltro)))
                    .count();
            acumulado += mesCount;
            String mesNombre = LocalDate.of(2000, mesLoop, 1).getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            lineDataset.addValue(acumulado, "Prescripciones (acumulado)", mesNombre);
        }
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Cantidad acumulada de recetas",
                "Mes",
                "Cantidad",
                lineDataset
        );
        view.getLineChartPanel().removeAll();
        view.getLineChartPanel().add(new ChartPanel(lineChart), BorderLayout.CENTER);
        view.getLineChartPanel().revalidate();
        view.getLineChartPanel().repaint();

        // Pie chart: recetas por estado (del filtro o todas)
        final String medicamentoFiltro = medicamentoCodigo;
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
        Map<String, Long> estadoCounts = recetas.stream()
                .filter(r -> medicamentoFiltro == null || r.getMedicamentos().stream()
                        .anyMatch(dm -> dm.getCodigoMedicamento() != null && dm.getCodigoMedicamento().equalsIgnoreCase(medicamentoFiltro)))
                .collect(Collectors.groupingBy(Receta::getEstado, Collectors.counting()));
        for (Map.Entry<String, Long> entry : estadoCounts.entrySet()) {
            pieDataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Recetas por estado",
                pieDataset,
                true,
                true,
                false
        );
        view.getPieChartPanel().removeAll();
        view.getPieChartPanel().add(new ChartPanel(pieChart), BorderLayout.CENTER);
        view.getPieChartPanel().revalidate();
        view.getPieChartPanel().repaint();
    }

    private int mesNombreToNumero(String mesNombre) {
        for (int i = 1; i <= 12; i++) {
            String nombre = LocalDate.of(2000, i, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            if (nombre.equalsIgnoreCase(mesNombre)) return i;
        }
        return 1;
    }

    private class ConfirmarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            actualizarGraficas();
        }
    }
}