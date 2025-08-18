package com.example.routinemanager;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.routinemanager.TimetableRepository; // Replace with your package name
import com.example.routinemanager.TimetableViewModel; // Replace with your package name

/**
 * 6. ViewModel Factory
 * A factory class to instantiate the ViewModel with the repository.
 */
public class TimetableViewModelFactory implements ViewModelProvider.Factory {
    private TimetableRepository repository;

    public TimetableViewModelFactory(TimetableRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TimetableViewModel.class)) {
            return (T) new TimetableViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

