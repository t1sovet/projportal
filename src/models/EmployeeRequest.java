package models;

import java.io.Serializable;
import enums.RequestStatus;

public class EmployeeRequest implements Serializable {
    private final String employeeId;
    private final String content;
    private RequestStatus status = RequestStatus.PENDING;

    public EmployeeRequest(String employeeId, String content) {
        this.employeeId = employeeId;
        this.content = content;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getContent() {
        return content;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Request from %s: %s [Status: %s]",
                employeeId, content, status);
    }
}
