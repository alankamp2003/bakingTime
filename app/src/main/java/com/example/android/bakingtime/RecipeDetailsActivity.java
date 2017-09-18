package com.example.android.bakingtime;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.data.Step;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity implements OnStepSelectListener {
    public static final String STEP_INDEX = "step_index";

    private Recipe mRecipe;
    private int mIndex = -1;
    private StepDetailsFragment mStepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(MainActivity.RECIPE);
            mIndex = savedInstanceState.getInt(RecipeDetailsActivity.STEP_INDEX, -1);
        } else if (intent != null) {
            mRecipe = intent.getParcelableExtra(MainActivity.RECIPE);
        }
        ArrayList<Step> steps = null;
        if (mRecipe != null) {
            String servingsText = getResources().getString(R.string.servings, mRecipe.getName(),
                    String.valueOf(mRecipe.getServings()));
            setTitle(servingsText);
            steps = mRecipe.getSteps();
        }
            /*
             * initialize the fragments
             */
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailsFragment detailsFragment =
                (RecipeDetailsFragment) fragmentManager.findFragmentById(R.id.recipe_details_fragment);
        detailsFragment.setRecipe(mRecipe);
        mStepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentById(R.id.step_details_fragment);
        if (mStepDetailsFragment != null) {
            mStepDetailsFragment.setSteps(steps, mIndex, false);
        }
    }

    /**
     * Starts the activity to show the details of a step in a recipe if it's not a two pane layout;
     * otherwise refreshes the fragment for showing the details
     */
    @Override
    public void onStepSelected(int stepIndex) {
        mIndex = stepIndex;
        if (mStepDetailsFragment == null) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(MainActivity.RECIPE, mRecipe);
            intent.putExtra(STEP_INDEX, stepIndex);
            startActivity(intent);
        } else if (mRecipe != null) {
            mStepDetailsFragment.setSteps(mRecipe.getSteps(), stepIndex, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MainActivity.RECIPE, mRecipe);
        outState.putInt(STEP_INDEX, mIndex);
        super.onSaveInstanceState(outState);
    }
}
