package models;

public class Mark {
    private int firstAttestation;
    private int secondAttestation;
    private int finalExam;
    private Course course;

    public Mark(int firstAttestation, int secondAttestation, int finalExam, Course course) {
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
        this.course = course;
    }

    public int getFirstAttestation() {
        return firstAttestation;
    }

    public int getSecondAttestation() {
        return secondAttestation;
    }

    public int getFinalExam() {
        return finalExam;
    }

    public Course getCourse() {
        return course;
    }

    public int getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }
}
