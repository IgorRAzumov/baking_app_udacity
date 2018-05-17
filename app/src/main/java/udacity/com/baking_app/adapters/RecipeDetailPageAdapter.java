package udacity.com.baking_app.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Ingredient;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.data.Step;
import udacity.com.baking_app.fragments.IngredientsFragment;
import udacity.com.baking_app.fragments.RecipeStepFragment;

public class RecipeDetailPageAdapter extends FragmentPagerAdapter {
    public static final int INGREDIENTS_POSITION = 0;
    private static final int DATA_OFFSET = 1;

    private final String ingredientsKey;
    private final String stepKey;

    private List<Step> stepsList;
    private List<Ingredient> ingredientsList;


    public RecipeDetailPageAdapter(Context context, FragmentManager fm, Recipe recipe) {
        super(fm);
        this.stepsList = recipe.getSteps();
        this.ingredientsList = recipe.getIngredients();

        ingredientsKey = context.getString(R.string.ingredients_key);
        stepKey = context.getString(R.string.step_key);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        switch (position) {
            case INGREDIENTS_POSITION: {
                bundle.putParcelableArrayList(ingredientsKey, new ArrayList<>(ingredientsList));
                fragment = IngredientsFragment.newInstance(bundle);
                break;
            }
            default: {
                bundle.putParcelable(stepKey, stepsList.get(position - DATA_OFFSET));
                fragment = RecipeStepFragment.newInstance(bundle);
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return stepsList.size() + DATA_OFFSET;
    }
}
