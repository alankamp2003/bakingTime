package com.example.android.bakingtime;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.bakingtime.data.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * The class to test the intent generated by the main activity
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    private static final String RECIPE_NAME = "Brownies";
    private static final String INGR_DESC = "1.5 TSP salt";
    private static final String STEP_DESC = "Add eggs.";
    private static final int NUM_INGREDIENTS = 10;
    private static final int NUM_STEPS = 10;


    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickRecipeName_SendsRecipeObject() {
        onView(ViewMatchers.withId(R.id.recipes_view)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(ViewMatchers.withId(R.id.recipes_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        // intended(Matcher<Intent> matcher) asserts the given matcher matches one and only one
        // intent sent by the application.
        intended(allOf(hasRecipeWithName(RECIPE_NAME),
                hasNumIngredients(NUM_INGREDIENTS),
                hasNumSteps(NUM_STEPS)));
    }

    /*
     * Matches the recipe's name
     */
    private static Matcher<Intent> hasRecipeWithName(final String text) {
        return new BoundedMatcher<Intent, Intent>(Intent.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("No recipe found with name: " + text);
            }

            @Override
            protected boolean matchesSafely(Intent intent) {
                Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE);
                return recipe != null && recipe.getName() != null && recipe.getName().equals(text);
            }
        };
    }

    /*
     * Matches the number of ingredients in the recipe
     */
    private static Matcher<Intent> hasNumIngredients(final int num) {
        return new BoundedMatcher<Intent, Intent>(Intent.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("No recipe found with number of ingredients: " + num);
            }

            @Override
            protected boolean matchesSafely(Intent intent) {
                Recipe recipe = intent.getParcelableExtra(MainActivity.RECIPE);
                return recipe != null && recipe.getIngredients() != null && recipe.getIngredients().size() == num;
            }
        };
    }

    /*
     * Matches the number of steps in the recipe
     */
    private static Matcher<Intent> hasNumSteps(final int num) {
        return new BoundedMatcher<Intent, Intent>(Intent.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("No recipe found with number of steps: " + num);
            }

            @Override
            protected boolean matchesSafely(Intent item) {
                Recipe recipe = item.getParcelableExtra(MainActivity.RECIPE);
                return recipe != null && recipe.getSteps() != null && recipe.getSteps().size() == num;
            }
        };
    }
}