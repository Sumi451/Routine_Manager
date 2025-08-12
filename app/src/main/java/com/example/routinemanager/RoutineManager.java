package com.example.routinemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class RoutineManager {

    private static final String PREFS_NAME = "RoutinePrefs";
    private static final String PREF_ROUTINE_KEY = "routine_key";
    private Context context;

    public RoutineManager(Context context) {
        this.context = context;
    }

    public void saveRoutineEntry(String subject, String time) {
        // Validate input fields
        if (subject.isEmpty() || time.isEmpty()) {
            Toast.makeText(context, "Please enter both subject and time", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Set<String> routineSet = sharedPreferences.getStringSet(PREF_ROUTINE_KEY, new HashSet<String>());

        Set<String> updatedRoutineSet = new HashSet<>(routineSet);

        String newEntry = time + ":" + subject;

        updatedRoutineSet.add(newEntry);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_ROUTINE_KEY, updatedRoutineSet);
        editor.apply();

        Toast.makeText(context, "Routine entry saved!", Toast.LENGTH_SHORT).show();
    }
}
