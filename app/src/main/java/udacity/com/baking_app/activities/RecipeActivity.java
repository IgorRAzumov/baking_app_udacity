package udacity.com.baking_app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.data.Step;
import udacity.com.baking_app.fragments.RecipeDetailFragment;
import udacity.com.baking_app.fragments.RecipeStepsFragment;


public class RecipeActivity extends AppCompatActivity
        implements RecipeStepsFragment.OnFragmentInteractionListener {
    @BindView(R.id.tb_toolbar)
    Toolbar toolbar;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        initToolbar();
        checkTwoPane();

        if (savedInstanceState == null) {
            String recipeKey = getString(R.string.recipe_key);
            Recipe recipe = getIntent().getParcelableExtra(recipeKey);

            FragmentManager fragmentManager = getSupportFragmentManager();
            showStepsFragment(recipeKey, recipe, fragmentManager);
            if (mTwoPane) {
                showStepDetailFragment(recipe.getSteps().get(0), fragmentManager);
            }

        }
    }

    @Override
    public void onIngredientsClick(Recipe recipe) {
        if (mTwoPane) {

        } else {

        }
    }

    @Override
    public void onStepClick(Recipe recipe, int selectedStepPosition) {
        if (mTwoPane) {
            //  showStepDetail();
        } else {
            startRecipeDetailActivity(recipe, selectedStepPosition);
        }
    }

    private void showStepDetailFragment(Step step, FragmentManager fragmentManager) {
        Bundle bundle;
        bundle = new Bundle();
        bundle.putParcelable(getString(R.string.recipe_step_key), step);
        Fragment fragment = RecipeDetailFragment.newInstance(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_activity_detail_recipe_container,
                        RecipeDetailFragment.newInstance(bundle),
                        RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showStepsFragment(String recipeKey, Recipe recipe,
                                   FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(recipeKey, recipe);
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_activity_recipe_container,
                        RecipeStepsFragment.newInstance(bundle))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void checkTwoPane() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ||
                findViewById(R.id.fl_activity_detail_recipe_container) != null) {
            mTwoPane = true;
        }
    }

    private void initToolbar() {
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void startRecipeDetailActivity(Recipe recipe, int selectedStepPosition) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.recipe_key), recipe);
        intent.putExtra(getString(R.string.recipe_detail_item_key), selectedStepPosition);
        startActivity(intent);
    }

    private void showStepDetail(Step step) {
        RecipeDetailFragment fragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailFragment.TAG);
        if (fragment != null) {
            fragment.showStepInfo(step);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipesListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
