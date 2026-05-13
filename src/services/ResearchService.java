package services;

import models.User;

import java.util.Comparator;

import models.ResearchEmployee;
import models.ResearchPaper;
import models.ResearchProject;
import models.Student;

import java.util.List;

import exceptions.NotResearcherException;

public class ResearchService {
    private ResearchService() {
    }

    public static List<ResearchPaper> sortPapersByAuthorName(List<ResearchPaper> papers) {
        papers.sort(Comparator.comparing((ResearchPaper p) -> p.getAuthor().getName()).reversed());
        return papers;
    }

    public static List<ResearchPaper> sortPapersByCitations(List<ResearchPaper> papers) {
        papers.sort(Comparator.comparingInt(ResearchPaper::getCitations).reversed());
        return papers;
    }

    public static void joinResearchProject(User user, ResearchProject project) throws NotResearcherException {
        if (!(user instanceof ResearchEmployee)) {
            throw new NotResearcherException(
                    "Only researchers can join research projects. If you want to become a researcher, send a request to the manager.");
        }
    }

    public static void promoteToResearcher(Student student, String password, UniversityDatabase db) {
        ResearcherDecorator researcher = new ResearcherDecorator((Student) student, password);
        db.updateUser(researcher);
    }
}
