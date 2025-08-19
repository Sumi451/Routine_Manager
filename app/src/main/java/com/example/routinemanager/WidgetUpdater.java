package com.example.routinemanager;
import android.content.Context;
import android.content.Intent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;

/**
 * A helper class to send a broadcast to update the app widget.
 */
public class WidgetUpdater {

    /**
     * Notifies the App Widget to refresh its data. This is more efficient
     * than sending a full update broadcast.
     */
    public static void updateWidget(Context context) {
        // Get the AppWidgetManager and the component name of the widget provider
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, TimetableWidgetProvider.class);

        // Get the IDs of all instances of the widget
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        // Notify the widget's list view that its data has changed
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        }
    }
}
