package com.example.routinemanager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
    private Spinner spinnerDaySelection;
    private RadioGroup radioGroupDayType;
    private TextView textViewDayLabel;
    private RadioButton radioButtonDay;
    private RadioButton radioButtonDayOrder;
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
        // Inflate the layout for this fragment
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
        spinnerDaySelection = view.findViewById(R.id.spinner_day_selection);
        radioGroupDayType = view.findViewById(R.id.radio_group_day_type);
        textViewDayLabel = view.findViewById(R.id.text_view_day_label);
        radioButtonDay = view.findViewById(R.id.radio_button_day);
        radioButtonDayOrder = view.findViewById(R.id.radio_button_day_order);
        buttonSave = view.findViewById(R.id.button_save);
        buttonDelete = view.findViewById(R.id.button_delete);

        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);

        // Set up the initial spinner adapter for Day of Week
        ArrayAdapter<CharSequence> dayOfWeekAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayOfWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaySelection.setAdapter(dayOfWeekAdapter);

        // Add a listener to the radio group to switch the spinner content
        radioGroupDayType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_button_day) {
                textViewDayLabel.setText("Day of Week");
                spinnerDaySelection.setAdapter(dayOfWeekAdapter);
            } else if (checkedId == R.id.radio_button_day_order) {
                textViewDayLabel.setText("Day Order");
                ArrayAdapter<CharSequence> dayOrderAdapter = ArrayAdapter.createFromResource(
                        requireContext(), R.array.day_orders, android.R.layout.simple_spinner_item);
                dayOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDaySelection.setAdapter(dayOrderAdapter);
            }
        });

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

        // Populate based on whether the entry has a day of week or day order
        if (entry.dayOfWeek != null && !entry.dayOfWeek.isEmpty()) {
            radioButtonDay.setChecked(true);
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerDaySelection.getAdapter();
            if (adapter != null) {
                int spinnerPosition = adapter.getPosition(entry.dayOfWeek);
                spinnerDaySelection.setSelection(spinnerPosition);
            }
        } else {
            radioButtonDayOrder.setChecked(true);
            ArrayAdapter<CharSequence> dayOrderAdapter = ArrayAdapter.createFromResource(
                    requireContext(), R.array.day_orders, android.R.layout.simple_spinner_item);
            dayOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDaySelection.setAdapter(dayOrderAdapter);
            // Day order is an integer, so we need to find its string representation
            String dayOrderString = "Day " + entry.dayOrder;
            int spinnerPosition = dayOrderAdapter.getPosition(dayOrderString);
            spinnerDaySelection.setSelection(spinnerPosition);
        }
    }

    private void saveTimetableEntry() {
        String subjectName = editTextSubjectName.getText().toString().trim();
        String professor = editTextProfessor.getText().toString().trim();
        String roomNumber = editTextRoomNumber.getText().toString().trim();
        String startTime = buttonStartTime.getText().toString();
        String endTime = buttonEndTime.getText().toString();

        String dayOfWeek = null;
        int dayOrder = 0;

        if (radioButtonDay.isChecked()) {
            dayOfWeek = spinnerDaySelection.getSelectedItem().toString();
        } else {
            String selectedDayOrderStr = spinnerDaySelection.getSelectedItem().toString();
            // Extract the number from the string "Day X"
            dayOrder = Integer.parseInt(selectedDayOrderStr.replace("Day ", ""));
        }

        if (subjectName.isEmpty() || startTime.equals("Select Time") || endTime.equals("Select Time")) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        TimetableEntry entry = new TimetableEntry(subjectName, professor, roomNumber, startTime, endTime, dayOfWeek, dayOrder);
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
