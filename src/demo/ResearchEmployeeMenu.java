package demo;

import models.*;
import services.UniversityDatabase;

public class ResearchEmployeeMenu {

    public static void open(ResearchEmployee researchEmployee, UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Research Employee Menu ---");
            System.out.println("1. View supervised students");
            System.out.println("2. View research projects");
            System.out.println("3. Add research project");
            System.out.println("4. Send message to other employees");
            System.out.println("5. Add paper to a research project");
            System.out.println("6. Add research paper");
            System.out.println("0. Logout");
            System.out.print("Select an option: ");
            String choice = ConsoleUtils.askText("Select an option: ");
            switch (choice) {
                case "1" -> viewSupervisedStudents(researchEmployee, db);
                case "2" -> viewResearchProjects(db);
                case "3" -> addResearchProject(researchEmployee, db);
                case "4" -> sendMessageToEmployees(researchEmployee, db);
                case "5" -> addPaperToResearchProject(researchEmployee, db);
                case "6" -> addResearchPaper(researchEmployee, db);
                case "0" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewSupervisedStudents(ResearchEmployee researchEmployee, UniversityDatabase db) {
        for (User user : db.getUsers()) {
            if (user instanceof Student student) {
                if (student.getSupervisor() != null && student.getSupervisor().equals(researchEmployee)) {
                    System.out.println("- " + student.getName());
                }
            }
        }
    }

    private static void viewResearchProjects(UniversityDatabase db) {
        if (db.getResearchProjects().isEmpty()) {
            System.out.println("No research projects available.");
        } else {
            System.out.println("Research Projects:");
            for (ResearchProject project : db.getResearchProjects()) {
                System.out.println("- " + project.getName());
            }
        }
    }

    private static void addResearchPaper(ResearchEmployee researchEmployee, UniversityDatabase db) {
        String title = ConsoleUtils.askText("Paper title: ");
        int citations = ConsoleUtils.askInt("Number of citations: ");
        ResearchPaper paper = new ResearchPaper(title, researchEmployee, citations);
        researchEmployee.addResearchPaper(paper);
        System.out.println("Research paper added successfully.");
    }

    private static void sendMessageToEmployees(ResearchEmployee researchEmployee, UniversityDatabase db) {
        db.getMessages()
                .forEach(m -> System.out
                        .println("From: " + m.getFrom() + " | To: " + m.getTo() + " | Content: "
                                + m.getContent()));
        String recipientId = ConsoleUtils.askText("Enter recipient user ID: ");
        User recipient = db.findUserById(recipientId);
        if (recipient == null) {
            System.out.println("User not found.");
            return;
        }
        String messageContent = ConsoleUtils.askText("Enter message content: ");
        Message message = new Message(researchEmployee.getId(), recipientId, messageContent);
        db.addMessage(message);
        System.out.println("Message sent to " + recipient.getName());
    }

    private static void addPaperToResearchProject(ResearchEmployee researchEmployee, UniversityDatabase db) {
        db.getResearchProjects().forEach(p -> System.out.println("- " + p.getName()));
        String projectName = ConsoleUtils.askText("Enter research project name: ");
        ResearchProject project = db.findResearchProjectByName(projectName);
        if (project == null) {
            System.out.println("Research project not found.");
            return;
        }
        String paperTitle = ConsoleUtils.askText("Paper title: ");
        int citations = ConsoleUtils.askInt("Number of citations: ");
        User author = researchEmployee;
        ResearchPaper paper = new ResearchPaper(paperTitle, author, citations);
        project.addPaper(paper);
        System.out.println("Research paper added to project successfully.");
    }

    private static void addResearchProject(ResearchEmployee researchEmployee, UniversityDatabase db) {
        String projectName = ConsoleUtils.askText("Enter new research project name: ");
        if (db.findResearchProjectByName(projectName) != null) {
            System.out.println("A project with that name already exists.");
        } else {
            ResearchProject newProject = new ResearchProject(projectName);
            db.addResearchProject(newProject);
            System.out.println("Research project '" + projectName + "' added successfully.");
        }
    }
}