package com.example.routinemanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 1. Data Model (Entity)
 * This class represents a single row in our database table.
 */
@Entity(tableName = "timetable_entries")
public class TimetableEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String subjectName;
    public String professor;
    public String roomNumber;
    public String startTime;
    public String endTime;
    public String dayOfWeek;
    public int dayOrder;

    public TimetableEntry(String subjectName, String professor, String roomNumber,
                          String startTime, String endTime, String dayOfWeek, int dayOrder) {
        this.subjectName = subjectName;
        this.professor = professor;
        this.roomNumber = roomNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.dayOrder = dayOrder;
    }
}

