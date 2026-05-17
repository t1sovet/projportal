package demo;

import java.util.Comparator;

import models.Message;
import models.ResearchPaper;
import models.ResearchProject;
import models.Student;
import models.User;
import services.ResearcherDecorator;
import services.UniversityDatabase;

public class StudentResearcherMenu {

    public static void open(ResearcherDecorator studentResearcher, UniversityDatabase db) {
        if (!(studentResearcher.getInnerUser() instanceof Student)) {
            throw new IllegalArgumentException("Student researcher menu requires a wrapped Student user.");
        }

        Student student = (Student) studentResearcher.getInnerUser();

        while (true) {
            System.out.println("\n--- Student Researcher Menu ---");
            System.out.println("1. View my student profile");
            System.out.println("2. View research projects");
            System.out.println("3. Add research project");
            System.out.println("4. Add paper to a research project");
            System.out.println("5. Add my research paper");
            System.out.println("6. View my research papers");
            System.out.println("7. Send message to other employees");
            System.out.println("0. Back");
            String choice = ConsoleUtils.askText("Select an option: ");

            switch (choice) {
                case "1" -> viewStudentProfile(student);
                case "2" -> viewResearchProjects(db);
                case "3" -> addResearchProject(db);
                case "4" -> addPaperToResearchProject(studentResearcher, db);
                case "5" -> addResearchPaper(studentResearcher);
                case "6" ->
                    studentResearcher.printPapers(Comparator.comparingInt(ResearchPaper::getCitations).reversed());
                case "7" -> sendMessageToEmployees(studentResearcher, db);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewStudentProfile(Student student) {
        System.out.println(student.getId() + " | " + student.getName() + " | " + student.getDegreeType());
    }

    private static void viewResearchProjects(UniversityDatabase db) {
        if (db.getResearchProjects().isEmpty()) {
            System.out.println("No research projects available.");
            return;
        }

        System.out.println("Research Projects:");
        for (ResearchProject project : db.getResearchProjects()) {
            System.out.println("- " + project.getName());
        }
    }

    private static void addResearchProject(UniversityDatabase db) {
        String projectName = ConsoleUtils.askText("Enter new research project name: ");
        if (db.findResearchProjectByName(projectName) != null) {
            System.out.println("A project with that name already exists.");
            return;
        }

        db.addResearchProject(new ResearchProject(projectName));
        System.out.println("Research project '" + projectName + "' added successfully.");
    }

    private static void addPaperToResearchProject(ResearcherDecorator studentResearcher, UniversityDatabase db) {
        viewResearchProjects(db);
        if (db.getResearchProjects().isEmpty()) {
            return;
        }

        String projectName = ConsoleUtils.askText("Enter research project name: ");
        ResearchProject project = db.findResearchProjectByName(projectName);
        if (project == null) {
            System.out.println("Research project not found.");
            return;
        }

        String paperTitle = ConsoleUtils.askText("Paper title: ");
        int citations = ConsoleUtils.askInt("Number of citations: ");
        ResearchPaper paper = new ResearchPaper(paperTitle, studentResearcher, citations);
        project.addPaper(paper);
        System.out.println("Research paper added to project successfully.");
    }

    private static void addResearchPaper(ResearcherDecorator studentResearcher) {
        String title = ConsoleUtils.askText("Paper title: ");
        int citations = ConsoleUtils.askInt("Number of citations: ");
        ResearchPaper paper = new ResearchPaper(title, studentResearcher, citations);
        studentResearcher.addResearchPaper(paper);
        System.out.println("Research paper added successfully.");
    }

    private static void sendMessageToEmployees(ResearcherDecorator studentResearcher, UniversityDatabase db) {
        db.getMessages().forEach(m -> System.out.println(
                "From: " + m.getFrom() + " | To: " + m.getTo() + " | Content: " + m.getContent()));

        String recipientId = ConsoleUtils.askText("Enter recipient user ID: ");
        User recipient = db.findUserById(recipientId);
        if (recipient == null) {
            System.out.println("User not found.");
            return;
        }

        String messageContent = ConsoleUtils.askText("Enter message content: ");
        Message message = new Message(studentResearcher.getId(), recipientId, messageContent);
        db.addMessage(message);
        System.out.println("Message sent to " + recipient.getName());
    }
}
