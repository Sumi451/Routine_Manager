package com.example.routinemanager;

import android.content.Context;
import android.content.Intent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.routinemanager.TimetableEntry; // Replace with your package name
import com.example.routinemanager.TimetableRepository; // Replace with your package name
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.os.AsyncTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;

/**
 * 10. RemoteViewsService
 * This service provides the factory that will populate the widget's ListView.
 */
public class TimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // Create the factory and load the data on a background thread
        return new TimetableWidgetFactory(this.getApplicationContext());
    }
}

/**
 * 11. RemoteViewsFactory
 * This factory is responsible for getting the data and creating the view for each list item.
 */
class TimetableWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<TimetableEntry> timetableEntries;
    private TimetableRepository repository;

    public TimetableWidgetFactory(Context context) {
        this.context = context;
        this.repository = new TimetableRepository(context);
    }

    @Override
    public void onCreate() {
        // Load data synchronously on a background thread
        loadTimetableData();
    }

    @Override
    public void onDataSetChanged() {
        loadTimetableData();
    }

    @Override
    public void onDestroy() {
        timetableEntries = null;
    }

    @Override
    public int getCount() {
        return timetableEntries == null ? 0 : timetableEntries.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= getCount()) {
            return null;
        }

        TimetableEntry entry = timetableEntries.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        views.setTextViewText(R.id.widget_subject_name, entry.subjectName);
        views.setTextViewText(R.id.widget_time, entry.startTime + " - " + entry.endTime);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadTimetableData() {
        try {
            // Get the current day of the week
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
            String currentDay = sdf.format(calendar.getTime());

            // Use AsyncTask to perform a synchronous query on a background thread
            timetableEntries = new AsyncTask<String, Void, List<TimetableEntry>>() {
                @Override
                protected List<TimetableEntry> doInBackground(String... params) {
                    return repository.getEntriesByDaySync(params[0]);
                }
            }.execute(currentDay).get(); // get() will block until the result is available
        } catch (ExecutionException e) {
            e.printStackTrace();
            timetableEntries = null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
