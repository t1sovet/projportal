package models;

import enums.ManagerType;
import interfaces.ReportStrategy;
import enums.UserType;

public class Manager extends Employee {
    private final ManagerType managerType;
    private ReportStrategy reportStrategy;

    public Manager(String id, String name, String email, String password, ManagerType managerType) {
        super(id, name, email, password);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void setReportStrategy(ReportStrategy strategy) {
        this.reportStrategy = strategy;
    }

    public void performReport(Course course) {
        if (reportStrategy == null) {
            System.out.println("No report strategy selected!");
            return;
        }
        reportStrategy.generateReport(course);
    }

    @Override
    public UserType getRole() {
        return UserType.MANAGER;
    }
}
