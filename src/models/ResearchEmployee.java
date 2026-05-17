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
        updateHIndex();
    }

    private void updateHIndex() {
        List<Integer> citations = papers.stream()
                .map(ResearchPaper::getCitations)
                .sorted((a, b) -> b - a)
                .toList();
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        this.hIndex = h;
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
