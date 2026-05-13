package models;

import enums.LessonType;

public class Lesson {
    private final Course course;
    private final Teacher teacher;
    private final LessonType lessonType;
    private int roomNumber;

    public Lesson(Course course, Teacher teacher, LessonType lessonType, int roomNumber) {
        this.course = course;
        this.teacher = teacher;
        this.lessonType = lessonType;
        this.roomNumber = roomNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Course getCourse() {
        return course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public LessonType getLessonType() {
        return lessonType;
    }
}
