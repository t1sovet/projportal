package models;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;
    private int firstAttestation;
    private int secondAttestation;
    private int finalExam;
    private Course course;

    public Mark(int firstAttestation, int secondAttestation, int finalExam, Course course) {
        if (firstAttestation < 0 || firstAttestation > 30) throw new IllegalArgumentException("First attestation must be between 0 and 30.");
        if (secondAttestation < 0 || secondAttestation > 30) throw new IllegalArgumentException("Second attestation must be between 0 and 30.");
        if (finalExam < 0 || finalExam > 40) throw new IllegalArgumentException("Final exam must be between 0 and 40.");
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
