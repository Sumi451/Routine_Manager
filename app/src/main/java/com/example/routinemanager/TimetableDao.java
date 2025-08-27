package com.example.routinemanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;
import com.example.routinemanager.TimetableEntry; // Replace with your package name

/**
 * 2. Data Access Object (DAO)
 * This interface contains methods for accessing the database. Room generates the code for this.
 */
@Dao
public interface TimetableDao {
    @Query("SELECT * FROM timetable_entries ORDER BY startTime ASC")
    LiveData<List<TimetableEntry>> getAllEntries();


    @Query("SELECT * FROM timetable_entries WHERE dayOfWeek = :day ORDER BY startTime ASC")
    LiveData<List<TimetableEntry>> getEntriesByDay(String day);

    @Query("SELECT * FROM timetable_entries WHERE dayOrder = :dayOrder ORDER BY startTime ASC")
    LiveData<List<TimetableEntry>> getEntriesByDayOrder(int dayOrder);

    @Query("SELECT * FROM timetable_entries WHERE id = :id")
    LiveData<TimetableEntry> getEntryById(int id);

    // Synchronous queries for the widget
    @Query("SELECT * FROM timetable_entries WHERE dayOfWeek = :day ORDER BY startTime ASC")
    List<TimetableEntry> getEntriesByDaySync(String day);

    // NEW: Synchronous query for the widget to get entries by day order
    @Query("SELECT * FROM timetable_entries WHERE dayOrder = :dayOrder ORDER BY startTime ASC")
    List<TimetableEntry> getEntriesByDayOrderSync(int dayOrder);

    @Insert
    void insert(TimetableEntry entry);

    @Update
    void update(TimetableEntry entry);

    @Delete
    void delete(TimetableEntry entry);
}
