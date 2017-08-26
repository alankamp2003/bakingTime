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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingtime.data.Recipe;

import java.util.ArrayList;


// This fragment displays all of the recipes in one grid
public class MasterListFragment extends Fragment implements RecipeDisplayManager<Recipe> {

    private static final String RECIPES = "recipes";

    private ArrayList<Recipe> mRecipes;

    private RecyclerView mRecipeView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageView;
    private TextView mNoRecipeMessageView;

    // Define a new interface OnRecipeClickListener that triggers a callback in the host activity
    OnRecipeClickListener mCallback;

    // OnRecipeClickListener interface, calls a method in the host activity named onRecipeSelected
    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all recipes
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get references to the views in the fragment_master_list xml layout file
        mRecipeView = (RecyclerView) rootView.findViewById(R.id.recipes_view);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.loading_indicator);
        mErrorMessageView = (TextView) rootView.findViewById(R.id.error_message_view);
        mNoRecipeMessageView = (TextView) rootView.findViewById(R.id.no_recipe_message_view);

        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES);
        }
        if (mRecipes == null) {
            loadRecipeData();
        } else {
            showRecipes();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPES, mRecipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void setData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        if (mRecipes != null) {
            showRecipes();
        } else if (mRecipes.size() ==0) {
            showNoRecipeMessage();
        } else {
            showErrorMessage();
        }
    }

    /*
     * Sets the recipe list in the recipe adapter
     */
    private void showRecipes() {
        showRecipeView();
        if (mRecipes != null) {
            MasterListAdapter mAdapter = new MasterListAdapter(getContext(), mCallback, mRecipes);

            // Set the adapter on the RecyclerView
            mRecipeView.setAdapter(mAdapter);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            if (width <= 1440) {
                mRecipeView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            } else {
                mRecipeView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }
        }
    }

    /**
     * This method will tell some background method to get the recipe data in the background.
     */
    private void loadRecipeData() {
        showRecipeView();

        new FetchRecipeTask(this).execute();
    }

    /**
     * This method will make the View for the recipe data visible and
     * hide the error message.
     */
    private void showRecipeView() {
        mErrorMessageView.setVisibility(View.GONE);
        mNoRecipeMessageView.setVisibility(View.GONE);
        mRecipeView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the recipe
     * View.
     */
    private void showErrorMessage() {
        mRecipeView.setVisibility(View.GONE);
        mNoRecipeMessageView.setVisibility(View.GONE);
        mErrorMessageView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the message saying no favorites were found visible and hide the recipe
     * View.
     */
    private void showNoRecipeMessage() {
        mRecipeView.setVisibility(View.INVISIBLE);
        mErrorMessageView.setVisibility(View.INVISIBLE);
        mNoRecipeMessageView.setVisibility(View.VISIBLE);
    }
}
