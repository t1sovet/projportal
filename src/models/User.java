package models;

import java.io.Serializable;

import enums.UserType;
import interfaces.Printable;

public abstract class User implements Serializable, Printable {
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public abstract UserType getRole();

    @Override
    public String print() {
        return String.format("User{id='%s', name='%s', email='%s'}", id, name, email);
    }

}
