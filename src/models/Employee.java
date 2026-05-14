package models;

import enums.UserType;

public abstract class Employee extends User {
    protected Employee(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public UserType getRole() {
        return UserType.EMPLOYEE;
    }
}
