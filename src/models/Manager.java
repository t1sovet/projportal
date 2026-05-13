package models;

import enums.ManagerType;

public class Manager extends Employee {
    private final ManagerType managerType;

    public Manager(String id, String name, String email, String password, ManagerType managerType) {
        super(id, name, email, password);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    @Override
    public String getRole() {
        return "Manager";
    }
}
