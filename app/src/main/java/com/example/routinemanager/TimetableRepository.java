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

    public LiveData<TimetableEntry> getEntryById(int id) {
        return timetableDao.getEntryById(id);
    }

    // Synchronous method for the widget to get entries by day of week
    public List<TimetableEntry> getEntriesByDaySync(String day) {
        return timetableDao.getEntriesByDaySync(day);
    }

    // NEW: Synchronous method for the widget to get entries by day order
    public List<TimetableEntry> getEntriesByDayOrderSync(int dayOrder) {
        return timetableDao.getEntriesByDayOrderSync(dayOrder);
    }

    public void insert(TimetableEntry entry) {
        new insertAsyncTask(timetableDao).execute(entry);
    }

    public void update(TimetableEntry entry) {
        new updateAsyncTask(timetableDao).execute(entry);
    }

    public void delete(TimetableEntry entry) {
        new deleteAsyncTask(timetableDao).execute(entry);
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

    private static class updateAsyncTask extends AsyncTask<TimetableEntry, Void, Void> {
        private TimetableDao asyncTaskDao;

        updateAsyncTask(TimetableDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimetableEntry... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<TimetableEntry, Void, Void> {
        private TimetableDao asyncTaskDao;

        deleteAsyncTask(TimetableDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimetableEntry... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
