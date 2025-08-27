package com.example.routinemanager;

import android.content.Context;
import android.content.Intent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
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
import java.util.stream.Collectors;
import android.util.Log;

/**
 * 10. RemoteViewsService
 * This service provides the factory that will populate the widget's ListView.
 */
public class TimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
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

    private static final String PREFS_NAME = "RoutineManagerPrefs";
    private static final String KEY_VIEW_TYPE = "view_type";
    private static final String KEY_MAPPING_PREFIX = "day_order_mapping_";

    public TimetableWidgetFactory(Context context) {
        this.context = context;
        this.repository = new TimetableRepository(context);
    }

    @Override
    public void onCreate() {
        // Load data on a background thread. This is a synchronous operation.
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
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String viewType = sharedPreferences.getString(KEY_VIEW_TYPE, "weekly");

        try {
            // Get the current day of the week
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
            String currentDayOfWeek = sdf.format(calendar.getTime());

            if (viewType.equals("weekly")) {
                // Widget is in Weekly View mode, filter by day of week
                timetableEntries = new AsyncTask<String, Void, List<TimetableEntry>>() {
                    @Override
                    protected List<TimetableEntry> doInBackground(String... params) {
                        return repository.getEntriesByDaySync(params[0]);
                    }
                }.execute(currentDayOfWeek).get();
            } else {
                // Widget is in Day Order View mode, determine the current day order
                String dayKey = currentDayOfWeek.toLowerCase(Locale.US);
                int currentDayOrder = sharedPreferences.getInt(KEY_MAPPING_PREFIX + dayKey, 0);

                if (currentDayOrder > 0) {
                    // Use AsyncTask to perform a synchronous query for day order
                    timetableEntries = new AsyncTask<Integer, Void, List<TimetableEntry>>() {
                        @Override
                        protected List<TimetableEntry> doInBackground(Integer... params) {
                            return repository.getEntriesByDayOrderSync(params[0]);
                        }
                    }.execute(currentDayOrder).get();
                } else {
                    // No mapping found or invalid day order, display nothing
                    timetableEntries = null;
                }
            }
        } catch (ExecutionException e) {
            Log.e("TimetableWidgetFactory", "ExecutionException while loading data", e);
            timetableEntries = null;
        } catch (InterruptedException e) {
            Log.e("TimetableWidgetFactory", "InterruptedException while loading data", e);
            Thread.currentThread().interrupt();
            timetableEntries = null;
        }
    }
}
