package com.example.routinemanager;
// RoutineEntryActivity.java

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

public class RoutineEntryActivity extends AppCompatActivity {

    private EditText subjectInput;
    private TextView timeInput;
    private Button saveButton;
    private RoutineManager routineManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_entry);

        // Initialize UI components
        subjectInput = findViewById(R.id.subject_input);
        timeInput = findViewById(R.id.time_input);
        saveButton = findViewById(R.id.save_button);

        // Initialize our new RoutineManager class
        routineManager = new RoutineManager(this);

        timeInput.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showTimePickerDialog();
            }
        });

        // Set up the save button click listener
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
    private void showTimePickerDialog()
    {
        final Calendar c= Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Update the TextView with the selected time
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                timeInput.setText(formattedTime);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
}

