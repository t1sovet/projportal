package models;

import enums.LessonType;
import enums.WeekDays;
import java.io.Serializable;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Course course;
    private final Teacher teacher;
    private final LessonType lessonType;
    private final WeekDays day;
    private final int hour;
    private int roomNumber;

    public Lesson(Course course, Teacher teacher, LessonType lessonType, WeekDays day, int hour, int roomNumber) {
        if (hour < 8 || hour > 20) throw new IllegalArgumentException("Hour must be between 8 and 20.");
        if (roomNumber < 1 || roomNumber > 999) throw new IllegalArgumentException("Room must be between 1 and 999.");
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
