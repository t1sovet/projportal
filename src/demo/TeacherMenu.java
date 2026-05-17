package demo;

import services.UniversityDatabase;

import java.util.Comparator;

import enums.LessonType;
import enums.WeekDays;
import exceptions.CourseNotTaughtException;
import exceptions.LessonTimeConflictException;
import exceptions.StudentNotEnrolledException;
import models.*;

public class TeacherMenu {

    public static void open(Teacher teacher, UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. View teaching courses");
            System.out.println("2. View students info");
            System.out.println("3. Put marks");
            System.out.println("4. Send message to other employees");
            System.out.println("5. View research papers");
            System.out.println("6. Add research papers");
            System.out.println("7. Send complaint");
            System.out.println("8. Add research paper to research project");
            System.out.println("9. Add lesson to schedule");
            System.out.println("10. Send employee request");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");

            switch (choice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    System.out.println("\n--- Your Courses ---");
                    teacher.getTeachingCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
                }
                case 2 -> {
                    System.out.println("\n--- Students Info ---");
                    db.getUsers().stream()
                            .filter(u -> u instanceof Student)
                            .map(u -> (Student) u)
                            .forEach(s -> System.out
                                    .println(s.getId() + " | " + s.getName() + " | " + s.getDegreeType()));
                }
                case 3 -> {
                    if (teacher.getTeachingCourses().isEmpty()) {
                        System.out.println("You are not teaching any courses.");
                        continue;
                    }
                    teacher.getTeachingCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
                    String courseCode = ConsoleUtils.askText("Enter course code to put marks for: ");
                    Course course = db.findCourseByCode(courseCode);
                    for (Student s : db.getUsers().stream().filter(u -> u instanceof Student).map(u -> (Student) u)
                            .filter(s -> s.getEnrolledCourses().contains(course)).toList()) {
                        System.out.println("- " + s.getName() + " (ID: " + s.getId() + ")");
                    }

                    try {
                        String studentId = ConsoleUtils.askText("Student ID: ");
                        Student student = (Student) db.findUserById(studentId);
                        if (student == null) {
                            System.out.println("Error: Student ID not found.");
                            return;
                        }

                        int firstAttestation = ConsoleUtils.askInt("First attestation: (max 30) ");
                        int secondAttestation = ConsoleUtils.askInt("Second attestation: (max 30) ");
                        int finalExam = ConsoleUtils.askInt("Final exam: (max 40) ");
                        teacher.putMark(course, student, firstAttestation, secondAttestation, finalExam);

                        System.out.println("Successfully put marks for " + student.getName());

                    } catch (CourseNotTaughtException | StudentNotEnrolledException e) {
                        System.out.println("Operation Failed: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                }
                case 4 -> {
                    db.getMessages()
                            .forEach(m -> System.out
                                    .println("From: " + m.getFrom() + " | To: " + m.getTo() + " | Content: "
                                            + m.getContent()));
                    String recipientId = ConsoleUtils.askText("Enter recipient user ID: ");
                    User recipient = db.findUserById(recipientId);
                    if (recipient == null) {
                        System.out.println("User not found.");
                        continue;
                    }
                    String messageContent = ConsoleUtils.askText("Enter message content: ");
                    Message message = new Message(teacher.getId(), recipientId, messageContent);
                    db.addMessage(message);
                    System.out.println("Message sent to " + recipient.getName());

                }

                case 5 -> {
                    System.out.println("\n--- Your Research Papers ---");
                    teacher.printPapers(Comparator.comparingInt(ResearchPaper::getCitations).reversed());
                }

                case 6 -> {
                    String title = ConsoleUtils.askText("Paper title: ");
                    int citations = ConsoleUtils.askInt("Number of citations: ");
                    ResearchPaper paper = new ResearchPaper(title, teacher, citations);
                    teacher.addResearchPaper(paper);
                    System.out.println("Research paper added successfully.");
                }

                case 7 -> {
                    String complaintContent = ConsoleUtils.askText("Enter your complaint: ");
                    Complaint complaint = new Complaint("Complaint from " + teacher.getName(), complaintContent);
                    db.addComplaint(complaint);
                    System.out.println("Complaint sent");
                }

                case 8 -> {
                    db.getResearchProjects().forEach(p -> System.out.println("- " + p.getName()));
                    String projectName = ConsoleUtils.askText("Enter research project name: ");
                    ResearchProject project = db.findResearchProjectByName(projectName);
                    if (project == null) {
                        System.out.println("Research project not found.");
                        continue;
                    }
                    String paperTitle = ConsoleUtils.askText("Paper title: ");
                    int citations = ConsoleUtils.askInt("Number of citations: ");
                    User author = teacher;
                    ResearchPaper paper = new ResearchPaper(paperTitle, author, citations);
                    project.addPaper(paper);
                    System.out.println("Research paper added to project successfully.");
                }
                case 9 -> {
                    teacher.getTeachingCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
                    String courseCode = ConsoleUtils.askText("Enter course code to add lesson to: ");
                    Course course = db.findCourseByCode(courseCode);
                    if (course == null || !teacher.getTeachingCourses().contains(course)) {
                        System.out.println("Course not found or you do not teach this course.");
                        continue;
                    }
                    try {
                        String typeInput = ConsoleUtils
                                .askText("Enter lesson type (LECTURE: 1, PRACTICE: 2, LABORATORY: 3): ");
                        String dayInput = ConsoleUtils
                                .askText("Enter lesson day (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY): ");
                        int hour = ConsoleUtils.askInt("Enter lesson hour (8-20): ");
                        int room = ConsoleUtils.askInt("Enter room number (1-999): ");

                        LessonType lessonType = parseLessonType(typeInput);
                        WeekDays day = WeekDays.valueOf(dayInput.trim().toUpperCase());
                        Lesson lesson = new Lesson(course, teacher, lessonType, day, hour, room);

                        db.addLesson(lesson);
                        System.out.println("Lesson added to schedule successfully.");
                    } catch (LessonTimeConflictException e) {
                        System.out.println("Cannot add lesson: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid lesson data: " + e.getMessage());
                    }
                }

                case 10 -> {
                    String requestContent = ConsoleUtils.askText("Enter your employee request: ");
                    EmployeeRequest request = new EmployeeRequest("Employee request from " + teacher.getName(),
                            requestContent);
                    db.addEmployeeRequest(request);
                    System.out.println("Employee request sent");
                }
            }
        }
    }

    private static LessonType parseLessonType(String typeInput) {
        String normalized = typeInput == null ? "" : typeInput.trim();
        return switch (normalized) {
            case "1" -> LessonType.LECTURE;
            case "2" -> LessonType.PRACTICE;
            case "3" -> LessonType.LABORATORY;
            default -> throw new IllegalArgumentException("Unknown lesson type. Use 1, 2, or 3.");
        };
    }
}
