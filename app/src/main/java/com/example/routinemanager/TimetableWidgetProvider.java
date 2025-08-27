package com.example.routinemanager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * 9. App Widget Provider
 * This is the main entry point for your widget. It handles updates and clicks.
 */
public class TimetableWidgetProvider extends AppWidgetProvider {

    /**
     * This method is called when the widget is added to the home screen
     * and at a specified update interval.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Get the widget's layout
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_widget_layout);

            // Set up the intent that starts the RemoteViewsService to populate the list view
            Intent intent = new Intent(context, TimetableWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // This is the crucial line: it makes the intent's data unique
            // so the RemoteViewsService is properly identified and updated.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Set the adapter for the list view
            views.setRemoteAdapter(R.id.widget_list_view, intent);

            // Set the empty view for the list view
            views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
