package services;

import java.util.ArrayList;
import java.util.List;
import interfaces.Researcher;
import models.User;
import models.ResearchPaper;
import models.Student;
import enums.UserType;

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

    public User getInnerUser() {
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
                    paper.getTitle() + " by " + paper.getAuthor().getName() + " - Citations: " + paper.getCitations());
        }
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
        updateHIndex();
    }

    private void updateHIndex() {
        List<Integer> citations = researchPapers.stream()
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
    public UserType getRole() {
        if (userToWrap.getRole() == UserType.STUDENT) {
            return UserType.STUDENT_RESEARCHER;
        } else {
            return UserType.TEACHER_RESEARCHER;
        }
    }

}
