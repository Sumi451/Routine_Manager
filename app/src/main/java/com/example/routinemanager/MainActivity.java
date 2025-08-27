package com.example.routinemanager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * 7. Main Activity
 * The main UI of the app.
 */
public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab_add_entry);

        // Load the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.timetable_fragment_container, new TimetableListFragment())
                    .commit();
        }

        // Find the FAB and set a click listener
        fab.setOnClickListener(v -> {
            // Use a FragmentTransaction to replace the current view with the Add/Edit fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.timetable_fragment_container, new AddEditTimetableFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Add a listener to handle fragment changes and update FAB visibility
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.timetable_fragment_container);
            if (currentFragment instanceof TimetableListFragment) {
                // Show the FAB on the main list screen
                fab.show();
            } else {
                // Hide the FAB on all other screens (like Add/Edit)
                fab.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            // Open the SettingsFragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.timetable_fragment_container, new SettingsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
