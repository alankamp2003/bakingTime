package com.example.android.bakingtime;

import android.content.Context;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * The interface used by an async task to interact with a class responsible for displaying data
 * related to recipes
 */
public interface RecipeDisplayManager<T extends Parcelable> {
    public void showLoadingIndicator();
    public void hideLoadingIndicator();
    public Context getContext();
    public void setData(ArrayList<T> data);
}
