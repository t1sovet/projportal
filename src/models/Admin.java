package models;

public class Admin extends Employee {
    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

}
