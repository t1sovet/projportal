package services;

import java.util.ArrayList;
import java.util.List;
import interfaces.Researcher;
import models.User;
import models.ResearchPaper;
import models.Student;

public class ResearcherDecorator extends User implements Researcher {
    private static final long serialVersionUID = 1L;

    private final User userToWrap;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private int hIndex;

    public ResearcherDecorator(Student student, String password) {
        super(student.getId(), student.getName(), student.getEmail(), password);
        this.userToWrap = student;
        this.hIndex = 0;
    }

    @Override
    public int getHIndex() {
        return hIndex;
    }

    public User getWrappedUser() {
        return userToWrap;
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    public void printPapers(java.util.Comparator<ResearchPaper> c) {
        researchPapers.sort(c);
        for (ResearchPaper paper : researchPapers) {
            System.out.println(
                    paper.getTitle() + " (" + paper.getYear() + ") - Citations: " + paper.getCitations() + ")");
        }
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
        if (paper.getCitations() > hIndex) {
            hIndex = paper.getCitations();
        }
    }

    @Override
    public String getRole() {
        return userToWrap.getRole() + " & Researcher";
    }

}
