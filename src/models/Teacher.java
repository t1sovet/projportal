package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import enums.TeacherTitle;
import exceptions.CourseNotTaughtException;
import exceptions.StudentNotEnrolledException;
import interfaces.Researcher;

public class Teacher extends Employee implements Researcher {
    private TeacherTitle title;
    private int hIndex;
    private boolean researcher;
    private final List<Course> teachingCourses = new ArrayList<>();
    private final List<Integer> ratings = new ArrayList<>();
    private final List<ResearchPaper> researchPapers = new ArrayList<>();

    public Teacher(String id, String name, String email, String password, TeacherTitle title,
            int hIndex) {
        super(id, name, email, password);
        this.title = title;
        this.hIndex = hIndex;
        if (title == TeacherTitle.PROFESSOR) {
            this.researcher = true;
        } else {
            this.researcher = false;
        }
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public boolean isResearcher() {
        return researcher;
    }

    public int getHIndex() {
        return hIndex;
    }

    public List<Course> getTeachingCourses() {
        return new ArrayList<>(teachingCourses);
    }

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        ratings.add(rating);
    }

    public int getAverageRating() {
        if (ratings.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }
        return sum / ratings.size();
    }

    public void assignCourse(Course course) {
        teachingCourses.add(course);
    }

    public void putMark(Course course, Student student, int m1, int m2, int mEx)
            throws CourseNotTaughtException, StudentNotEnrolledException {

        if (!teachingCourses.contains(course)) {
            throw new CourseNotTaughtException("You don't teach " + course.getName());
        }
        if (!student.getEnrolledCourses().contains(course)) {
            throw new StudentNotEnrolledException(student.getName() + " is not in this course.");
        }

        Mark newMark = new Mark(m1, m2, mEx, course);
        student.addMark(newMark);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        if (!researcher) {
            throw new UnsupportedOperationException("Only professors can have research papers.");
        }
        return new ArrayList<>(researchPapers);
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!researcher) {
            throw new UnsupportedOperationException("Only professors can add research papers.");
        }
        researchPapers.add(paper);
        if (paper.getCitations() > hIndex) {
            hIndex = paper.getCitations();
            if (hIndex > 10) {
                this.title = TeacherTitle.PROFESSOR;
                this.researcher = true;
            }
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        if (!researcher) {
            throw new UnsupportedOperationException("Only professors can print research papers.");
        }
        researchPapers.sort(c);
        for (ResearchPaper paper : researchPapers) {
            System.out.println(
                    paper.getTitle() + " (" + paper.getYear() + ") - Citations: " + paper.getCitations() + ")");
        }
    }

}