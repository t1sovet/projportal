package models;

import java.io.Serializable;

public class Complaint implements Serializable {
    private final String header;
    private final String body;

    public Complaint(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

}
