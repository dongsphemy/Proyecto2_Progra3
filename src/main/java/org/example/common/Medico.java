package org.example.common;

import java.io.Serializable;

public class Medico extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String especialidad;

    // 🔹 Constructor vacío (requerido para serialización y frameworks)
    public Medico() {
        super();
        this.setRole("Medico"); // asegura que siempre tenga el rol correcto
    }

    // 🔹 Constructor completo
    public Medico(String name, String password, String id, String especialidad) {
        super(name, password, "Medico", id);
        this.especialidad = especialidad;
    }

    // === Getters y Setters ===
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}