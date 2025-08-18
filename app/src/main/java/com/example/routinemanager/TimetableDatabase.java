package com.example.routinemanager;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.routinemanager.TimetableEntry; // Replace with your package name
import com.example.routinemanager.TimetableDao; // Replace with your package name

/**
 * 3. Room Database
 * This is the main database class. It defines the tables and version.
 */
@Database(entities = {TimetableEntry.class}, version = 1)
public abstract class TimetableDatabase extends RoomDatabase {
    public abstract TimetableDao timetableDao();

    private static volatile TimetableDatabase INSTANCE;

    public static TimetableDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TimetableDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TimetableDatabase.class, "timetable_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

