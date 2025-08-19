package com.example.routinemanager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;
import com.example.routinemanager.TimetableRepository; // Replace with your package name
import com.example.routinemanager.TimetableViewModel; // Replace with your package name
import com.example.routinemanager.TimetableViewModelFactory; // Replace with your package name
import com.example.routinemanager.TimetableAdapter; // Replace with your package name
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * 7. Main Activity
 * The main UI of the app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.timetable_fragment_container, new TimetableListFragment())
                    .commit();
        }

        // Find the FAB and set a click listener
        FloatingActionButton fab = findViewById(R.id.fab_add_entry);
        fab.setOnClickListener(v -> {
            // Use a FragmentTransaction to replace the current view with the Add/Edit fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.timetable_fragment_container, new AddEditTimetableFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}