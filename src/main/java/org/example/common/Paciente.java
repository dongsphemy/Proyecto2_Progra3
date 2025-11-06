package org.example.common;

import java.io.Serializable;

public class Paciente extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fechaNacimiento;
    private String telefono;

    // constructor vacio
    public Paciente() {
        super();
        this.setRole("Paciente");
    }

    // constructor con parametros
    public Paciente(String name, String password, String id, String fechaNacimiento, String telefono) {
        super(name, password, "Paciente", id);
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    // getters y setters
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // metodo toString
    @Override
    public String toString() {
        return "Paciente{" +
                "nombre='" + getName() + '\'' +
                ", id='" + getId() + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}