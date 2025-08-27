package com.example.routinemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.routinemanager.TimetableAdapter;
import com.example.routinemanager.TimetableViewModel;
import com.example.routinemanager.TimetableViewModelFactory;
import com.example.routinemanager.TimetableRepository;
import com.google.android.material.tabs.TabLayout;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment to display the list of timetable entries.
 */
public class TimetableListFragment extends Fragment implements TimetableAdapter.OnItemClickListener{

    private TimetableViewModel timetableViewModel;
    private RecyclerView recyclerView;
    private TimetableAdapter adapter;
    private TabLayout tabLayout;

    private static final String PREFS_NAME = "RoutineManagerPrefs";
    private static final String KEY_VIEW_TYPE = "view_type";
    private static final String KEY_MAPPING_PREFIX = "day_order_mapping_";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the new layout that contains the RecyclerView and TabLayout
        return inflater.inflate(R.layout.fragment_timetable_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.timetable_recycler_view);
        tabLayout = view.findViewById(R.id.tab_layout_days);
        adapter = new TimetableAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add a LayoutAnimationController to animate the RecyclerView
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        recyclerView.setLayoutAnimation(animation);

        // Get the ViewModel from the Activity's scope
        TimetableRepository repository = new TimetableRepository(requireActivity().getApplication());
        TimetableViewModelFactory factory = new TimetableViewModelFactory(repository);
        timetableViewModel = new ViewModelProvider(requireActivity(), factory).get(TimetableViewModel.class);

        // Get view preference from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String viewType = sharedPreferences.getString(KEY_VIEW_TYPE, "weekly");

        setupTabLayout(viewType);

        // Observe the LiveData
        timetableViewModel.getAllEntries().observe(getViewLifecycleOwner(), allEntries -> {
            Log.d("TimetableListFragment", "Timetable entries received: " + allEntries.size());
            updateTimetableView(allEntries, viewType);
        });

        // Add a listener for tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (timetableViewModel.getAllEntries().getValue() != null) {
                    updateTimetableView(timetableViewModel.getAllEntries().getValue(), viewType);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupTabLayout(String viewType) {
        tabLayout.removeAllTabs();
        if (viewType.equals("weekly")) {
            String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
            for (String day : daysOfWeek) {
                tabLayout.addTab(tabLayout.newTab().setText(day.substring(0, 3)));
            }
        } else {
            String[] dayOrders = getResources().getStringArray(R.array.day_orders);
            for (String dayOrder : dayOrders) {
                tabLayout.addTab(tabLayout.newTab().setText(dayOrder));
            }
        }
    }

    private void updateTimetableView(List<TimetableEntry> allEntries, String viewType) {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        if (selectedTabPosition == TabLayout.Tab.INVALID_POSITION) {
            return;
        }

        String selectedTitle = (String) tabLayout.getTabAt(selectedTabPosition).getText();
        List<TimetableEntry> filteredEntries;

        if (viewType.equals("weekly")) {
            String fullDayName = getFullDayNameFromAbbreviation(selectedTitle);
            filteredEntries = allEntries.stream()
                    .filter(entry -> entry.dayOfWeek != null && entry.dayOfWeek.equals(fullDayName))
                    .collect(Collectors.toList());
        } else {
            // Day Order View
            int dayOrder = Integer.parseInt(selectedTitle.replace("Day ", ""));
            filteredEntries = allEntries.stream()
                    .filter(entry -> entry.dayOrder == dayOrder)
                    .collect(Collectors.toList());
        }
        adapter.setTimetableEntries(filteredEntries);
    }

    private String getFullDayNameFromAbbreviation(String abbreviation) {
        String[] fullDays = getResources().getStringArray(R.array.days_of_week);
        for (String day : fullDays) {
            if (day.substring(0, 3).equalsIgnoreCase(abbreviation)) {
                return day;
            }
        }
        return "";
    }


    @Override
    public void onItemClick(TimetableEntry entry) {
        // Create a Bundle to pass the entry ID
        Bundle bundle = new Bundle();
        bundle.putInt("ENTRY_ID", entry.id);

        // Create the fragment and set the arguments
        AddEditTimetableFragment fragment = new AddEditTimetableFragment();
        fragment.setArguments(bundle);

        // Navigate to the edit screen
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.timetable_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
