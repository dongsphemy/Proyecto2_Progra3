package org.example.common;
import java.io.Serializable;

public abstract class AbstractUser implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String password;
    protected String role;

    public AbstractUser() {}
    public AbstractUser(String name, String password, String role, String id) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}