package com.example.android.bakingtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.data.Step;

/*
 * The activity that handles showing details of a step in a recipe, on a phone
 */
public class StepDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        Intent intent = getIntent();
        if (intent != null) {
            /*
             * get the details of the step to be shown from the intent and populate the
             * fragment
             */
            Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE);
            int index = intent.getIntExtra(RecipeDetailsActivity.STEP_INDEX, -1);
            if (recipe != null && index >= 0) {
                setTitle(recipe.getName());
                StepDetailsFragment detailsFragment =
                        (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment);
                detailsFragment.setSteps(recipe.getSteps(), index, true);
            }
        }
    }
}
