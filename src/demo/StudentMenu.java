package demo;

import models.*;
import services.UniversityDatabase;

public class StudentMenu {

    public static void open(Student student, UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View courses");
            System.out.println("2. Register for courses");
            System.out.println("3. View info about teachers");
            System.out.println("4. View marks");
            System.out.println("5. View transcript");
            System.out.println("6. Rate teachers");
            System.out.println("7. Enroll in research projects");
            System.out.println("8. View enrolled courses");
            System.out.println("9. View schedule");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");

            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                System.out.println("Viewing courses...");
                db.getCourses().forEach(System.out::println);
            }
            if (choice == 2) {
                System.out.println("Registering for courses...");
                db.getCourses().forEach(System.out::println);
                String courseCode = ConsoleUtils.askText("Enter course code to register: ");
                Course courseToRegister = db.findCourseByCode(courseCode);
                if (courseToRegister != null) {
                    RegistrationRequest request = new RegistrationRequest(student, courseToRegister);
                    db.addRegistrationRequest(request);
                    System.out.println("Registration request submitted for " + courseToRegister.getName());
                } else {
                    System.out.println("Course not found.");
                }
            }
            if (choice == 3) {
                db.getUsers().stream()
                        .filter(u -> u instanceof Teacher)
                        .forEach(t -> System.out.println(((Teacher) t).getTeachingCourses().stream()
                                .map(Course::getCode)
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("No courses") + " | " + t.getName() + " | H-Index: "
                                + ((Teacher) t).getHIndex()));
            }
            if (choice == 4) {
                System.out.println("Viewing marks...");
                student.getMarks().stream()
                        .filter(m -> m.getCourse() != null)
                        .forEach(m -> System.out.println(m.getCourse().getCode() + " | " + m.getCourse().getName()
                                + " | " + m.getFirstAttestation() + " | " + m.getSecondAttestation() + " | "
                                + m.getFinalExam()));
            }
            if (choice == 5) {
                System.out.println("Viewing transcript...");
                student.getMarks().stream()
                        .filter(m -> m.getCourse() != null)
                        .forEach(m -> System.out.println(m.getCourse().getCode() + " | " + m.getCourse().getName()
                                + " | " + m.getFirstAttestation() + " | " + m.getSecondAttestation() + " | "
                                + m.getFinalExam()));
                System.out.println("GPA: " + student.calculateGPA());
            }
            if (choice == 6) {
                System.out.println("Rating teachers...");
                db.getUsers().stream()
                        .filter(u -> u instanceof Teacher)
                        .forEach(t -> System.out.println(((Teacher) t).getTeachingCourses().stream()
                                .map(Course::getCode)
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("No courses") + " | " + t.getName() + " | Average Rating: "
                                + ((Teacher) t).getAverageRating()));
                String teacherId = ConsoleUtils.askText("Enter teacher ID to rate: ");
                User user = db.findUserById(teacherId);
                if (user == null || !(user instanceof Teacher)) {
                    System.out.println("Teacher not found.");
                    continue;
                }
                Teacher teacherToRate = (Teacher) user;
                int rating = ConsoleUtils.askInt("Enter rating (1-5): ");
                try {
                    teacherToRate.addRating(rating);
                    System.out.println("Rating submitted for " + teacherToRate.getName());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid rating: " + e.getMessage());
                }
            }
            if (choice == 8) {
                System.out.println("Viewing enrolled courses...");
                student.getEnrolledCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
            }
            if (choice == 9) {
                System.out.println("Viewing schedule...");
                student.getEnrolledCourses().forEach(c -> c.getLessons().forEach(l -> System.out.println(
                        l.getDay() + " " + l.getHour() + ":00/" + (l.getHour() + 1) + ":00 | " + c.getCode() + " | "
                                + c.getName() + " | "
                                + l.getLessonType() + " | Room: " + l.getRoomNumber())));
            }
        }
    }
}
