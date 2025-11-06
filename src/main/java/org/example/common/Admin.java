package org.example.common;

public class Admin extends AbstractUser{
    public Admin(String name, String password, String id) {
        super(name, password, "Admin", id);
    }
    public Admin() {
        super();
    }
}