package com.example.android.bakingtime.utils;

import android.util.JsonReader;
import android.util.JsonToken;

import com.example.android.bakingtime.data.Ingredient;
import com.example.android.bakingtime.data.Recipe;
import com.example.android.bakingtime.data.Step;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


/**
 * This class that handles the operations related to the json file containing recipe data
 */
public class JSONUtil {

    /**
     * Reads the json file and returns a list of objects containing the data in the file
     */
    public static ArrayList<Recipe> getRecipes(String jsonStr) {
        JsonReader reader;
        ArrayList<Recipe> recipes = new ArrayList<>();
        reader = null;
        try {
            reader = new JsonReader(new StringReader(jsonStr));
            reader.beginArray();
            while (reader.hasNext()) {
                recipes.add(readRecipe(reader));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return recipes;
    }

    /**
     * Method used for obtaining a single recipe from the JSON file.
     * @param reader The JSON reader object pointing a single sample JSON object.
     * @return The Recipe the JsonReader is pointing to.
     */
    private static Recipe readRecipe(JsonReader reader) {
        Recipe recipe;

        recipe = new Recipe();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String nextName = reader.nextName();
                switch (nextName) {
                    case "id":
                        recipe.setId(reader.nextInt());
                        break;
                    case "name":
                        recipe.setName(reader.nextString());
                        break;
                    case "ingredients":
                        if (reader.peek() != JsonToken.NULL) {
                            recipe.setIngredients(readIngredients(reader));
                        }
                        break;
                    case "steps":
                        if (reader.peek() != JsonToken.NULL) {
                            recipe.setSteps(readSteps(reader));
                        }
                        break;
                    case "servings":
                        recipe.setServings(reader.nextInt());
                        break;
                    case "image":
                        recipe.setImage(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    /**
     * Method used for obtaining a list of ingredients for a recipe from the JSON file.
     * @param reader The JSON reader object pointing a single recipe JSON object.
     * @return The Recipe the JsonReader is pointing to.
     */
    private static ArrayList<Ingredient> readIngredients(JsonReader reader) throws IOException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ingredients.add(readIngredient(reader));
        }
        reader.endArray();
        return ingredients;
    }

    /**
     * Method used for obtaining one ingredient for a recipe from the JSON file.
     * @param reader The JSON reader object pointing a single ingredient JSON object.
     * @return The Recipe the JsonReader is pointing to.
     */
    private static Ingredient readIngredient(JsonReader reader) throws IOException {
        Ingredient ingredient = new Ingredient();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "quantity":
                    ingredient.setQuantity(reader.nextDouble());
                    break;
                case "measure":
                    ingredient.setMeasure(reader.nextString());
                    break;
                case "ingredient":
                    ingredient.setName(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return ingredient;
    }

    /**
     * Method used for obtaining a list of ingredients for a recipe from the JSON file.
     * @param reader The JSON reader object pointing a single recipe JSON object.
     * @return The Recipe the JsonReader is pointing to.
     */
    private static ArrayList<Step> readSteps(JsonReader reader) throws IOException {
        ArrayList<Step> steps = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            steps.add(readStep(reader));
        }
        reader.endArray();
        return steps;
    }

    /**
     * Method used for obtaining one ingredient for a recipe from the JSON file.
     * @param reader The JSON reader object pointing a single ingredient JSON object.
     * @return The Recipe the JsonReader is pointing to.
     */
    private static Step readStep(JsonReader reader) throws IOException {
        Step step = new Step();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    step.setId(reader.nextInt());
                    break;
                case "shortDescription":
                    step.setShortDescription(reader.nextString());
                    break;
                case "description":
                    step.setDescription(reader.nextString());
                    break;
                case "videoURL":
                    step.setVideoURL(reader.nextString());
                    break;
                case "thumbnailURL":
                    step.setThumbnailURL(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return step;
    }
}
