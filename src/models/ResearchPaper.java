package models;

import java.io.Serializable;

public class ResearchPaper implements Serializable {
    private String title;
    private User author;
    private int citations;

    public ResearchPaper(String title, User author, int citations) {
        this.title = title;
        this.author = author;
        this.citations = citations;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }

    public int getCitations() {
        return citations;
    }
}
