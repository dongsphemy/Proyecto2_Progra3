package org.example.frontend.View;

import javax.swing.*;
import java.awt.*;

public class despachoView extends JPanel {
    private JComboBox<String> comboRecetas;
    private JButton btnIniciar;
    private JButton btnAlistar;
    private JButton btnEntregar;
    private JTextArea areaEstado;

    public despachoView() {
        setLayout(new BorderLayout());

        // Parte superior: comboBox de recetas
        JPanel panelInput = new JPanel();
        panelInput.add(new JLabel("Seleccione receta:"));
        comboRecetas = new JComboBox<>();
        comboRecetas.setPreferredSize(new Dimension(200, 25));
        panelInput.add(comboRecetas);
        add(panelInput, BorderLayout.NORTH);

        // Parte central: Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
        btnIniciar = new JButton("Iniciar despacho");
        btnAlistar = new JButton("Alistar");
        btnEntregar = new JButton("Entregar");
        panelBotones.add(btnIniciar);
        panelBotones.add(btnAlistar);
        panelBotones.add(btnEntregar);
        add(panelBotones, BorderLayout.CENTER);

        // Parte inferior: área de estado
        areaEstado = new JTextArea(5, 35);
        areaEstado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaEstado);
        add(scroll, BorderLayout.SOUTH);
    }

    public JComboBox<String> getComboRecetas() {
        return comboRecetas;
    }

    public JButton getBtnIniciar() {
        return btnIniciar;
    }

    public JButton getBtnAlistar() {
        return btnAlistar;
    }

    public JButton getBtnEntregar() {
        return btnEntregar;
    }

    public JTextArea getAreaEstado() {
        return areaEstado;
    }
}