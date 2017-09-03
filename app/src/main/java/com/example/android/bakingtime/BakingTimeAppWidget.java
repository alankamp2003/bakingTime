package com.example.android.bakingtime;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.android.bakingtime.data.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingTimeAppWidget extends AppWidgetProvider {
    private static Recipe sRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent;
        RemoteViews views;

        // Construct the RemoteViews object
        if (sRecipe != null) {
            views = getRecipeView(context);
        } else {
            views = getNoRecipeView(context);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, Recipe recipe) {
        sRecipe = recipe;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        AppWidgetRefreshService.startActionRecipe(context, sRecipe);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        AppWidgetRefreshService.startActionRecipe(context, sRecipe);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Returns the view to be shown when a recipe is selected in the app; if the recipe has
     * ingredients, they're in a grid view; otherwise only the recipe's name is shown
     */
    private static RemoteViews getRecipeView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(MainActivity.RECIPE, sRecipe);
        views.setRemoteAdapter(R.id.grid_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.grid_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.grid_view, R.id.empty_view);
        return views;
    }

    /**
     * Returns the view when no recipe is selected in the app
     */
    private static RemoteViews getNoRecipeView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_app_widget);
        Intent intent = new Intent(context, MainActivity.class);
        views.setViewVisibility(R.id.recipe_name, View.GONE);
        views.setViewVisibility(R.id.widget_cake_image, View.VISIBLE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_cake_image, pendingIntent);
        return views;
    }
}

