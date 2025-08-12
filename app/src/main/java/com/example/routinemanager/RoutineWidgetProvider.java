package com.example.routinemanager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RoutineWidgetProvider extends AppWidgetProvider {

    public static final String PREF_ROUTINE_KEY = "routine_key";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.routine_widget);

        SharedPreferences sharedPreferences = context.getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        Set<String> routineSet = sharedPreferences.getStringSet(PREF_ROUTINE_KEY, null);

        if (routineSet != null && !routineSet.isEmpty()) {
            List<String> routineList = new ArrayList<>(routineSet);
            Collections.sort(routineList);

            String nextClass = findNextClass(routineList);

            if (nextClass != null) {
                String[] parts = nextClass.split(":", 2);
                String time = parts[0];
                String subject = parts[1];
                views.setTextViewText(R.id.appwidget_time, time);
                views.setTextViewText(R.id.appwidget_subject, subject);
            } else {
                views.setTextViewText(R.id.appwidget_time, "No more classes");
                views.setTextViewText(R.id.appwidget_subject, "");
            }
        }else
        {
            views.setTextViewText(R.id.appwidget_time, "No routine saved");
            views.setTextViewText(R.id.appwidget_subject, "");
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    private static String findNextClass(List<String> routineList) {
        // This is a simplified implementation. A real-world app would use
        // a more robust time comparison to find the next class based on the current time.
        // For this example, we'll just return the first entry.
        //TODO: Add time comparison
        if (!routineList.isEmpty()) {
            return routineList.get(0);
        }
        return null;
    }
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
