package udacity.com.baking_app.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.data.Step;
import udacity.com.baking_app.fragments.IngredientsFragment;
import udacity.com.baking_app.fragments.RecipeStepFragment;

public class RecipeDetailPageAdapter extends FragmentStatePagerAdapter {
    public static final int INGREDIENTS_POSITION = 0;
    private static final int STEP_POSITION_OFFSET = 1;

    private final String ingredientKey;
    private final String stepTextKey;

    private Fragment fragment;
    private Recipe recipe;

    public RecipeDetailPageAdapter(Context context, FragmentManager fm, @NonNull Recipe recipe) {
        super(fm);
        this.recipe = recipe;
        this.ingredientKey = context.getString(R.string.ingredients_key);
        this.stepTextKey = context.getString(R.string.step_text_key);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case INGREDIENTS_POSITION: {
                bundle.putParcelableArrayList(ingredientKey,
                        new ArrayList<>(recipe.getIngredients()));
                fragment = IngredientsFragment.newInstance(bundle);
                break;
            }
            default: {
                bundle.putString(stepTextKey,
                        (recipe.getSteps().get(position - STEP_POSITION_OFFSET).getDescription()));
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

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (fragment != object) {
            fragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return fragment;
    }

    public Step getRecipeDetail(int viewPagerPosition) {
        //     Process: udacity.com.baking_app, PID: 3925
        //                  java.lang.ArrayIndexOutOfBoundsException: length=12; index=-1
        //
        return recipe.getSteps().get(viewPagerPosition - STEP_POSITION_OFFSET);
    }
}
