package com.example.routinemanager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.routinemanager.TimetableRepository; // Replace with your package name
import com.example.routinemanager.TimetableViewModel; // Replace with your package name
import com.example.routinemanager.TimetableViewModelFactory; // Replace with your package name
import com.example.routinemanager.TimetableAdapter; // Replace with your package name

import java.util.List;
import java.util.concurrent.Executors;

/**
 * 7. Main Activity
 * The main UI of the app.
 */
public class MainActivity extends AppCompatActivity {

    private TimetableViewModel timetableViewModel;
    private RecyclerView recyclerView;
    private TimetableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.timetable_recycler_view);
        adapter = new TimetableAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the ViewModel
        TimetableRepository repository = new TimetableRepository(getApplication());
        TimetableViewModelFactory factory = new TimetableViewModelFactory(repository);
        timetableViewModel = new ViewModelProvider(this, factory).get(TimetableViewModel.class);

        // Observe the LiveData
        timetableViewModel.getAllEntries().observe(this, entries -> {
            // Update the UI with the new list of entries
            Log.d("MainActivity", "Timetable entries received: " + entries.size());
            adapter.setTimetableEntries(entries);
        });
        //insertDummyData();
    }
    /**
     * Inserts dummy data into the database if it's empty.
     * This is a temporary method for testing purposes.
     */
    /*
    private void insertDummyData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Get all entries from the database to check if it's empty
            List<TimetableEntry> existingEntries = timetableViewModel.getAllEntries().getValue();

            // If the list is null or empty, insert the dummy data
            if (existingEntries == null || existingEntries.isEmpty()) {
                Log.d("MainActivity", "Inserting dummy data...");
                timetableViewModel.insert(new TimetableEntry("Data Structures", "Prof. Smith", "C-201", "09:00 AM", "10:00 AM", "Monday", 1));
                timetableViewModel.insert(new TimetableEntry("Operating Systems", "Prof. Johnson", "B-105", "11:00 AM", "12:00 PM", "Monday", 1));
                timetableViewModel.insert(new TimetableEntry("Discrete Mathematics", "Prof. Davis", "A-302", "01:00 PM", "02:00 PM", "Tuesday", 2));
            } else {
                Log.d("MainActivity", "Database already contains data, skipping dummy data insertion.");
            }
        });
    } */
}
