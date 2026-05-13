package models;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class ResearchProject implements Serializable {
    public final String name;
    private final List<ResearchPaper> papers = new ArrayList<>();

    public ResearchProject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPaperCount() {
        return this.papers.size();
    }

    public void addPaper(ResearchPaper paper) {
        this.papers.add(paper);
    }

    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }
}
