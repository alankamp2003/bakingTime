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

package com.example.android.bakingtime;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.idlingResource.SimpleIdlingResource;

import java.util.ArrayList;

/**
 * Takes a String and returns it after a while via a callback.
 * <p>
 * This executes a long-running operation on a different thread that results in problems with
 * Espresso if an {@link IdlingResource} is not implemented and registered.
 */
class RecipeDownloader {

    private static final int DELAY_MILLIS = 3000;

    private static ProgressBar mLoadingIndicator;

    // Create an ArrayList of recipes
    final static ArrayList<Recipe> mRecipes = new ArrayList<>();

    interface DelayerCallback{
        void setData(ArrayList<Recipe> recipes);
    }

    /**
     * This method is meant to simulate downloading a large image file which has a loading time
     * delay. This could be similar to downloading an image from the internet.
     * For simplicity, in this hypothetical situation, we've provided the image in
     * {@link drawable/order_activity_tea_image.jpg}.
     * We simulate a delay time of {@link #DELAY_MILLIS} and once the time
     * is up we return the image back to the calling activity via a {@link DelayerCallback}.
     * @param callback used to notify the caller asynchronously
     */
    static void downloadRecipes(Context context, final DelayerCallback callback,
            @Nullable final SimpleIdlingResource idlingResource) {

        /**
         * The IdlingResource is null in production as set by the @Nullable annotation which means
         * the value is allowed to be null.
         *
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        mLoadingIndicator = new ProgressBar(context);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        // Fill ArrayList with recipe objects
        mRecipes.add(new Recipe(1, "Nutella Pie", 8, ""));
        mRecipes.add(new Recipe(2, "Brownies", 8 , ""));
        mRecipes.add(new Recipe(3, "Yellow Cake", 8 , ""));
        mRecipes.add(new Recipe(4, "Cheesecake", 8 , ""));


        /**
         * {@link postDelayed} allows the {@link Runnable} to be run after the specified amount of
         * time set in DELAY_MILLIS elapses. An object that implements the Runnable interface
         * creates a thread. When this thread starts, the object's run method is called.
         *
         * After the time elapses, if there is a callback we return the image resource ID and
         * set the idle state to true.
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.setData(mRecipes);
                    mLoadingIndicator.setVisibility(View.GONE);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }
}