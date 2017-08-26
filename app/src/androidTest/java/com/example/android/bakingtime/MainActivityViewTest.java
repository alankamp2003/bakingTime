package com.example.android.bakingtime;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * The class to test the main activity grid view
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityViewTest {
    public static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Checks whether on opening the main activity, the text at a certain position in the grid view
     * matches a certain name
     */
    @Test
    public void opensMainActivity_textMatchesName() {
        onView(withId(R.id.recipes_view)).perform(RecyclerViewActions.scrollToHolder(
                        withHolderRecipeName(RECIPE_NAME)
                )
        );
    }

    private static Matcher<RecyclerView.ViewHolder> withHolderRecipeName(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MasterListAdapter.MasterListAdapterViewHolder>(
                MasterListAdapter.MasterListAdapterViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(MasterListAdapter.MasterListAdapterViewHolder item) {
                TextView nameView = (TextView) item.itemView.findViewById(R.id.recipe_name);
                if (nameView == null) {
                    return false;
                }
                return nameView.getText().equals(text);
            }
        };
    }
}
