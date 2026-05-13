package models;

import java.io.Serializable;
import enums.RequestStatus;

public class RegistrationRequest implements Serializable {
    private final Student student;
    private final Course course;
    private RequestStatus status = RequestStatus.PENDING;

    public RegistrationRequest(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
