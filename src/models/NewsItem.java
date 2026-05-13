package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NewsItem implements Serializable {
    private final String title;
    private final String body;
    private final LocalDateTime createdAt;

    public NewsItem(String title, String body) {
        this.title = title;
        this.body = body;
        this.createdAt = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "[" + createdAt + "] " + title + " - " + body;
    }
}
