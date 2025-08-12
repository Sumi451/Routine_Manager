package com.example.routinemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText subjectInput;
    private EditText timeInput;
    private Button saveButton;
    private RoutineManager routineManager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjectInput = findViewById(R.id.subject_input);
        timeInput = findViewById(R.id.time_input);
        saveButton = findViewById(R.id.save_button);

        routineManager = new RoutineManager(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectInput.getText().toString().trim();
                String time = timeInput.getText().toString().trim();

                // Call the save method from our new RoutineManager
                routineManager.saveRoutineEntry(subject, time);

                // Clear the input fields for the next entry
                subjectInput.setText("");
                timeInput.setText("");
            }
        });

    }
}