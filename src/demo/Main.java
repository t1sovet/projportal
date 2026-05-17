package demo;

import java.util.List;

import services.AuthService;
import services.ResearcherDecorator;
import services.UniversityDatabase;
import utils.DataStorage;
import gui.WspGui;
import models.*;
import enums.*;
import exceptions.*;

public class Main {
    private static final String DATA_PATH = "data.ser";

    public static void main(String[] args) {
        UniversityDatabase db = UniversityDatabase.getInstance();
        loadIfExists(db);
        cleanupDuplicates(db);
        seedIfEmpty(db);

        // Add shutdown hook to save data on exit (including GUI close)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutdown detected. Saving data...");
            DataStorage.save(UniversityDatabase.getInstance(), DATA_PATH);
        }));

        AuthService authService = new AuthService(db);
        authService.addObserver(db.getLogger());

        while (true) {
            System.out.println("\n===Welcome to WSP-wannabe===");
            System.out.println("1. Login (Console)");
            System.out.println("2. Launch GUI");
            System.out.println("3. View News");
            System.out.println("4. View research projects (sorted alphabetically)");
            System.out.println("5. View research projects (sorted by page number)");
            System.out.println("6. Show top researchers (by H-Index)");
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
                WspGui.start(db);
                System.out.println("GUI launched. Continue using console or close GUI to exit.");
            }
            if (inputInt == 3) {
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
            if (inputInt == 4) {
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
            if (inputInt == 6) {
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
            if (inputInt == 5) {
                List<ResearchProject> projects = db.getResearchProjects();
                if (projects.isEmpty()) {
                    System.out.println("No research projects available.");
                } else {
                    System.out.println("\n--- Research Projects (sorted by paper count) ---");
                    projects.stream()
                            .sorted((p1, p2) -> Integer.compare(p2.getPaperCount(), p1.getPaperCount()))
                            .forEach(p -> System.out.println(p.getName() + " | Papers: " + p.getPaperCount()));
                }
            }
        }
    }

    private static void cleanupDuplicates(UniversityDatabase db) {
        for (User u : db.getUsers()) {
            if (u instanceof Student s) {
                java.util.Map<Course, Mark> markMap = new java.util.LinkedHashMap<>();
                for (Mark m : s.getMarks()) {
                    markMap.put(m.getCourse(), m);
                }
                s.getMarks().clear();
                s.getMarks().addAll(markMap.values());
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
        db.setComplaints(loadedDb.getComplaints());
        db.setRegistrationRequests(loadedDb.getRegistrationRequests());
        db.setMessages(loadedDb.getMessages());
        db.setEmployeeRequests(loadedDb.getEmployeeRequests());
        db.setLessons(loadedDb.getLessons());

        System.out.println("Successfully restored " + db.getUsers().size() + " users.");
    }

    private static void seedIfEmpty(UniversityDatabase db) {
        if (!db.getUsers().isEmpty()) {
            return;
        }

        Admin admin = new Admin("A1", "AdminName", "admin@uni.kz", "admin123");
        Manager manager = new Manager("M1", "ManagerName", "manager@uni.kz", "manager123", ManagerType.OR);
        Course cs101 = new Course("CS101", "Intro to Computer Science", 10, 1);
        Course ma102 = new Course("MA102", "Calculus I", 20, 1);
        Course cs103 = new Course("CS103", "Web Development", 4, 2);
        Course cs201 = new Course("CS201", "OOP", 3, 2);
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
        ResearchEmployee researchEmployee2 = new ResearchEmployee("R2", "Alice", "alice@uni.kz", "alice123", 4);
        ResearchProject project1 = new ResearchProject("data science");
        ResearchProject project2 = new ResearchProject("quantum computing");
        Dean dean = new Dean("D1", "DeanName", "dean@uni.kz", "dean123");
        Rector rector = new Rector("E1", "RectorName", "rector@uni.kz", "rector123");

        db.addUser(admin);
        db.addUser(manager);
        db.addUser(prof);
        db.addUser(lecturer);
        db.addUser(student1);
        db.addUser(student2);
        db.addUser(student3);
        db.addUser(student4);
        db.addUser(researchEmployee);
        db.addUser(researchEmployee2);
        db.addUser(dean);
        db.addUser(rector);
        db.addCourse(cs103);
        db.addCourse(cs101);
        db.addCourse(ma102);
        db.addCourse(cs201);
        db.addResearchProject(project1);
        db.addResearchProject(project2);

        prof.assignCourse(cs201);
        prof.assignCourse(ma102);
        lecturer.assignCourse(cs201);
        lecturer.assignCourse(cs103);

        try {
            student1.requestToEnroll(cs101); // 10
            student1.requestToEnroll(cs103); // 4. Total 14
            student2.requestToEnroll(cs201); // 3
            student2.requestToEnroll(cs101); // 10. Total 13

            db.addLesson(new Lesson(cs101, prof, LessonType.LECTURE, WeekDays.MONDAY, 9, 301));
            db.addLesson(new Lesson(ma102, prof, LessonType.PRACTICE, WeekDays.TUESDAY, 11, 402));
            db.addLesson(new Lesson(cs201, lecturer, LessonType.LECTURE, WeekDays.WEDNESDAY, 14, 205));
            db.addLesson(new Lesson(cs103, lecturer, LessonType.LABORATORY, WeekDays.THURSDAY, 10, 101));

            student4.setSupervisor(researchEmployee);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void openRoleMenu(User user, UniversityDatabase db) {

        if (user.getRole() == UserType.ADMIN) {
            AdminMenu.open((Admin) user, db);
        } else if (user.getRole() == UserType.MANAGER) {
            ManagerMenu.open((Manager) user, db);
        } else if (user.getRole() == UserType.TEACHER || user.getRole() == UserType.TEACHER_RESEARCHER) {
            TeacherMenu.open((Teacher) user, db);
        } else if (user.getRole() == UserType.STUDENT) {
            StudentMenu.open((Student) user, db);
        } else if (user.getRole() == UserType.RESEARCH_EMPLOYEE) {
            ResearchEmployeeMenu.open((ResearchEmployee) user, db);
        } else if (user.getRole() == UserType.STUDENT_RESEARCHER) {
            if (user instanceof ResearcherDecorator decorator && decorator.getInnerUser() instanceof Student) {
                StudentResearcherMenu.open(decorator, db);
            } else {
                throw new IllegalStateException("Invalid STUDENT_RESEARCHER user type: wrapped Student expected.");
            }
        } else if (user.getRole() == UserType.DEAN || user.getRole() == UserType.RECTOR) {
            DeanRectorMenu.open((Employee) user, db);
        }
    }
}
