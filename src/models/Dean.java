package models;

import enums.UserType;
import enums.RequestStatus;

public class Dean extends Employee {
    public Dean(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public UserType getRole() {
        return UserType.DEAN;
    }

    public void signRequest(EmployeeRequest request) {
        request.setStatus(RequestStatus.APPROVED);
    }
}
