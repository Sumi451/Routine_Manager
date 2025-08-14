package com.example.routinemanager;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    private Button routineEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Find the button in the layout
        routineEntryButton = findViewById(R.id.routine_entry_button);

        // Set up the click listener to launch the RoutineEntryActivity
        routineEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, RoutineEntryActivity.class);
                startActivity(intent);
            }
        });
    }
}

