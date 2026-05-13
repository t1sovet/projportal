package utils;

import java.util.List;

import interfaces.ReportStrategy;
import models.Course;
import models.Student;
import models.Mark;
import services.UniversityDatabase;

public class SimpleStatisticsStrategy implements ReportStrategy {
    @Override
    public void generateReport(Course course) {
        System.out.println("--- Report for: " + course.getName() + " ---");

        List<Student> students = UniversityDatabase.getInstance().getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .filter(s -> s.getEnrolledCourses().contains(course))
                .toList();

        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }

        double totalSum = 0;
        int count = 0;

        for (Student s : students) {
            for (Mark m : s.getMarks()) {
                if (m.getCourse().equals(course)) {
                    totalSum += m.getTotal();
                    count++;
                }
            }
        }

        if (count > 0) {
            System.out.println("Average Score: " + (totalSum / count));
            System.out.println("Total Students Graded: " + count);
        } else {
            System.out.println("No marks have been entered yet.");
        }
    }
}