package com.example.routinemanager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.routinemanager.TimetableEntry;
import com.example.routinemanager.TimetableViewModel;
import java.util.Calendar;

public class AddEditTimetableFragment extends Fragment {

    private EditText editTextSubjectName;
    private EditText editTextProfessor;
    private EditText editTextRoomNumber;
    private Button buttonStartTime;
    private Button buttonEndTime;
    private Spinner spinnerDayOfWeek;
    private Button buttonSave;
    private Button buttonDelete;

    private TimetableViewModel timetableViewModel;
    private int entryId = -1; // -1 indicates a new entry, otherwise it's an existing one.

    public AddEditTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        editTextSubjectName = view.findViewById(R.id.edit_text_subject_name);
        editTextProfessor = view.findViewById(R.id.edit_text_professor);
        editTextRoomNumber = view.findViewById(R.id.edit_text_room_number);
        buttonStartTime = view.findViewById(R.id.button_start_time);
        buttonEndTime = view.findViewById(R.id.button_end_time);
        spinnerDayOfWeek = view.findViewById(R.id.spinner_day_of_week);
        buttonSave = view.findViewById(R.id.button_save);
        buttonDelete = view.findViewById(R.id.button_delete);

        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);

        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.days_of_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(adapter);

        // Time Picker Logic
        buttonStartTime.setOnClickListener(v -> showTimePicker(true));
        buttonEndTime.setOnClickListener(v -> showTimePicker(false));

        // Save Button Logic
        buttonSave.setOnClickListener(v -> saveTimetableEntry());

        // Check for arguments to determine if we are editing
        if (getArguments() != null) {
            entryId = getArguments().getInt("ENTRY_ID", -1);
            if (entryId != -1) {
                timetableViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), entry -> {
                    if (entry != null) {
                        populateFields(entry);
                        buttonDelete.setVisibility(View.VISIBLE);
                    }
                });

                // Set up the Delete button's click listener
                buttonDelete.setOnClickListener(v -> deleteTimetableEntry());
            }
        }
    }

    private void showTimePicker(boolean isStartTime) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    if (isStartTime) {
                        buttonStartTime.setText(time);
                    } else {
                        buttonEndTime.setText(time);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void populateFields(TimetableEntry entry) {
        editTextSubjectName.setText(entry.subjectName);
        editTextProfessor.setText(entry.professor);
        editTextRoomNumber.setText(entry.roomNumber);
        buttonStartTime.setText(entry.startTime);
        buttonEndTime.setText(entry.endTime);
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerDayOfWeek.getAdapter();
        if (adapter != null) {
            int spinnerPosition = adapter.getPosition(entry.dayOfWeek);
            spinnerDayOfWeek.setSelection(spinnerPosition);
        }
    }

    private void saveTimetableEntry() {
        String subjectName = editTextSubjectName.getText().toString().trim();
        String professor = editTextProfessor.getText().toString().trim();
        String roomNumber = editTextRoomNumber.getText().toString().trim();
        String startTime = buttonStartTime.getText().toString();
        String endTime = buttonEndTime.getText().toString();
        String dayOfWeek = spinnerDayOfWeek.getSelectedItem().toString();

        if (subjectName.isEmpty() || startTime.equals("Select Time") || endTime.equals("Select Time")) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        TimetableEntry entry = new TimetableEntry(subjectName, professor, roomNumber, startTime, endTime, dayOfWeek, 0);
        if (entryId == -1) {
            timetableViewModel.insert(entry);
            Toast.makeText(requireContext(), "Entry added!", Toast.LENGTH_SHORT).show();
        } else {
            entry.id = entryId;
            timetableViewModel.update(entry);
            Toast.makeText(requireContext(), "Entry updated!", Toast.LENGTH_SHORT).show();
        }

        // Trigger a widget update
        WidgetUpdater.updateWidget(requireContext());

        // Return to the previous fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void deleteTimetableEntry() {
        if (entryId != -1) {
            TimetableEntry entryToDelete = new TimetableEntry("", "", "", "", "", "", 0);
            entryToDelete.id = entryId;
            timetableViewModel.delete(entryToDelete);
            Toast.makeText(requireContext(), "Entry deleted!", Toast.LENGTH_SHORT).show();

            // Trigger a widget update
            WidgetUpdater.updateWidget(requireContext());

            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
