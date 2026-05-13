package models;

public class Employee extends User {
    protected Employee(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "Employee";
    }
}
