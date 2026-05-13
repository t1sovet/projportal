package models;

import java.io.Serializable;

public class ResearchPaper implements Serializable {
    private String title;
    private int year;
    private int citations;

    public ResearchPaper(String title, int year, int citations, int pages) {
        this.title = title;
        this.year = year;
        this.citations = citations;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getCitations() {
        return citations;
    }
}
