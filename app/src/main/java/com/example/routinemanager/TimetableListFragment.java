package com.example.routinemanager;

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

/**
 * Fragment to display the list of timetable entries.
 */
public class TimetableListFragment extends Fragment implements TimetableAdapter.OnItemClickListener{

    private TimetableViewModel timetableViewModel;
    private RecyclerView recyclerView;
    private TimetableAdapter adapter;

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
        adapter = new TimetableAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the ViewModel from the Activity's scope
        TimetableRepository repository = new TimetableRepository(requireActivity().getApplication());
        TimetableViewModelFactory factory = new TimetableViewModelFactory(repository);
        timetableViewModel = new ViewModelProvider(requireActivity(), factory).get(TimetableViewModel.class);

        // Observe the LiveData
        timetableViewModel.getAllEntries().observe(getViewLifecycleOwner(), entries -> {
            Log.d("TimetableListFragment", "Timetable entries received: " + entries.size());
            adapter.setTimetableEntries(entries);
        });
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
