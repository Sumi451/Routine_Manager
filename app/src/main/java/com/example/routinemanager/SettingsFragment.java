package com.example.routinemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.routinemanager.WidgetUpdater;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "RoutineManagerPrefs";
    private static final String KEY_VIEW_TYPE = "view_type";
    private static final String KEY_MAPPING_PREFIX = "day_order_mapping_";

    private RadioGroup radioGroupTimetableView;
    private RadioButton radioButtonWeekly;
    private RadioButton radioButtonDayOrder;
    private Spinner spinnerMonday;
    private Spinner spinnerTuesday;
    private Spinner spinnerWednesday;
    private Spinner spinnerThursday;
    private Spinner spinnerFriday;
    private Button buttonSaveSettings;

    private SharedPreferences sharedPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize UI components
        radioGroupTimetableView = view.findViewById(R.id.radio_group_timetable_view);
        radioButtonWeekly = view.findViewById(R.id.radio_button_weekly);
        radioButtonDayOrder = view.findViewById(R.id.radio_button_day_order);
        spinnerMonday = view.findViewById(R.id.spinner_monday_day_order);
        spinnerTuesday = view.findViewById(R.id.spinner_tuesday_day_order);
        spinnerWednesday = view.findViewById(R.id.spinner_wednesday_day_order);
        spinnerThursday = view.findViewById(R.id.spinner_thursday_day_order);
        spinnerFriday = view.findViewById(R.id.spinner_friday_day_order);
        buttonSaveSettings = view.findViewById(R.id.button_save_settings);

        // Set up spinners with the day order array
        ArrayAdapter<CharSequence> dayOrderAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.day_orders, android.R.layout.simple_spinner_item);
        dayOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonday.setAdapter(dayOrderAdapter);
        spinnerTuesday.setAdapter(dayOrderAdapter);
        spinnerWednesday.setAdapter(dayOrderAdapter);
        spinnerThursday.setAdapter(dayOrderAdapter);
        spinnerFriday.setAdapter(dayOrderAdapter);

        // Load and set the current settings
        loadSettings();

        // Save settings on button click
        buttonSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        // Load the saved view type
        String savedViewType = sharedPreferences.getString(KEY_VIEW_TYPE, "weekly");
        if (savedViewType.equals("weekly")) {
            radioButtonWeekly.setChecked(true);
        } else {
            radioButtonDayOrder.setChecked(true);
        }

        // Load the saved day order mappings
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
        Spinner[] spinners = {spinnerMonday, spinnerTuesday, spinnerWednesday, spinnerThursday, spinnerFriday};
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerMonday.getAdapter();

        for (int i = 0; i < days.length; i++) {
            String day = days[i];
            Spinner spinner = spinners[i];
            int savedDayOrder = sharedPreferences.getInt(KEY_MAPPING_PREFIX + day, i + 1); // Default to Day 1, Day 2, etc.

            // Find the position of the saved day order string in the adapter
            String dayOrderString = "Day " + savedDayOrder;
            if (adapter != null) {
                int position = adapter.getPosition(dayOrderString);
                if (position >= 0) {
                    spinner.setSelection(position);
                }
            }
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the selected view type
        if (radioButtonWeekly.isChecked()) {
            editor.putString(KEY_VIEW_TYPE, "weekly");
        } else {
            editor.putString(KEY_VIEW_TYPE, "day_order");
        }

        // Save the day order mappings
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
        Spinner[] spinners = {spinnerMonday, spinnerTuesday, spinnerWednesday, spinnerThursday, spinnerFriday};
        for (int i = 0; i < days.length; i++) {
            String day = days[i];
            Spinner spinner = spinners[i];
            String selectedDayOrderStr = spinner.getSelectedItem().toString();
            int selectedDayOrder = Integer.parseInt(selectedDayOrderStr.replace("Day ", ""));
            editor.putInt(KEY_MAPPING_PREFIX + day, selectedDayOrder);
        }

        editor.apply();

        Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show();

        // Trigger a widget update to reflect the new settings immediately
        WidgetUpdater.updateWidget(requireContext());

        // Return to the previous fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
