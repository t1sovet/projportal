package models;

import java.util.ArrayList;
import java.util.List;
import enums.DegreeType;
import exceptions.CreditLimitExceededException;
import exceptions.FailLimitExceededException;
import exceptions.LowHIndexException;
import enums.UserType;

public class Student extends User {
    private final static int MAX_CREDITS = 21;
    private final static int MAX_FAILS = 3;
    private final int year;
    private final DegreeType degreeType;
    private final List<Course> enrolledCourses = new ArrayList<>();
    private final List<Mark> marks = new ArrayList<>();
    private ResearchEmployee supervisor;
    private int creditsTaken = 0;
    private int failedCourses = 0;

    public Student(String id, String name, String email, String password, int year, DegreeType degreeType) {
        super(id, name, email, password);
        this.year = year;
        this.degreeType = degreeType;
        this.supervisor = null;
    }

    public Student(String id, String name, String email, String password, int year, DegreeType degreeType,
            ResearchEmployee supervisor) throws LowHIndexException {
        super(id, name, email, password);
        this.year = year;
        this.degreeType = degreeType;
        if (year >= 4) {
            if (supervisor.getHIndex() < 3) {
                throw new LowHIndexException("Supervisor must have an h-index of at least 3.");
            } else {
                this.supervisor = supervisor;
            }

        } else {
            this.supervisor = null;
            throw new IllegalArgumentException("Supervisor can only be assigned to students in 4th year or above.");
        }
    }

    public int getYear() {
        return year;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void addMark(Mark mark) {
        this.marks.add(mark);
    }

    @Override
    public UserType getRole() {
        return UserType.STUDENT;
    }

    public int getCreditsTaken() {
        return creditsTaken;
    }

    public int getFailedCourses() {
        return failedCourses;
    }

    public double calculateGPA() {
        int sum = 0;
        for (Mark mark : marks) {
            sum += mark.getTotal();
        }
        return sum / (double) marks.size();
    }

    public void requestToEnroll(Course course) throws CreditLimitExceededException, FailLimitExceededException {
        if (creditsTaken + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException(
                    "Cannot enroll in more than " + MAX_CREDITS + " credits.");
        }
        if (failedCourses >= MAX_FAILS) {
            throw new FailLimitExceededException(
                    "Cannot enroll in more courses after failing " + MAX_FAILS + " courses.");
        }
        enrolledCourses.add(course);
        creditsTaken += course.getCredits();
    }

    public void setSupervisor(ResearchEmployee supervisor) throws LowHIndexException {
        if (year < 4) {
            throw new IllegalArgumentException("Supervisor can only be assigned to students in 4th year or above.");
        }
        if (supervisor.getHIndex() < 3) {
            throw new LowHIndexException("Supervisor must have an h-index of at least 3.");
        }
        this.supervisor = supervisor;
    }

    public ResearchEmployee getSupervisor() {
        return supervisor;
    }

}
