package org.example.common;

import java.io.Serializable;

public class Medico extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String especialidad;

    //constructor vacio
    public Medico() {
        super();
        this.setRole("Medico");
    }

    // constructor con parametros
    public Medico(String name, String password, String id, String especialidad) {
        super(name, password, "Medico", id);
        this.especialidad = especialidad;
    }

    //getters y setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}