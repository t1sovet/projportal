package demo;

import models.*;
import services.UniversityDatabase;

public class ResearchEmployeeMenu {

    public static void open(ResearchEmployee researchEmployee, UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Research Employee Menu ---");
            System.out.println("1. View supervised students");
            System.out.println("2. View research projects");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");
            String choice = ConsoleUtils.askText("Select an option: ");
            switch (choice) {
                case "1" -> viewSupervisedStudents(researchEmployee, db);
                case "2" -> viewResearchProjects(db);
                case "3" -> {
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
                System.out.println("- " + project.name);
            }
        }
    }
}
