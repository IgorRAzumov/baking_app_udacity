package udacity.com.baking_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtil {
    public static void saveRecipeDetailPosition(Context context, int recipeDetailPosition) {
        getPreferencesEditor(context)
                .putInt(context.getString(R.string.pref_default_recipe_detail_position),
                        recipeDetailPosition)
                .apply();
    }


    public static int getSavedRecipeDetailPosition(Context context) {
        return getSharedPreferences(context)
                .getInt(context.getString(R.string.pref_default_recipe_detail_position),
                        context.getResources().getInteger(R.integer.default_recipe_detail_position));
    }

    public static void saveRecipe(Context context, Recipe recipe) {
        Gson gson = new Gson();
        String json = gson.toJson(recipe);

        getPreferencesEditor(context)
                .putString(context.getString(R.string.ingredients_json_key), json)
                .apply();
    }

    public static Recipe getRecipe(Context context) {
        Recipe recipe = null;
        String json = getSharedPreferences(context)
                .getString(context.getString(R.string.ingredients_json_key), null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Recipe>() {
            }.getType();
            recipe = gson.fromJson(json, type);
        }
        return recipe;
    }


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferencesEditor(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.edit();
    }

    public static boolean checkIngredientList(Context context) {
        String ingredientJson = getSharedPreferences(context)
                .getString(context.getString(R.string.ingredients_json_key), null);
        return ingredientJson!=null && !TextUtils.isEmpty(ingredientJson);
    }
}
