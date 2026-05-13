package models;

import enums.LessonType;
import enums.WeekDays;

public class Lesson {
    private final Course course;
    private final Teacher teacher;
    private final LessonType lessonType;
    private final WeekDays day;
    private final int hour;
    private int roomNumber;

    public Lesson(Course course, Teacher teacher, LessonType lessonType, WeekDays day, int hour, int roomNumber) {
        this.course = course;
        this.teacher = teacher;
        this.lessonType = lessonType;
        this.day = day;
        this.hour = hour;
        this.roomNumber = roomNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getHour() {
        return hour;
    }

    public WeekDays getDay() {
        return day;
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
