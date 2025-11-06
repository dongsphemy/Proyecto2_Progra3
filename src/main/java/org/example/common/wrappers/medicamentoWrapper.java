package org.example.common.wrappers;

import org.example.common.Medicamento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class medicamentoWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private String presentacion;

    // colección de medicamentos para loadMedicamentos()
    private List<Medicamento> medicamentos = new ArrayList<>();

    public medicamentoWrapper() {}

    public medicamentoWrapper(String codigo, String nombre, String presentacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    // collection accessors
    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<Medicamento> medicamentos) { this.medicamentos = medicamentos; }
    public void addMedicamento(Medicamento m) { this.medicamentos.add(m); }

    @Override
    public String toString() {
        return "medicamentoWrapper{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", presentacion='" + presentacion + '\'' +
                '}';
    }
}
