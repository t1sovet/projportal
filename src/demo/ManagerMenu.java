package demo;

import enums.*;
import models.*;
import services.UniversityDatabase;
import utils.SimpleStatisticsStrategy;
import exceptions.AlreadyEnrolledException;
import exceptions.CreditLimitExceededException;
import exceptions.FailLimitExceededException;
import exceptions.YearRequirementNotMetException;

public class ManagerMenu {
    public static void open(Manager manager, UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. List all users");
            System.out.println("2. Add course");
            System.out.println("3. Assign teacher to course");
            System.out.println("4. Process registration requests");
            System.out.println("5. View students info (sorted by gpa)");
            System.out.println("6. View students info (sorted alphabetically)");
            System.out.println("7. View teachers info (sorted by title)");
            System.out.println("8. Add news");
            System.out.println("9. Send message to other employees");
            System.out.println("10. View complaints");
            System.out.println("11. Generate report");
            System.out.println("12. View employee request signed by dean/rector");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            switch (choice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    db.getUsers()
                            .forEach(u -> System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail()));
                }
                case 2 -> {
                    String code = ConsoleUtils.askText("Course code: ");
                    String name = ConsoleUtils.askText("Course name: ");
                    int credits = ConsoleUtils.askInt("Credits: ");
                    int yearRequired = ConsoleUtils.askInt("Year required: ");
                    Course course = new Course(code, name, credits, yearRequired);
                    db.addCourse(course);
                    System.out.println("Course added.");
                }
                case 3 -> {
                    System.out.println("Available courses:");
                    db.getCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
                    String courseCode = ConsoleUtils.askText("Enter course code to assign: ");
                    Course course = db.findCourseByCode(courseCode);
                    if (course == null) {
                        System.out.println("Course not found.");
                        break;
                    }
                    System.out.println("Available teachers:");
                    db.getUsers().stream()
                            .filter(u -> u instanceof Teacher)
                            .forEach(t -> System.out.println(t.getId() + " | " + t.getName()));
                    String teacherId = ConsoleUtils.askText("Enter teacher ID to assign: ");
                    User user = db.findUserById(teacherId);
                    if (user == null || !(user instanceof Teacher)) {
                        System.out.println("Teacher not found.");
                        break;
                    }
                    Teacher teacher = (Teacher) user;
                    if (teacher.getTeachingCourses().contains(course)) {
                        System.out.println("Teacher is already assigned to this course.");
                        break;
                    }
                    teacher.assignCourse(course);
                    System.out.println("Teacher assigned to course.");
                }
                case 4 -> {
                    for (RegistrationRequest request : db.getRegistrationRequests()) {
                        if (request.getStatus() != RequestStatus.PENDING) {
                            continue;
                        }
                        System.out.println(
                                "Request: " + request.getStudent().getName() + " -> " + request.getCourse().getName());
                        int decision = ConsoleUtils.askInt("1 approve / 2 reject / 3 skip: ");
                        if (decision == 1) {
                            try {
                                request.getStudent().requestToEnroll(request.getCourse());
                                request.setStatus(RequestStatus.APPROVED);
                                System.out.println("Approved and registered.");
                            } catch (CreditLimitExceededException | FailLimitExceededException
                                    | AlreadyEnrolledException | YearRequirementNotMetException e) {
                                System.out.println("Approval failed: " + e.getMessage());
                                request.setStatus(RequestStatus.REJECTED);
                            }
                        } else if (decision == 2) {
                            request.setStatus(RequestStatus.REJECTED);
                            System.out.println("Rejected.");
                        }
                    }
                }
                case 5 -> {
                    db.getUsers().stream()
                            .filter(u -> u instanceof Student)
                            .map(u -> (Student) u)
                            .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
                            .forEach(s -> System.out.println(s.getId() + " | " + s.getName() + " | GPA: "
                                    + String.format("%.2f", s.calculateGPA())));
                }
                case 6 -> {
                    db.getUsers().stream()
                            .filter(u -> u instanceof Student)
                            .map(u -> (Student) u)
                            .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
                            .forEach(s -> System.out.println(s.getId() + " | " + s.getName()));
                }
                case 7 -> {
                    db.getUsers().stream()
                            .filter(u -> u instanceof Teacher)
                            .map(u -> (Teacher) u)
                            .sorted((t1, t2) -> t1.getTitle().compareTo(t2.getTitle()))
                            .forEach(t -> System.out.println(t.getId() + " | " + t.getName() + " | " + t.getTitle()));
                }
                case 8 -> {
                    String title = ConsoleUtils.askText("News title: ");
                    String content = ConsoleUtils.askText("News content: ");
                    db.addNews(new NewsItem(title, content));
                    System.out.println("News added.");
                }
                case 9 -> {
                    db.getMessages()
                            .forEach(m -> System.out
                                    .println(" | Content: "
                                            + m.getContent()));
                    String recipientId = ConsoleUtils.askText("Enter recipient user ID: ");
                    User recipient = db.findUserById(recipientId);
                    if (recipient == null) {
                        System.out.println("User not found.");
                        continue;
                    }
                    String messageContent = ConsoleUtils.askText("Enter message content: ");
                    Message message = new Message(manager.getId(), recipientId, messageContent);
                    db.addMessage(message);
                    System.out.println("Message sent to " + recipient.getName());

                }
                case 10 -> {
                    if (db.getComplaints().isEmpty()) {
                        System.out.println("No complaints available.");
                    } else {
                        System.out.println("\n--- Complaints ---");
                        db.getComplaints().forEach(c -> System.out.println(c.getHeader() +
                                " | Content: " + c.getBody()));
                    }
                }

                case 11 -> {
                    System.out.println("Generating report...");
                    db.getCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
                    manager.setReportStrategy(new SimpleStatisticsStrategy());
                    Course course = db.findCourseByCode(ConsoleUtils.askText("Enter Course Code: "));
                    if (course == null) {
                        System.out.println("Course not found.");
                        continue;
                    }
                    manager.performReport(course);
                }

                default -> {
                    System.out.println("Invalid choice.");
                }
                case 12 -> {
                    if (db.getEmployeeRequests().isEmpty()) {
                        System.out.println("No signed employee requests available.");
                    } else {
                        System.out.println("\n--- Employee Requests ---");
                        db.getEmployeeRequests().stream()
                                .filter(r -> r.getStatus().equals(RequestStatus.APPROVED))
                                .forEach(r -> System.out
                                        .println(r.getEmployeeId() + " | " + r.getContent() + " | " + r.getStatus()));
                    }
                }

            }
        }
    }
}