package com.example.android.bakingtime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.data.Ingredient;

import java.util.ArrayList;

/**
 * The adapter for displaying the ingredients of a recipe in the recycler view
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {
    private ArrayList<Ingredient> mIngredients;
    private final Context mContext;

    public IngredientAdapter(Context context) {
        mContext = context;
    }

    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_step_item, parent, false);

        view.setFocusable(true);

        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapterViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        Double quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();
        String name = ingredient.getName();
        String description = String.format("%.1f %s %s", quantity, measure, name);
        holder.mDescriptionView.setText(description);
    }

    @Override
    public int getItemCount() {
        return mIngredients != null ? mIngredients.size() : 0;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mDescriptionView;

        public IngredientAdapterViewHolder(View itemView) {
            super(itemView);
            mDescriptionView = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
