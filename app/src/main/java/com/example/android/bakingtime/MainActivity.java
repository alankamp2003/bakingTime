package com.example.android.bakingtime;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingtime.appWidget.BakingTimeAppWidget1;
import com.example.android.bakingtime.appWidget.RecipeStore;
import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.idlingResource.SimpleIdlingResource;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnRecipeClickListener, RecipeDownloader.DelayerCallback {

    public static final String RECIPE = "recipe";
    private MasterListFragment mMasterListFragment;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMasterListFragment =
                (MasterListFragment) fragmentManager.findFragmentById(R.id.master_list_fragment);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BakingTimeAppWidget1.sendRefreshBroadcast(this);
    }

    @Override
    public void setData(ArrayList<Recipe> recipes) {
        mMasterListFragment.setData(recipes);
    }
}