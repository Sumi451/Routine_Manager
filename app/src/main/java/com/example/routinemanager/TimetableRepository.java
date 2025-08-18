package com.example.routinemanager;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import java.util.List;
import com.example.routinemanager.TimetableEntry; // Replace with your package name
import com.example.routinemanager.TimetableDao; // Replace with your package name
import com.example.routinemanager.TimetableDatabase; // Replace with your package name

/**
 * 4. Repository
 * This class abstracts access to data sources. It's the single source of truth for data.
 */
public class TimetableRepository {
    private TimetableDao timetableDao;
    private LiveData<List<TimetableEntry>> allEntries;

    public TimetableRepository(Context context) {
        TimetableDatabase db = TimetableDatabase.getDatabase(context);
        this.timetableDao = db.timetableDao();
        this.allEntries = timetableDao.getAllEntries();
    }

    public LiveData<List<TimetableEntry>> getAllEntries() {
        return allEntries;
    }

    public LiveData<List<TimetableEntry>> getEntriesByDay(String day) {
        return timetableDao.getEntriesByDay(day);
    }

    public LiveData<List<TimetableEntry>> getEntriesByDayOrder(int dayOrder) {
        return timetableDao.getEntriesByDayOrder(dayOrder);
    }

    public void insert(TimetableEntry entry) {
        new insertAsyncTask(timetableDao).execute(entry);
    }

    private static class insertAsyncTask extends AsyncTask<TimetableEntry, Void, Void> {
        private TimetableDao asyncTaskDao;

        insertAsyncTask(TimetableDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimetableEntry... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

