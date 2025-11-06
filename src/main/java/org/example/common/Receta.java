package org.example.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idReceta;
    private Medico medico;
    private Paciente paciente;
    private LocalDate fechaConfeccion;
    private LocalDate fechaRetiro;
    private String estado; // "en_proceso", "confeccionada", "entregada"
    private List<DetalleMedicamento> medicamentos = new ArrayList<>();

    //  constructores
    public Receta() {
        this.fechaConfeccion = LocalDate.now();
        this.estado = "en_proceso";
    }

    public Receta(String idReceta, Medico medico, Paciente paciente) {
        this.idReceta = idReceta;
        this.medico = medico;
        this.paciente = paciente;
        this.fechaConfeccion = LocalDate.now();
        this.estado = "en_proceso";
    }

    // getters y setters
    public String getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFechaConfeccion() {
        return fechaConfeccion;
    }

    public void setFechaConfeccion(LocalDate fechaConfeccion) {
        this.fechaConfeccion = fechaConfeccion;
    }

    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleMedicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<DetalleMedicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    // métodos para agregar y eliminar medicamentos
    public void agregarMedicamento(DetalleMedicamento detalle) {
        medicamentos.add(detalle);
    }

    public void eliminarMedicamento(String codigoMedicamento) {
        Iterator<DetalleMedicamento> it = medicamentos.iterator();
        while (it.hasNext()) {
            if (it.next().getCodigoMedicamento().equalsIgnoreCase(codigoMedicamento)) {
                it.remove();
                break;
            }
        }
    }

    public void registrar(LocalDate fechaRetiro) {
        this.estado = "confeccionada";
        this.fechaRetiro = fechaRetiro;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "idReceta='" + idReceta + '\'' +
                ", medico=" + (medico != null ? medico.getName() : "null") +
                ", paciente=" + (paciente != null ? paciente.getName() : "null") +
                ", fechaConfeccion=" + fechaConfeccion +
                ", fechaRetiro=" + fechaRetiro +
                ", estado='" + estado + '\'' +
                ", medicamentos=" + medicamentos.size() +
                '}';
    }
}