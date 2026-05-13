package demo;

import services.UniversityDatabase;

import java.util.Comparator;

import exceptions.CourseNotTaughtException;
import exceptions.StudentNotEnrolledException;
import models.*;

public class TeacherMenu {

    public static void open(Teacher teacher, UniversityDatabase db)
            throws CourseNotTaughtException, StudentNotEnrolledException {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. View courses");
            System.out.println("2. View students info");
            System.out.println("3. Put marks");
            System.out.println("4. Send message to other employees");
            System.out.println("5. View research papers");
            System.out.print("6. Add research papers");
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
                    try {
                        // 1. Data Collection
                        String studentId = ConsoleUtils.askText("Student ID: ");
                        Student student = (Student) db.findUserById(studentId);
                        if (student == null) {
                            System.out.println("Error: Student ID not found.");
                            return; // Exit this menu option early
                        }

                        String courseCode = ConsoleUtils.askText("Course code: ");
                        Course course = db.findCourseByCode(courseCode);
                        if (course == null) {
                            System.out.println("Error: Course code not found.");
                            return;
                        }

                        int firstAttestation = ConsoleUtils.askInt("First attestation: (max 30) ");
                        int secondAttestation = ConsoleUtils.askInt("Second attestation: (max 30) ");
                        int finalExam = ConsoleUtils.askInt("Final exam: (max 40) ");
                        if (firstAttestation < 0 || firstAttestation > 30 || secondAttestation < 0
                                || secondAttestation > 30
                                || finalExam < 0 || finalExam > 40) {
                            System.out.println("Error: Marks must be within the specified ranges.");
                            return;
                        }
                        // 2. Delegate the "Action" to the Teacher object
                        // This is where the magic happens!
                        teacher.putMark(course, student, firstAttestation, secondAttestation, finalExam);

                        System.out.println("Successfully put marks for " + student.getName());

                    } catch (CourseNotTaughtException | StudentNotEnrolledException e) {
                        // 3. Graceful Error Handling
                        // Instead of crashing, we print the specific reason why it failed
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
                    teacher.printPapers(Comparator.comparingInt(ResearchPaper::getYear).reversed());
                }

                case 6 -> {
                    String title = ConsoleUtils.askText("Paper title: ");
                    int year = ConsoleUtils.askInt("Publication year: ");
                    int citations = ConsoleUtils.askInt("Number of citations: ");
                    int pages = ConsoleUtils.askInt("Number of pages: ");
                    ResearchPaper paper = new ResearchPaper(title, year, citations, pages);
                    teacher.addResearchPaper(paper);
                    System.out.println("Research paper added successfully.");
                }
            }
        }
    }
}
