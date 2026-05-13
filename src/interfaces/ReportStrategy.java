package interfaces;

import models.Course;
import java.util.List;

public interface ReportStrategy {
    void generateReport(Course course);
}