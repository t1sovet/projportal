package utils;

import java.util.List;
import java.util.Locale;

import interfaces.ReportStrategy;
import models.Course;
import models.Mark;
import models.Student;
import services.UniversityDatabase;

public class SimpleStatisticsStrategy implements ReportStrategy {
    @Override
    public void generateReport(Course course) {
        if (course == null) {
            System.out.println("Cannot generate report: course is null.");
            return;
        }

        System.out.println("--- Course Report ---");
        System.out.println("Code: " + course.getCode());
        System.out.println("Name: " + course.getName());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Required Year: " + course.getYearRequired());

        List<Student> students = UniversityDatabase.getInstance().getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .filter(s -> s.getEnrolledCourses().contains(course))
                .toList();

        System.out.println("Enrolled Students: " + students.size());

        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }

        int gradedStudents = 0;
        int passedStudents = 0;
        int sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (Student s : students) {
            for (Mark m : s.getMarks()) {
                if (m.getCourse().equals(course)) {
                    int total = m.getTotal();
                    sum += total;
                    min = Math.min(min, total);
                    max = Math.max(max, total);
                    gradedStudents++;
                    if (total >= 50) {
                        passedStudents++;
                    }
                    break;
                }
            }
        }

        if (gradedStudents > 0) {
            double average = sum / (double) gradedStudents;
            double passRate = passedStudents * 100.0 / gradedStudents;

            System.out.println("Graded Students: " + gradedStudents);
            System.out.println("Average Score: " + String.format(Locale.US, "%.2f", average));
            System.out.println("Highest Score: " + max);
            System.out.println("Lowest Score: " + min);
            System.out.println("Pass Rate: " + String.format(Locale.US, "%.2f%%", passRate));
        } else {
            System.out.println("No marks have been entered yet.");
        }
    }
}