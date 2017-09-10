package com.example.android.bakingtime.appWidget;

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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.Ingredient;
import com.example.android.bakingtime.data.Recipe;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppRemoteViewsFactory(this.getApplicationContext());
    }
}

class AppRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Recipe mRecipe;

    public AppRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mRecipe = RecipeStore.getInstance().getRecipe();
    }

    @Override
    public void onDestroy() {
        mRecipe = null;
    }

    @Override
    public int getCount() {
        if (mRecipe == null || mRecipe.getIngredients() == null)
            return 0;
        return mRecipe.getIngredients().size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() == 0)
            return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Ingredient ingredient = mRecipe.getIngredients().get(position);
        Double quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();
        String name = ingredient.getName();
        String description = String.format("%.1f %s %s", quantity, measure, name);
        views.setTextViewText(R.id.widget_ingredient_name, description);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(MainActivity.RECIPE, mRecipe);
        views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

