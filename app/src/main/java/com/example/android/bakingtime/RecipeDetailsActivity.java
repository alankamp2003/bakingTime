package com.example.android.bakingtime;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingtime.data.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity implements OnStepSelectListener {
    public static final String STEP_INDEX = "step_index";

    private Recipe mRecipe;
    private StepDetailsFragment mStepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Intent intent = getIntent();
        if (intent != null) {
            mRecipe = intent.getParcelableExtra(MainActivity.RECIPE);
            if (mRecipe != null) {
                setTitle(mRecipe.getName());
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
                mStepDetailsFragment.setSteps(null, -1, false);
            }
        }
    }

    /**
     * Starts the activity to show the details of a step in a recipe if it's not a two pane layout;
     * otherwise refreshes the fragment for showing the details
     */
    @Override
    public void onStepSelected(int stepIndex) {
        if (mStepDetailsFragment == null) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(MainActivity.RECIPE, mRecipe);
            intent.putExtra(STEP_INDEX, stepIndex);
            startActivity(intent);
        } else if (mRecipe != null) {
            mStepDetailsFragment.setSteps(mRecipe.getSteps(), stepIndex, false);
        }
    }
}
