package com.example.android.bakingtime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.data.Step;

import java.util.ArrayList;

/**
 * The adapter for displaying steps in a recipe in the recycler view
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {
    private ArrayList<Step> mSteps;
    private OnStepSelectListener mListener;
    private final Context mContext;
    private View prevSelectedView;

    public StepAdapter(Context context) {
        mContext = context;
    }

    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_step_item, parent, false);

        view.setFocusable(true);

        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapterViewHolder holder, int position) {
        String shortDesc;

        shortDesc = mSteps.get(position).getShortDescription();
        holder.mShortDescriptionView.setText(shortDesc);
    }

    @Override
    public int getItemCount() {
        return mSteps != null ? mSteps.size() : 0;
    }

    public void setSteps(ArrayList<Step> steps, OnStepSelectListener listener) {
        mSteps = steps;
        mListener = listener;
        notifyDataSetChanged();
    }

    class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mShortDescriptionView;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            mShortDescriptionView = (TextView) itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (prevSelectedView != null) {
                prevSelectedView.setSelected(false);
            }
            v.setSelected(true);
            prevSelectedView = v;
            mListener.onStepSelected(getAdapterPosition());
        }
    }
}
