package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exceptions.LessonTimeConflictException;
import models.*;
import utils.SystemLogger;

public class UniversityDatabase implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final UniversityDatabase instance = new UniversityDatabase();

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<NewsItem> news = new ArrayList<>();
    private final List<Complaint> complaints = new ArrayList<>();
    private final List<RegistrationRequest> registrationRequests = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();
    private final SystemLogger logger = new SystemLogger();
    private final List<Message> messages = new ArrayList<>();
    private final List<EmployeeRequest> employeeRequests = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();

    private UniversityDatabase() {
    }

    public static UniversityDatabase getInstance() {
        return instance;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public List<NewsItem> getNews() {
        return new ArrayList<>(news);
    }

    public List<Complaint> getComplaints() {
        return new ArrayList<>(complaints);
    }

    public List<RegistrationRequest> getRegistrationRequests() {
        return new ArrayList<>(registrationRequests);
    }

    public List<ResearchProject> getResearchProjects() {
        return new ArrayList<>(researchProjects);
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public SystemLogger getLogger() {
        return logger;
    }

    public List<EmployeeRequest> getEmployeeRequests() {
        return new ArrayList<>(employeeRequests);
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    public void addEmployeeRequest(EmployeeRequest request) {
        if (request != null) {
            this.employeeRequests.add(request);
        }
    }

    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
        }
    }

    public void addCourse(Course course) {
        if (course != null) {
            this.courses.add(course);
        }
    }

    public void addMark(Student student, Course course, int mark1, int mark2, int markExam) {
        if (student == null || course == null) {
            return;
        }
        Mark mark = new Mark(mark1, mark2, markExam, course);
        student.addMark(mark);
    }

    public void addNews(NewsItem newsItem) {
        if (newsItem != null) {
            this.news.add(newsItem);
        }
    }

    public void addComplaint(Complaint complaint) {
        if (complaint != null) {
            this.complaints.add(complaint);
        }
    }

    public void addRegistrationRequest(RegistrationRequest request) {
        if (request != null) {
            this.registrationRequests.add(request);
        }
    }

    public void addResearchProject(ResearchProject project) {
        if (project != null) {
            this.researchProjects.add(project);
        }
    }

    public void addMessage(Message message) {
        if (message != null) {
            this.messages.add(message);
        }
    }

    public void addLesson(Lesson lesson) throws LessonTimeConflictException {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson cannot be null.");
        }

        for (Lesson existingLesson : lessons) {
            boolean sameTeacher = existingLesson.getTeacher().getId().equals(lesson.getTeacher().getId());
            boolean sameTime = existingLesson.getDay() == lesson.getDay()
                    && existingLesson.getHour() == lesson.getHour();
            if (sameTeacher && sameTime) {
                throw new LessonTimeConflictException(
                        "Teacher " + lesson.getTeacher().getName() + " already has a lesson on "
                                + lesson.getDay() + " at " + lesson.getHour() + ":00.");
            }
        }

        this.lessons.add(lesson);
        if (!lesson.getCourse().getLessons().contains(lesson)) {
            lesson.getCourse().addLesson(lesson);
        }
    }

    public void updateUser(User updatedUser) {
        if (updatedUser == null) {
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                return;
            }
        }
    }

    public void setUsers(List<User> users) {
        this.users.clear();
        if (users != null) {
            this.users.addAll(users);
        }
    }

    public void setCourses(List<Course> courses) {
        this.courses.clear();
        if (courses != null) {
            this.courses.addAll(courses);
        }
    }

    public void setNews(List<NewsItem> news) {
        this.news.clear();
        if (news != null) {
            this.news.addAll(news);
        }
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints.clear();
        if (complaints != null) {
            this.complaints.addAll(complaints);
        }
    }

    public void setRegistrationRequests(List<RegistrationRequest> registrationRequests) {
        this.registrationRequests.clear();
        if (registrationRequests != null) {
            this.registrationRequests.addAll(registrationRequests);
        }
    }

    public void setResearchProjects(List<ResearchProject> researchProjects) {
        this.researchProjects.clear();
        if (researchProjects != null) {
            this.researchProjects.addAll(researchProjects);
        }
    }

    public void setMessages(List<Message> messages) {
        this.messages.clear();
        if (messages != null) {
            this.messages.addAll(messages);
        }
    }

    public void setEmployeeRequests(List<EmployeeRequest> employeeRequests) {
        this.employeeRequests.clear();
        if (employeeRequests != null) {
            this.employeeRequests.addAll(employeeRequests);
        }
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons.clear();
        if (lessons != null) {
            this.lessons.addAll(lessons);
        }
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    public User findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public Course findCourseByCode(String code) {
        return courses.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
    }

    public ResearchProject findResearchProjectByName(String name) {
        return researchProjects.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Message> getMessagesWithUser(String userId) {
        List<Message> userMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getTo().equals(userId) || message.getFrom().equals(userId)) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    public void removeUser(User user) {
        if (user != null) {
            this.users.remove(user);
        }
    }

    public void removeNews(NewsItem newsItem) {
        if (newsItem != null) {
            this.news.remove(newsItem);
        }
    }
}
