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

    public static List<ResearchPaper> sortPapersByDate(List<ResearchPaper> papers) {
        papers.sort(Comparator.comparingInt(ResearchPaper::getYear).reversed());
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

    public static User promoteToResearcher(Student student, String password) {
        ResearcherDecorator researcher = new ResearcherDecorator(student, password);
        List<User> allUsers = UniversityDatabase.getInstance().getUsers();
        int index = allUsers.indexOf(student);
        if (index != -1) {
            allUsers.set(index, researcher);
        }
        return researcher;
    }
}
