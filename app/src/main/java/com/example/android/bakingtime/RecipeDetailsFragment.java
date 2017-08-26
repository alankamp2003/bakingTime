package com.example.android.bakingtime;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingtime.data.Ingredient;
import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.data.Step;

import java.util.ArrayList;


/**
 * The fragment for showing the ingredients and steps of a recipe
 */
public class RecipeDetailsFragment extends Fragment {
    private static String INGREDIENT_STATE = "ingredient_state";
    private static String STEP_STATE = "step_state";

    private RecyclerView mIngredientView;
    private TextView mNoIngredientView;
    private RecyclerView mStepView;
    private TextView mNoStepView;
    private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepAdapter;

    private Recipe mRecipe;

    private OnStepSelectListener mListener;

    //private Parcelable ingredientLayoutState;
    //private Parcelable stepLayoutState;
    private int ingredientPosition = -1;
    private int stepPosition = -1;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mIngredientView = (RecyclerView)view.findViewById(R.id.ingredient_view);
        mIngredientView.setLayoutManager(layoutManager);
        mNoIngredientView = (TextView)view.findViewById(R.id.no_ingredient_view);
        layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mStepView = (RecyclerView)view.findViewById(R.id.step_view);
        mStepView.setLayoutManager(layoutManager);
        mStepView.setHasFixedSize(true);
        mStepView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));
        mNoStepView = (TextView)view.findViewById(R.id.no_step_view);
        return view;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        ArrayList<Ingredient> ingredients = null;
        ArrayList<Step> steps = null;
        if (mRecipe != null) {
            ingredients = mRecipe.getIngredients();
            steps = mRecipe.getSteps();
        }
        /*
         * populate the ingredient and step recycler views if there are ingredients and steps
         * respectively; otherwise show the views for no ingredients or steps found
         */
        if (ingredients != null && ingredients.size() > 0) {
            mIngredientView.setVisibility(View.VISIBLE);
            mIngredientAdapter = new IngredientAdapter(this.getActivity());
            mIngredientView.setAdapter(mIngredientAdapter);
            mIngredientAdapter.setIngredients(ingredients);
            mNoIngredientView.setVisibility(View.GONE);
        } else {
            mIngredientView.setVisibility(View.GONE);
            mNoIngredientView.setVisibility(View.VISIBLE);
        }
        if (steps != null && steps.size() > 0) {
            mStepView.setVisibility(View.VISIBLE);
            mStepAdapter = new StepAdapter(this.getActivity());
            mStepView.setAdapter(mStepAdapter);
            mStepAdapter.setSteps(steps, mListener);
            mNoStepView.setVisibility(View.GONE);
        } else {
            mStepView.setVisibility(View.GONE);
            mNoStepView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnStepSelectListener) {
            mListener = (OnStepSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepSelectListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (ingredientLayoutState != null) {
            mIngredientView.getLayoutManager().onRestoreInstanceState(ingredientLayoutState);
        }
        if (stepLayoutState != null) {
            mStepView.getLayoutManager().onRestoreInstanceState(stepLayoutState);
        }*/
        /*if (stepPosition >= 0) {
            mStepView.getLayoutManager().scrollToPosition(stepPosition);
        }
        if (ingredientPosition >= 0) {
            ((LinearLayoutManager)mIngredientView.getLayoutManager()).scrollToPositionWithOffset(ingredientPosition, 0);
        }*/
        if (stepPosition >= 0) {
            //((LinearLayoutManager)mStepView.getLayoutManager()).scrollToPositionWithOffset(stepPosition, 0);
            mStepView.smoothScrollToPosition(stepPosition);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            /*
             * this should restore the scroll position to what it was before rotating the device
             */
            //ingredientLayoutState = savedInstanceState.getParcelable(INGREDIENT_STATE);
            //stepLayoutState = savedInstanceState.getParcelable(STEP_STATE);
            ingredientPosition = savedInstanceState.getInt(INGREDIENT_STATE, -1);
            stepPosition = savedInstanceState.getInt(STEP_STATE, -1);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /*
         * this should save the scroll position at the time of rotating the device; this way the
         * position could be restored after rotating it
         */
        //outState.putParcelable(INGREDIENT_STATE, mIngredientView.getLayoutManager().onSaveInstanceState());
        //outState.putParcelable(STEP_STATE, mStepView.getLayoutManager().onSaveInstanceState());
        outState.putInt(INGREDIENT_STATE, ((LinearLayoutManager)mIngredientView.getLayoutManager()).findLastCompletelyVisibleItemPosition());
        outState.putInt(STEP_STATE, ((LinearLayoutManager)mStepView.getLayoutManager()).findLastCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }
}
