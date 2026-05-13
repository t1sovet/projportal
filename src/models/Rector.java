package models;

import enums.UserType;
import enums.RequestStatus;

public class Rector extends Employee {
    public Rector(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public UserType getRole() {
        return UserType.RECTOR;
    }

    public void signRequest(EmployeeRequest request) {
        request.setStatus(RequestStatus.APPROVED);
    }
}
