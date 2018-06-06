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
        checkTwoPane();

        if (savedInstanceState == null) {
            initFragments();
        } else {
            recipeContentPosition = savedInstanceState
                    .getInt(getString(R.string.recipe_content_position_key));
            checkNeedChangeFragmentContainer(savedInstanceState
                    .getBoolean(getString(R.string.was_two_pane_key)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);
        outState.putBoolean(getString(R.string.was_two_pane_key), twoPane);
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
    public void onRecipeContentItemClick(Recipe recipe, int recipeDetailPosition) {
        if (!twoPane) {
            replaceContentFragmentWithDetailFragment(recipeDetailPosition);
        } else {
            showRecipeDetailFragment(recipeDetailPosition);
        }
    }

    private void checkTwoPane() {
        twoPane = findViewById(R.id.fl_activity_recipe_detail_container) != null;
    }

    @Override
    public void changedContent(int position) {
        if (twoPane) {
            Fragment recipeContentFragment = getRecipeContentFragment(getSupportFragmentManager());
            //       ((RecipeContentFragment) recipeContentFragment).setContentPosition(position);
        }
    }

    private void initToolbar() {
        toolbar.setTitle(getString(R.string.back_to_ricepes));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initFragments() {
        addRecipeContentFragment();
        if (twoPane) {
            addRecipeDetailFragment(R.id.fl_activity_recipe_detail_container);
        }
    }

    private void checkNeedChangeFragmentContainer(Boolean wasTwoPane) {
        if (!wasTwoPane && twoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager
                    .findFragmentById(R.id.fl_activity_recipe_main_container);
            if (fragment == null || !(fragment instanceof RecipeDetailFragment)) {
                return;
            }
            fragment = cleanFragment(fragmentManager, fragment);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_activity_recipe_detail_container, fragment,
                            RecipeDetailFragment.TAG)
                    .replace(R.id.fl_activity_recipe_main_container,
                            getRecipeContentFragment(fragmentManager), RecipeContentFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            }
    }

    private void addRecipeContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment recipeContentFragment = getRecipeContentFragment(fragmentManager);
        fragmentManager
                .beginTransaction()
                .add(R.id.fl_activity_recipe_main_container, recipeContentFragment,
                        RecipeContentFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void addRecipeDetailFragment(int containerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment recipeDetailFragment = getRecipeDetailFragment(fragmentManager);
        fragmentManager
                .beginTransaction()
                .add(containerId, recipeDetailFragment, RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void replaceContentFragmentWithDetailFragment(int recipeContentPosition) {
        this.recipeContentPosition = recipeContentPosition;
        Fragment recipeDetailFragment = getRecipeDetailFragment(getSupportFragmentManager());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_recipe_main_container, recipeDetailFragment,
                        RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    private void showRecipeDetailFragment(int recipeDetailPosition) {
        RecipeDetailFragment fragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailFragment.TAG);
        fragment.setCurrentDetailItem(recipeDetailPosition);
    }

    private Fragment getRecipeContentFragment(FragmentManager fragmentManager) {
        Fragment recipeContentFragment = fragmentManager
                .findFragmentByTag(RecipeContentFragment.TAG);
        if (recipeContentFragment == null) {
            recipeContentFragment = RecipeContentFragment.newInstance(getRecipeFragmentBundle());
        }
        return recipeContentFragment;
    }

    private Fragment getRecipeDetailFragment(FragmentManager fragmentManager) {
        Fragment recipeDetailFragment = fragmentManager
                .findFragmentByTag(RecipeDetailFragment.TAG);
        if (recipeDetailFragment == null) {
            Bundle bundle = getRecipeFragmentBundle();
            bundle.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);
            recipeDetailFragment = RecipeDetailFragment.newInstance(bundle);
        } else {
            cleanFragment(fragmentManager, recipeDetailFragment);
        }
        return recipeDetailFragment;
    }

    private Fragment cleanFragment(FragmentManager fragmentManager, Fragment recipeContentFragment) {
        Fragment.SavedState savedState = fragmentManager.saveFragmentInstanceState(recipeContentFragment);
        fragmentManager.beginTransaction().remove(recipeContentFragment).commit();
        fragmentManager.executePendingTransactions();
        recipeContentFragment.setInitialSavedState(savedState);
        return recipeContentFragment;
    }


    @NonNull
    private Bundle getRecipeFragmentBundle() {
        String recipeKey = getString(R.string.recipe_key);
        Recipe recipe = getIntent().getParcelableExtra(recipeKey);

        Bundle bundle;
        bundle = new Bundle();
        bundle.putParcelable(recipeKey, recipe);
        bundle.putInt(getString(R.string.default_recipe_detail_position_key),
                recipeContentPosition);
        return bundle;
    }
}
