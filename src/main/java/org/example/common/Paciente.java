package org.example.common;

import java.io.Serializable;

public class Paciente extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fechaNacimiento;
    private String telefono;

    // 🔹 Constructor vacío (requerido para serialización y frameworks)
    public Paciente() {
        super();
        this.setRole("Paciente");
    }

    // 🔹 Constructor completo
    public Paciente(String name, String password, String id, String fechaNacimiento, String telefono) {
        super(name, password, "Paciente", id);
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    // === Getters y Setters ===
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

    // === Método toString para depuración ===
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