package udacity.com.baking_app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import udacity.com.baking_app.fragments.RecipeDetailFragment;
import udacity.com.baking_app.fragments.RecipeStepsFragment;
import udacity.com.baking_app.utils.PreferencesUtil;


public class RecipeActivity extends AppCompatActivity
        implements RecipeStepsFragment.OnFragmentInteractionListener,
        RecipeDetailFragment.OnFragmentInteractionListener {
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
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = getFragmentBundle();

            showStepsFragment(fragmentManager, bundle);
            if (mTwoPane) {
                showRecipeDetailFragment(fragmentManager, bundle);
            }
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


    @Override
    protected void onPause() {
        super.onPause();
        saveRecipeDetailPosition();

    }


    @Override
    public void onRecipeDetailItemClick(Recipe recipe, int recipeDetailPosition) {
        if (mTwoPane) {
            showRecipeDetailFromPosition(recipeDetailPosition);
        } else {
            startRecipeDetailActivity(recipe, recipeDetailPosition);
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

    private void checkTwoPane() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ||
                findViewById(R.id.fl_activity_detail_recipe_container) != null) {
            mTwoPane = true;
        }
    }

    @NonNull
    private Bundle getFragmentBundle() {
        String recipeKey = getString(R.string.recipe_key);
        Recipe recipe = getIntent().getParcelableExtra(recipeKey);

        Bundle bundle;
        bundle = new Bundle();
        bundle.putParcelable(recipeKey, recipe);
        bundle.putInt(getString(R.string.default_recipe_detail_position_key),
                getSavedRecipeDetailPosition());
        return bundle;
    }

    private void showRecipeDetailFragment(FragmentManager fragmentManager, Bundle bundle) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_activity_detail_recipe_container,
                        RecipeDetailFragment.newInstance(bundle), RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showStepsFragment(FragmentManager fragmentManager, Bundle bundle) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_activity_recipe_container,
                        RecipeStepsFragment.newInstance(bundle))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void startRecipeDetailActivity(Recipe recipe, int recipeDetailPosition) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.recipe_key), recipe);
        intent.putExtra(getString(R.string.recipe_detail_position_key), recipeDetailPosition);
        startActivity(intent);
    }

    private void showRecipeDetailFromPosition(int recipeDetailPosition) {
       /* RecipeDetailFragment fragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailFragment.TAG);
        if (fragment != null) {
            fragment.showRecipeDetailInfo(recipeDetailPosition);
        }*/
    }

    private int getSavedRecipeDetailPosition() {
        return PreferencesUtil.getSavedRecipeDetailPosition(this);
    }

    private void saveRecipeDetailPosition() {
        PreferencesUtil.saveRecipeDetailPosition(this, getRecipeDetailPosition());
    }

    private int getRecipeDetailPosition() {
        RecipeDetailFragment fragment = (RecipeDetailFragment)
                getSupportFragmentManager().findFragmentByTag(RecipeDetailFragment.TAG);
        return (fragment == null)
                ? getResources().getInteger(R.integer.default_recipe_detail_position)
                : fragment.getRecipeDetailPosition();
    }

    @Override
    public void showFullScreenMode() {

    }

    @Override
    public void showDefaultMode() {

    }
}
