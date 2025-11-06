package org.example.common.wrappers;

import org.example.common.AbstractUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class userWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String role;

    // collection of users for loadUsers()
    private List<AbstractUser> users = new ArrayList<>();

    public userWrapper() {}

    public userWrapper(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // collection accessors
    public List<AbstractUser> getUsers() { return users; }
    public void setUsers(List<AbstractUser> users) { this.users = users; }
    public void addUser(AbstractUser u) { this.users.add(u); }

    @Override
    public String toString() {
        return "userWrapper{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
