package interfaces;

import java.util.Comparator;
import java.util.List;
import models.ResearchPaper;

public interface Researcher {
    int getHIndex();

    List<ResearchPaper> getResearchPapers();

    void addResearchPaper(ResearchPaper paper);

    void printPapers(Comparator<ResearchPaper> c);
}
