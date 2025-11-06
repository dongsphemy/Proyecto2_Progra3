package org.example.common.wrappers;

import org.example.common.Receta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class recetaWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idReceta;
    private medicoWrapper medico;
    private pacienteWrapper paciente;
    private String fechaConfeccion;
    private String fechaRetiro;
    private String estado;
    private List<medicamentoWrapper> medicamentos = new ArrayList<>();

    private List<Receta> recetas = new ArrayList<>();

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public recetaWrapper() {}

    public String getIdReceta() { return idReceta; }
    public void setIdReceta(String idReceta) { this.idReceta = idReceta; }

    public medicoWrapper getMedico() { return medico; }
    public void setMedico(medicoWrapper medico) { this.medico = medico; }

    public pacienteWrapper getPaciente() { return paciente; }
    public void setPaciente(pacienteWrapper paciente) { this.paciente = paciente; }

    public String getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(String fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<medicamentoWrapper> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<medicamentoWrapper> medicamentos) { this.medicamentos = medicamentos; }

    public void addMedicamento(medicamentoWrapper m) { this.medicamentos.add(m); }
}
