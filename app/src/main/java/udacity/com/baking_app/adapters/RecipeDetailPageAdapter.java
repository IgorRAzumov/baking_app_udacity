package udacity.com.baking_app.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.data.Step;
import udacity.com.baking_app.fragments.IngredientsFragment;
import udacity.com.baking_app.fragments.RecipeStepFragment;

public class RecipeDetailPageAdapter extends FragmentStatePagerAdapter {
    private static final int INGREDIENTS_POSITION = 0;
    private static final int STEP_POSITION_OFFSET = 1;

    private final String ingredientKey;
    private final String stepKey;

    private Recipe recipe;

    public RecipeDetailPageAdapter(Context context, FragmentManager fm, @NonNull Recipe recipe) {
        super(fm);
        this.recipe = recipe;
        this.ingredientKey = context.getString(R.string.ingredients_key);
        this.stepKey = context.getString(R.string.step_key);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        switch (position) {
            case INGREDIENTS_POSITION: {
                bundle.putParcelableArrayList(ingredientKey,
                        new ArrayList<>(recipe.getIngredients()));
                fragment = IngredientsFragment.newInstance(bundle);
                break;
            }
            default: {
                bundle.putParcelable(stepKey,
                        (recipe.getSteps().get(position - STEP_POSITION_OFFSET)));
                fragment = RecipeStepFragment.newInstance(bundle);
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        int count;
        List<Step> steps = recipe.getSteps();
        if (steps == null) {
            count = 0;
        } else {
            count = steps.size() + STEP_POSITION_OFFSET;
        }
        return count;
    }
}
