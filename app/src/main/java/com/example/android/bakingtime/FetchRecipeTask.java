package com.example.android.bakingtime;

import android.os.AsyncTask;

import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.utils.JSONUtil;
import com.example.android.bakingtime.utils.NetworkUtil;

import java.net.URL;
import java.util.ArrayList;

public class FetchRecipeTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {

    private RecipeDisplayManager mRecipeActivity;

    public FetchRecipeTask(RecipeDisplayManager recipeActivity) {
        this.mRecipeActivity = recipeActivity;
    }

    //  Within your AsyncTask, override the method onPreExecute and show the loading indicator
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRecipeActivity.showLoadingIndicator();
    }

    @Override
    protected ArrayList<Recipe> doInBackground(Void... params) {

        URL recipeUrl = NetworkUtil.getRecipeUrl();

        try {
            String jsonResponse = NetworkUtil
                    .getResponseFromHttpUrl(recipeUrl);

            return JSONUtil.getRecipes(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        // As soon as the data is finished loading, hide the loading indicator and show the recipes
        mRecipeActivity.hideLoadingIndicator();
        mRecipeActivity.setData(recipes);
    }
}
