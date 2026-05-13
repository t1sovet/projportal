package demo;

import services.UniversityDatabase;
import models.*;
import enums.*;

public class DeanRectorMenu {

    public void open(Dean dean, UniversityDatabase db) {
        while (true) {
            System.out.println("1. View all courses");
            System.out.println("2. View all teachers");
            System.out.println("3. View all students");
            System.out.println("4. View all requests");
            System.out.println("5. Process employee requests");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");

            if (choice == 0) {
                break;
            }

            if (choice == 1) {
                System.out.println("Viewing all courses...");
                db.getCourses().forEach(c -> System.out.println(c.getCode() + " | " + c.getName()));
            }

            if (choice == 2) {
                System.out.println("Viewing all teachers...");
                db.getUsers().stream().filter(u -> u instanceof Teacher)
                        .forEach(t -> System.out.println(t.getId() + " | " + t.getName()));
            }

            if (choice == 3) {
                System.out.println("Viewing all students...");
                db.getUsers().stream().filter(u -> u instanceof Student)
                        .forEach(s -> System.out.println(s.getId() + " | " + s.getName()));
            }

            if (choice == 4) {
                System.out.println("Viewing all requests...");
                for (EmployeeRequest request : db.getEmployeeRequests()) {
                    System.out.println(
                            request.getEmployeeId() + " | " + request.getContent() + " | " + request.getStatus());
                    int decision = ConsoleUtils.askInt("Approve (1) or Reject (2) this request? ");
                    if (decision == 1) {
                        request.setStatus(RequestStatus.APPROVED);
                        System.out.println("Request approved.");
                    } else if (decision == 2) {
                        request.setStatus(RequestStatus.REJECTED);
                        System.out.println("Request rejected.");
                    } else {
                        System.out.println("Invalid choice. Skipping this request.");
                    }
                }
            }
        }
    }
}
