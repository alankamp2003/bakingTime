package com.example.android.bakingtime;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.bakingtime.data.Recipe;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class AppWidgetRefreshService extends IntentService {

    public static final String ACTION_RECIPE = "com.example.android.bakingtime.action.recipe";
    public static final String EXTRA_RECIPE = "com.example.android.bakingtime.extra.recipe";

    public AppWidgetRefreshService() {
        super("AppWidgetRefreshService");
    }

    /**
     * Starts this service to perform Recipe action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionRecipe(Context context, Recipe recipe) {
        Intent intent = new Intent(context, AppWidgetRefreshService.class);
        intent.setAction(ACTION_RECIPE);
        intent.putExtra(EXTRA_RECIPE, recipe);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RECIPE.equals(action)) {
                final Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingTimeAppWidget.class));
                //Trigger data update to handle the GridView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.grid_view);
                BakingTimeAppWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, recipe);
            }
        }
    }
}
