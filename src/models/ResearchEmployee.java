package models;

import java.util.ArrayList;
import java.util.List;
import enums.UserType;

import interfaces.Researcher;

public class ResearchEmployee extends Employee implements Researcher {
    private int hIndex;
    private final List<ResearchPaper> papers = new ArrayList<>();

    public ResearchEmployee(String id, String name, String email, String password, int hIndex) {
        super(id, name, email, password);
        this.hIndex = hIndex;
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(papers);
    }

    @Override
    public int getHIndex() {
        return hIndex;
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        this.papers.add(paper);
        int citations = paper.getCitations();
        if (citations > hIndex) {
            hIndex = citations;
        }
    }

    @Override
    public void printPapers(java.util.Comparator<ResearchPaper> c) {
        papers.sort(c);
        for (ResearchPaper paper : papers) {
            System.out.println(
                    paper.getTitle() + " by " + paper.getAuthor().getName() + " - Citations: " + paper.getCitations());
        }
    }

    @Override
    public UserType getRole() {
        return UserType.RESEARCH_EMPLOYEE;
    }

}
