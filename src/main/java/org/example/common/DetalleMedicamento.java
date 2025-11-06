package org.example.common;

import java.io.Serializable;

public class DetalleMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;  // id del detalle (opcional si lo manejas desde la BD)
    private String codigoMedicamento;
    private int cantidad;
    private String indicaciones;
    private int duracionDias;

    // 🔹 Relación con Medicamento (opcional, no se guarda directamente en la tabla)
    private Medicamento medicamento;

    // ===== Constructores =====
    public DetalleMedicamento() {}

    public DetalleMedicamento(String codigoMedicamento, int cantidad, String indicaciones, int duracionDias) {
        this.codigoMedicamento = codigoMedicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }

    // ===== Getters y Setters =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(String codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getNombreMedicamento() {
        return medicamento != null ? medicamento.getNombre() : codigoMedicamento;
    }

    @Override
    public String toString() {
        return "DetalleMedicamento{" +
                "codigoMedicamento='" + codigoMedicamento + '\'' +
                ", cantidad=" + cantidad +
                ", indicaciones='" + indicaciones + '\'' +
                ", duracionDias=" + duracionDias +
                '}';
    }
}