package org.example.common.wrappers;

import org.example.common.Medico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class medicoWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String especialidad;

    // colección de medicos para loadMedicos()
    private List<Medico> medicos = new ArrayList<>();

    public medicoWrapper() {}

    public medicoWrapper(String id, String name, String especialidad) {
        this.id = id;
        this.name = name;
        this.especialidad = especialidad;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    // collection accessors
    public List<Medico> getMedicos() { return medicos; }
    public void setMedicos(List<Medico> medicos) { this.medicos = medicos; }
    public void addMedico(Medico m) { this.medicos.add(m); }

    @Override
    public String toString() {
        return "medicoWrapper{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
}
