package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String code;
    private final String name;
    private final int credits;
    private final int yearRequired;
    private final List<Lesson> lessons = new ArrayList<>();

    public Course(String code, String name, int credits, int yearRequired) {
        if (credits < 1 || credits > 21) throw new IllegalArgumentException("Credits must be between 1 and 21.");
        if (yearRequired < 1 || yearRequired > 4) throw new IllegalArgumentException("Year must be between 1 and 4.");
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.yearRequired = yearRequired;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getYearRequired() {
        return yearRequired;
    }

    public int getCredits() {
        return credits;
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return code != null ? code.equals(course.code) : course.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + credits + " credits)";
    }
}
