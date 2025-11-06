package org.example.common;

import java.io.Serializable;

public class Farmaceutico extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    // constructor vacio
    public Farmaceutico() {
        super();
        this.setRole("Farmaceutico"); // asegúrate que coincida con ENUM de la BD
    }

    // const
    public Farmaceutico(String name, String password, String id) {
        super(name, password, "Farmaceutico", id);
    }

    @Override
    public String toString() {
        return "Farmaceutico{" +
                "nombre='" + getName() + '\'' +
                ", id='" + getId() + '\'' +
                ", rol='" + getRole() + '\'' +
                '}';
    }
}