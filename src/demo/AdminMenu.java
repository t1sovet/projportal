package demo;

import models.NewsItem;
import enums.ManagerType;
import models.*;
import services.UniversityDatabase;
import services.ResearchService;
import utils.UserFactory;
import enums.*;

public final class AdminMenu {

    public static void open(Admin admin, UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add news");
            System.out.println("2. View logs");
            System.out.println("3. Add User");
            System.out.println("4. Remove User");
            System.out.println("5. Update User(Not reccomended)");
            System.out.println("6. Search User by Regular Expression");
            System.out.println("7. Send message to other employees");
            System.out.println("8. Promote student to Researcher (Decorator)");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");

            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                String title = ConsoleUtils.askText("Title: ");
                String body = ConsoleUtils.askText("Body: ");
                db.addNews(new NewsItem(title, body));
                System.out.println("News added.");
            }
            if (choice == 2) {
                db.getLogger().getLogs().forEach(System.out::println);
            }
            if (choice == 3) {
                System.out.println("Select user type:");
                System.out.println("1. Admin");
                System.out.println("2. Manager");
                System.out.println("3. Teacher");
                System.out.println("4. Research Employee");
                System.out.println("5. Student");
                int userType = ConsoleUtils.askInt("Choose user type: ");
                switch (userType) {
                    case 1:
                        System.out.println("Adding Admin...");
                        String adminId = ConsoleUtils.askText("ID: ");
                        String adminName = ConsoleUtils.askText("Name: ");
                        String adminEmail = ConsoleUtils.askText("Email: ");
                        String adminPassword = ConsoleUtils.askText("Password: ");
                        db.addUser(UserFactory.createAdmin(adminId, adminName, adminEmail, adminPassword));
                        System.out.println("Admin added.");
                        break;
                    case 2:
                        System.out.println("Adding Manager...");
                        String managerId = ConsoleUtils.askText("ID: ");
                        String managerName = ConsoleUtils.askText("Name: ");
                        String managerEmail = ConsoleUtils.askText("Email: ");
                        String managerPassword = ConsoleUtils.askText("Password: ");
                        System.out.println("Select manager type:");
                        System.out.println("1. OR");
                        System.out.println("2. Department");
                        int managerTypeChoice = ConsoleUtils.askInt("Choose manager type: ");
                        ManagerType managerType = managerTypeChoice == 1 ? ManagerType.OR : ManagerType.DEPARTMENT;
                        db.addUser(UserFactory.createManager(managerId, managerName, managerEmail, managerPassword,
                                managerType));
                        System.out.println("Manager added.");
                        break;
                    case 3:
                        System.out.println("Adding Teacher...");
                        String teacherId = ConsoleUtils.askText("ID: ");
                        String teacherName = ConsoleUtils.askText("Name: ");
                        String teacherEmail = ConsoleUtils.askText("Email: ");
                        String teacherPassword = ConsoleUtils.askText("Password: ");
                        System.out.println("Select teacher title:");
                        System.out.println("1. Lecturer");
                        System.out.println("2. Professor");
                        int teacherTitleChoice = ConsoleUtils.askInt("Choose teacher title: ");
                        TeacherTitle teacherTitle = teacherTitleChoice == 1 ? TeacherTitle.LECTURER
                                : TeacherTitle.PROFESSOR;
                        db.addUser(UserFactory.createTeacher(teacherId, teacherName, teacherEmail, teacherPassword,
                                teacherTitle));
                        System.out.println("Teacher added.");
                        break;
                    case 4:
                        System.out.println("Adding Research Employee...");
                        String researchId = ConsoleUtils.askText("ID: ");
                        String researchName = ConsoleUtils.askText("Name: ");
                        String researchEmail = ConsoleUtils.askText("Email: ");
                        String researchPassword = ConsoleUtils.askText("Password: ");
                        int hIndex = ConsoleUtils.askInt("H-Index: ");
                        db.addUser(UserFactory.createResearchEmployee(researchId, researchName, researchEmail,
                                researchPassword, hIndex));
                        System.out.println("Research Employee added.");
                        break;
                    case 5:
                        System.out.println("Adding Student...");
                        String studentId = ConsoleUtils.askText("ID: ");
                        String studentName = ConsoleUtils.askText("Name: ");
                        String studentEmail = ConsoleUtils.askText("Email: ");
                        String studentPassword = ConsoleUtils.askText("Password: ");
                        int year = ConsoleUtils.askInt("Year: ");
                        if (year < 1 || year > 4) {
                            System.out.println("Invalid year. Must be between 1 and 4.");
                            break;
                        }
                        if (year == 4) {
                            System.out.println("Select Research Employee supervisor:");
                            db.getUsers().stream().filter(r -> r instanceof ResearchEmployee)
                                    .forEach(r -> System.out.println(r.getId() + " | " + r.getName()));
                            String supervisorId = ConsoleUtils.askText("Enter supervisor ID: ");
                            User supervisor = db.findUserById(supervisorId);
                            if (supervisor == null || !(supervisor instanceof ResearchEmployee)) {
                                System.out.println("Supervisor not found or not a Research Employee.");
                                break;
                            }
                            try {
                                db.addUser(UserFactory.create4thYearStudent(studentId, studentName, studentEmail,
                                        studentPassword, DegreeType.BACHELOR, (ResearchEmployee) supervisor));
                                System.out.println("4th Year Student added.");
                            } catch (Exception e) {
                                System.out.println("Failed to add 4th Year Student: " + e.getMessage());
                            }
                        } else {
                            db.addUser(UserFactory.createStudent(studentId, studentName, studentEmail, studentPassword,
                                    year, DegreeType.BACHELOR));
                            System.out.println("Student added.");
                        }
                        break;

                    default:
                        break;
                }

            }
            if (choice == 4) {
                db.getUsers().forEach(u -> System.out.println(u.getId() + " | " + u.print()));
                String userId = ConsoleUtils.askText("Enter user ID to remove: ");
                if (admin.getId().equals(userId)) {
                    System.out.println("You cannot remove yourself.");
                    continue;
                }
                User userToRemove = db.getUsers().stream()
                        .filter(u -> u.getId().equals(userId))
                        .findFirst()
                        .orElse(null);
                if (userToRemove != null) {
                    db.removeUser(userToRemove);
                    System.out.println("User removed.");
                } else {
                    System.out.println("User not found.");
                }
            }
            if (choice == 5) {
                db.getUsers().forEach(u -> System.out.println(u.getId() + " | " + u.print()));
                String userId = ConsoleUtils.askText("Enter user ID to update: ");
                User userToUpdate = db.getUsers().stream()
                        .filter(u -> u.getId().equals(userId))
                        .findFirst()
                        .orElse(null);
                if (userToUpdate != null) {
                    String newName = ConsoleUtils.askText("New name (leave blank to keep current): ");
                    String newEmail = ConsoleUtils.askText("New email (leave blank to keep current): ");
                    String newPassword = ConsoleUtils.askText("New password (leave blank to keep current): ");
                    if (!newName.isBlank()) {
                        userToUpdate.setName(newName);
                    }
                    if (!newEmail.isBlank()) {
                        userToUpdate.setEmail(newEmail);
                    }
                    if (!newPassword.isBlank()) {
                        userToUpdate.setPassword(newPassword);
                    }
                    System.out.println("User updated.");
                } else {
                    System.out.println("User not found.");
                }
            }
            if (choice == 6) {
                System.out.println("Search results:");
                String regex = ConsoleUtils
                        .askText("Enter regular expression to search for users: (matches name, email, or ID): ");
                db.getUsers().stream()
                        .filter(u -> u.getName().matches(regex) || u.getEmail().matches(regex)
                                || u.getId().matches(regex))
                        .forEach(u -> System.out.println(u.getId() + " | " + u.print()));

            }
            if (choice == 7) {
                db.getUsers().forEach(u -> System.out.println(u.getId() + " | " + u.getName()));
                db.getMessages()
                        .forEach(m -> System.out.println("From: " + m.getFrom() + " | To: " + m.getTo() + " | Content: "
                                + m.getContent()));
                String recipientId = ConsoleUtils.askText("Enter recipient user ID: ");
                User recipient = db.findUserById(recipientId);
                if (recipient instanceof Student) {
                    System.out.println("Cannot send message to students.");
                    continue;
                }
                if (recipient == null) {
                    System.out.println("User not found.");
                    continue;
                }
                String messageContent = ConsoleUtils.askText("Enter message content: ");
                Message message = new Message(admin.getId(), recipientId, messageContent);
                db.addMessage(message);
                System.out.println("Message sent to " + recipient.getName());
            }
            if (choice == 8) {
                String studentId = ConsoleUtils.askText("Enter student ID to promote: ");
                User student = db.findUserById(studentId);
                if (student == null || !(student instanceof Student)) {
                    System.out.println("Student not found.");
                    continue;
                }
                String password = ConsoleUtils.askText("Enter new password for researcher: ");
                try {
                    ResearchService.promoteToResearcher((Student) student, password, db);
                    System.out.println("Student promoted to Researcher.");
                } catch (Exception e) {
                    System.out.println("Promotion failed: " + e.getMessage());
                }
            }
        }
    }
}
