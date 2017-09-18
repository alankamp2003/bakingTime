/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.bakingtime;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingtime.appWidget.BakingTimeAppWidget1;
import com.example.android.bakingtime.appWidget.RecipeStore;
import com.example.android.bakingtime.data.Recipe;

import java.util.List;

import com.example.android.bakingtime.R;
import com.squareup.picasso.Picasso;

// Custom adapter class that displays a list of recipes in a GridView
public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.MasterListAdapterViewHolder> {

    // Keeps track of the context and list of recipes to display
    private Context mContext;
    private List<Recipe> mRecipes;
    private MasterListFragment.OnRecipeClickListener mCallback;

    /**
     * Constructor method
     * @param recipes The list of recipes to display
     */
    public MasterListAdapter(Context context, MasterListFragment.OnRecipeClickListener callback, List<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
        mCallback = callback;
    }

    @Override
    public MasterListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);

        view.setFocusable(true);

        return new MasterListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MasterListAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        // Set the recipe name and image
        holder.nameView.setText(recipe.getName());
        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(mContext).load(recipe.getImage()).placeholder(R.drawable.question_mark).
                    error(R.drawable.question_mark).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.question_mark);
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mRecipes != null ? mRecipes.size() : 0;
    }

    /*
     * The viewholder for the RecyclerView
     */
    class MasterListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView nameView;
        ImageView imageView;

        public MasterListAdapterViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.recipe_name);
            imageView = (ImageView) itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mRecipes.get(getAdapterPosition());
            mCallback.onRecipeSelected(recipe);
            RecipeStore.getInstance().setRecipe(recipe);
            BakingTimeAppWidget1.sendRefreshBroadcast(mContext);
        }
    }
}
