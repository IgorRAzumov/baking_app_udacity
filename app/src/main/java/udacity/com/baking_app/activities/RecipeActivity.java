package udacity.com.baking_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import udacity.com.baking_app.fragments.RecipeContentFragment;
import udacity.com.baking_app.fragments.RecipeDetailFragment;
import udacity.com.baking_app.fragments.RecipeStepFragment;
import udacity.com.baking_app.utils.PreferencesUtil;


public class RecipeActivity extends AppCompatActivity
        implements RecipeContentFragment.OnFragmentInteractionListener,
        RecipeDetailFragment.OnFragmentInteractionListener {
    @BindView(R.id.tb_toolbar)
    Toolbar toolbar;

    private boolean twoPane;
    private int recipeContentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        initToolbar();
        twoPane = checkTwoPane();

        if (savedInstanceState == null) {
            addRecipeContentFragment();

            if (twoPane) {
                addRecipeDetailFragment(R.id.fl_activity_recipe_detail_container);
            }
        } else {
            recipeContentPosition = savedInstanceState
                    .getInt(getString(R.string.recipe_content_position_key));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);
        super.onSaveInstanceState(outState);
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
    public void onRecipeContentItemClick(Recipe recipe, int recipeDetailPosition) {
        if (!twoPane) {
            replaceContentFragmentWithDetailFragment(recipeDetailPosition);
        } else {
            showRecipeDetailFragment(recipeDetailPosition);
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

    private boolean checkTwoPane() {
        return findViewById(R.id.fl_activity_recipe_detail_container) != null;
    }

    @NonNull
    private Bundle getRecipeFragmentBundle() {
        String recipeKey = getString(R.string.recipe_key);
        Recipe recipe = getIntent().getParcelableExtra(recipeKey);

        Bundle bundle;
        bundle = new Bundle();
        bundle.putParcelable(recipeKey, recipe);
        bundle.putInt(getString(R.string.default_recipe_detail_position_key),
                getSavedRecipeDetailPosition());
        return bundle;
    }

    private void addRecipeContentFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_recipe_main_container,
                        RecipeContentFragment.newInstance(getRecipeFragmentBundle()),
                        RecipeStepFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void addRecipeDetailFragment(int containerId) {
        Bundle bundle = getRecipeFragmentBundle();
        bundle.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId,
                        RecipeDetailFragment.newInstance(bundle), RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void replaceContentFragmentWithDetailFragment(int recipeContentPosition) {
        this.recipeContentPosition = recipeContentPosition;

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(RecipeDetailFragment.TAG);
        if (fragment == null || !(fragment instanceof RecipeDetailFragment)) {
            addRecipeDetailFragment(R.id.fl_activity_recipe_main_container);
        } else {
            ((RecipeDetailFragment) fragment).setCurrentDetailItem(recipeContentPosition);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_activity_recipe_main_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

    }

    private void showRecipeDetailFragment(int recipeDetailPosition) {
        RecipeDetailFragment fragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailFragment.TAG);
        fragment.setCurrentDetailItem(recipeDetailPosition);
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
}

/*
*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(createSystemUiVisabilytyChangeListener());

 }

    private View.OnSystemUiVisibilityChangeListener createSystemUiVisabilytyChangeListener() {
        return new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(RecipeDetailFragment.TAG);
                if (fragment == null) {
                    return;
                }
                RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) fragment;

                if (!recipeDetailFragment.isCurrentItemContainsVideo()) {
                    return;
                }

                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    showSystemUI();
                    recipeDetailFragment.showDefaultMode();
                } else {
                    hideSystemUI();
                    recipeDetailFragment.showFullScreenMode();
                }
            }
        };
    }

    private void hideSystemUI() {

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void showSystemUI() {

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }
*
*
* */