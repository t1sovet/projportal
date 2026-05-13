package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.*;
import utils.SystemLogger;

public class UniversityDatabase implements Serializable {
    private static final UniversityDatabase instance = new UniversityDatabase();

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<NewsItem> news = new ArrayList<>();
    private final List<Complaint> complaints = new ArrayList<>();
    private final List<RegistrationRequest> registrationRequests = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();
    private final SystemLogger logger = new SystemLogger();
    private final List<Message> messages = new ArrayList<>();

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

    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    public User findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public Course findCourseByCode(String code) {
        return courses.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
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
}
