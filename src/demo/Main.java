package demo;

import java.util.List;

import services.AuthService;
import services.UniversityDatabase;
import utils.DataStorage;
import models.Admin;
import models.User;
import models.Course;
import models.Teacher;
import models.Manager;
import models.ResearchEmployee;
import models.ResearchProject;
import models.Student;
import enums.ManagerType;
import enums.TeacherTitle;
import exceptions.*;

public class Main {
    private static final String DATA_PATH = "data.ser";

    public static void main(String[] args) {
        UniversityDatabase db = UniversityDatabase.getInstance();
        loadIfExists(db);
        seedIfEmpty(db);

        AuthService authService = new AuthService(db);
        authService.addObserver(db.getLogger());

        while (true) {
            System.out.println("\n===Welcome to WSP-wannabe===");
            System.out.println("1. Login");
            System.out.println("2. View News");
            System.out.println("3. View research projects (sorted alphabetically)");
            System.out.println("4. View research projects (sorted by page number)");
            System.out.println("5. Show top researchers (by H-Index)");
            System.out.println("0. Exit");
            int inputInt = ConsoleUtils.askInt("Choose an option: ");

            if (inputInt == 0) {
                DataStorage.save(db, DATA_PATH);
                System.out.println("Saved data to " + DATA_PATH + ". Exiting...");
                return;
            }
            if (inputInt == 1) {
                String email = ConsoleUtils.askText("Email: ");
                String password = ConsoleUtils.askText("Password: ");
                try {
                    User user = authService.login(email, password);
                    System.out.println("Login successful! Welcome, " + user.getName() + " (" + user.getRole() + ")");
                    openRoleMenu(user, db);
                } catch (AuthenticationException e) {
                    System.out.println("Login failed: " + e.getMessage());
                }
            }
            if (inputInt == 2) {
                List<models.NewsItem> news = db.getNews();
                if (news.isEmpty()) {
                    System.out.println("No news available.");
                } else {
                    System.out.println("\n--- Latest News ---");
                    for (models.NewsItem item : news) {
                        System.out.println(item);
                    }
                }

            }
            if (inputInt == 3) {
                List<ResearchProject> projects = db.getResearchProjects();
                if (projects.isEmpty()) {
                    System.out.println("No research projects available.");
                } else {
                    System.out.println("\n--- Research Projects (sorted alphabetically) ---");
                    projects.stream()
                            .sorted((p1, p2) -> p1.name.compareToIgnoreCase(p2.name))
                            .forEach(p -> System.out.println(p.name));
                }
            }
            if (inputInt == 5) {
                List<ResearchEmployee> researchers = db.getUsers().stream()
                        .filter(u -> u instanceof ResearchEmployee)
                        .map(u -> (ResearchEmployee) u)
                        .toList();
                if (researchers.isEmpty()) {
                    System.out.println("No researchers available.");
                } else {
                    System.out.println("\n--- Top Researchers (by H-Index) ---");
                    researchers.stream()
                            .sorted((r1, r2) -> Integer.compare(r2.getHIndex(), r1.getHIndex()))
                            .forEach(r -> System.out.println(r.getName() + " | H-Index: " + r.getHIndex()));
                }
            }
            if (inputInt == 4) {
                List<ResearchProject> projects = db.getResearchProjects();
                if (projects.isEmpty()) {
                    System.out.println("No research projects available.");
                } else {
                    System.out.println("\n--- Research Projects (sorted by page count) ---");
                    projects.stream()
                            .sorted((p1, p2) -> Integer.compare(p2.getPageCount(), p1.getPageCount()))
                            .forEach(p -> System.out.println(p.getName() + " | Pages: " + p.getPageCount()));
                }
            }
        }
    }

    private static void loadIfExists(UniversityDatabase db) {
        UniversityDatabase loadedDb = utils.DataStorage.load(DATA_PATH);

        if (loadedDb == null) {
            System.out.println("Starting fresh: " + DATA_PATH + " not found or corrupted.");
            return;
        }

        db.setUsers(loadedDb.getUsers());
        db.setCourses(loadedDb.getCourses());
        db.setNews(loadedDb.getNews());
        db.setResearchProjects(loadedDb.getResearchProjects());

        System.out.println("Successfully restored " + db.getUsers().size() + " users.");
    }

    private static void seedIfEmpty(UniversityDatabase db) {
        if (!db.getUsers().isEmpty()) {
            return;
        }

        Admin admin = new Admin("A1", "AdminName", "admin@uni.kz", "admin123");
        Manager manager = new Manager("M1", "ManagerName", "manager@uni.kz", "manager123", ManagerType.OR);
        Course cs101 = new Course("CS101", "Intro to Computer Science", 3, 1);
        Course ma102 = new Course("MA102", "Calculus I", 4, 1);
        Course ph103 = new Course("PH103", "Physics I", 4, 1);
        Course cs201 = new Course("CS201", "Data Structures", 3, 2);
        Teacher prof = new Teacher("P1", "Pakita", "pakita@uni.kz", "pakita123", TeacherTitle.PROFESSOR, 5);
        Teacher lecturer = new Teacher("L1", "Beken", "beken@uni.kz", "beken123",
                TeacherTitle.LECTURER, 2);
        Student student1 = new Student("S1", "Fedya", "fedya@uni.kz", "fedya123", 1,
                enums.DegreeType.BACHELOR);
        Student student2 = new Student("S2", "Dima", "dima@uni.kz", "dima123", 2,
                enums.DegreeType.BACHELOR);
        Student student3 = new Student("S3", "Karim", "karim@uni.kz", "karim123", 3,
                enums.DegreeType.BACHELOR);
        Student student4 = new Student("S4", "Tevos", "tevos@uni.kz", "tevos123", 4,
                enums.DegreeType.BACHELOR);
        ResearchEmployee researchEmployee = new ResearchEmployee("R1", "Roberto", "roberto@uni.kz", "roberto123", 3);
        ResearchProject project1 = new ResearchProject("data science");
        ResearchProject project2 = new ResearchProject("quantum computing");

        db.addUser(admin);
        db.addUser(manager);
        db.addUser(prof);
        db.addUser(lecturer);
        db.addUser(student1);
        db.addUser(student2);
        db.addUser(student3);
        db.addUser(student4);
        db.addUser(researchEmployee);
        db.addCourse(ph103);
        db.addCourse(cs101);
        db.addCourse(ma102);
        db.addCourse(cs201);
        db.addResearchProject(project1);
        db.addResearchProject(project2);
        try {
            student4.setSupervisor(researchEmployee);
        } catch (LowHIndexException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void openRoleMenu(User user, UniversityDatabase db) {
        if (user instanceof Admin admin) {
            AdminMenu.open(admin, db);
        } else if (user instanceof Manager manager) {
            ManagerMenu.open(manager, db);
        } else if (user instanceof Teacher teacher) {
            try {
                TeacherMenu.open(teacher, db);
            } catch (CourseNotTaughtException | StudentNotEnrolledException e) {
                System.out.println(e.getMessage());
            }
        } else if (user instanceof Student student) {
            StudentMenu.open(student, db);
        } else if (user instanceof ResearchEmployee researchEmployee) {
            ResearchEmployeeMenu.open(researchEmployee, db);
        }
    }
}
