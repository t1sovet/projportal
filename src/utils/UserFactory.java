package utils;

import models.*;
import enums.*;
import exceptions.*;

public class UserFactory {

    public static User createUser(UserType type, String id, String name, String email, String password) {
        return switch (type) {
            case ADMIN -> new Admin(id, name, email, password);
            case MANAGER -> new Manager(id, name, email, password, ManagerType.OR);
            case STUDENT -> new Student(id, name, email, password, 1, DegreeType.BACHELOR);
            case TEACHER -> new Teacher(id, name, email, password, TeacherTitle.LECTURER, 0);
            case RESEARCH_EMPLOYEE -> new ResearchEmployee(id, name, email, password, 0);
            case DEAN -> new Dean(id, name, email, password);
            case RECTOR -> new Rector(id, name, email, password);
            default -> throw new IllegalArgumentException("Unknown user type: " + type);
        };
    }

    public static Student createStudent(String id, String name, String email, String password, int year,
            DegreeType degree) {
        return new Student(id, name, email, password, year, degree);
    }

    public static Student create4thYearStudent(String id, String name, String email, String password,
            DegreeType degree, ResearchEmployee supervisor) throws LowHIndexException {
        return new Student(id, name, email, password, 4, degree, supervisor);
    }

    public static Teacher createTeacher(String id, String name, String email, String password, TeacherTitle title) {
        return new Teacher(id, name, email, password, title, 0);
    }

    public static ResearchEmployee createResearchEmployee(String id, String name, String email, String password,
            int hIndex) {
        return new ResearchEmployee(id, name, email, password, hIndex);
    }

    public static Manager createManager(String id, String name, String email, String password,
            ManagerType managerType) {
        return new Manager(id, name, email, password, managerType);
    }

    public static Admin createAdmin(String id, String name, String email, String password) {
        return new Admin(id, name, email, password);
    }
}