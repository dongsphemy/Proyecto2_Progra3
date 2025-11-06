package org.example.common.wrappers;

import org.example.common.Paciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class pacienteWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String fechaNacimiento;
    private String telefono;

    // collection of pacientes for loadPacientes()
    private List<Paciente> pacientes = new ArrayList<>();

    public pacienteWrapper() {}

    public pacienteWrapper(String id, String name, String fechaNacimiento, String telefono) {
        this.id = id;
        this.name = name;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // collection accessors
    public List<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }
    public void addPaciente(Paciente p) { this.pacientes.add(p); }

    @Override
    public String toString() {
        return "pacienteWrapper{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
