package com.example.android.bakingtime.appWidget;

import com.example.android.bakingtime.data.Recipe;

/**
 * The singleton class that stores the currently selected recipe
 */
public class RecipeStore {
    private static RecipeStore instance;
    private Recipe mRecipe;

    private RecipeStore() {
    }

    public static RecipeStore getInstance() {
        if (instance == null)
            instance = new RecipeStore();
        return instance;
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    public Recipe getRecipe() {
        return mRecipe;
    }
}
