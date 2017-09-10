package com.example.android.bakingtime;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * The class to test the various fragments of the app
 */

@RunWith(AndroidJUnit4.class)
public class FragmentViewTest {
    private static final String INGR_DESC = "1.0 TSP salt";
    private static final String STEP_DESC = "Recipe Introduction";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void clickRecipeName() {
        onView(ViewMatchers.withId(R.id.recipes_view)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(ViewMatchers.withId(R.id.recipes_view)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

    @Test
    public void hasIngredient() {
        // test whether the ingredient at this position has the right description
        onView(ViewMatchers.withId(R.id.ingredient_view)).perform(
                RecyclerViewActions.scrollToHolder(withIngredientDescription(INGR_DESC))
        );
    }

    @Test
    public void hasStep() {
        // test whether the step at this position has the right description
        onView(ViewMatchers.withId(R.id.step_view)).perform(
                RecyclerViewActions.scrollToHolder(withStepDescription(STEP_DESC)));
    }

    @Test
    public void clickStep_ShowDescription() {
        /*
         * test whether on clicking on a step, the fragment showing the step's details shows the
         * right description
         */
        onView(ViewMatchers.withId(R.id.step_view)).perform(
                RecyclerViewActions.scrollToHolder(withStepDescription(STEP_DESC)));
        onView(ViewMatchers.withId(R.id.step_view)).perform(RecyclerViewActions.actionOnHolderItem(
                withStepDescription(STEP_DESC), click()));
        onView(withId(R.id.step_short_description)).check(matches(withText(STEP_DESC)));
        onView(withId(R.id.step_description)).check(matches(withText(STEP_DESC)));
    }

    /*
     * Matches the description of an ingredient
     */
    private static Matcher<RecyclerView.ViewHolder> withIngredientDescription(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, IngredientAdapter.IngredientAdapterViewHolder>(
                IngredientAdapter.IngredientAdapterViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(IngredientAdapter.IngredientAdapterViewHolder item) {
                TextView description = (TextView) item.itemView.findViewById(R.id.description);
                if (description == null) {
                    return false;
                }
                return description.getText().equals(text);
            }
        };
    }

    /*
     * Matches the description of an step
     */
    private static Matcher<RecyclerView.ViewHolder> withStepDescription(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, StepAdapter.StepAdapterViewHolder>(
                StepAdapter.StepAdapterViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(StepAdapter.StepAdapterViewHolder item) {
                TextView description = (TextView) item.itemView.findViewById(R.id.description);
                if (description == null) {
                    return false;
                }
                return description.getText().toString().equals(text);
            }
        };
    }
}
