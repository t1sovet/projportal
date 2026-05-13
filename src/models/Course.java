package models;

import java.io.Serializable;

public class Course implements Serializable {
    private final String code;
    private final String name;
    private final int credits;
    private final int yearRequired;

    public Course(String code, String name, int credits, int yearRequired) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.yearRequired = yearRequired;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getYearRequired() {
        return yearRequired;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + credits + " credits)";
    }
}
