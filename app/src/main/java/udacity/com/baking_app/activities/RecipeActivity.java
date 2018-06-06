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
    public void onRecipeContentItemClick(int recipeContentPosition) {
        this.recipeContentPosition = recipeContentPosition;
        if (!twoPane) {
            replaceContentFragmentWithDetailFragment();
        } else {
            showRecipeDetailFragmentContent();
        }
    }

    private void checkTwoPane() {
        twoPane = findViewById(R.id.fl_activity_recipe_detail_container) != null;
    }

    @Override
    public void changedContent(int position) {
        recipeContentPosition=position;
        if (twoPane) {
            Fragment recipeContentFragment = getRecipeContentFragment(getSupportFragmentManager());
                 ((RecipeContentFragment) recipeContentFragment).setContentPosition(position);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        addFragment(fragmentManager, getRecipeContentFragment(fragmentManager),
                R.id.fl_activity_recipe_main_container, RecipeContentFragment.TAG);
        if (twoPane) {
            addFragment(fragmentManager, getRecipeDetailFragment(fragmentManager),
                    R.id.fl_activity_recipe_detail_container, RecipeDetailFragment.TAG);
        }
    }

    private void checkNeedChangeFragmentContainer(Boolean wasTwoPane) {
        if (!wasTwoPane && twoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment recipeContentFragment = getRecipeContentFragment(fragmentManager);
            Fragment recipeDetailFragment = fragmentManager
                    .findFragmentById(R.id.fl_activity_recipe_main_container);
            if (recipeDetailFragment == null || !(recipeDetailFragment instanceof RecipeDetailFragment)) {
                return;
            }
            recipeDetailFragment = cleanFragment(fragmentManager, recipeDetailFragment);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_activity_recipe_detail_container, recipeDetailFragment,
                            RecipeDetailFragment.TAG)
                    .replace(R.id.fl_activity_recipe_main_container, recipeContentFragment,
                            RecipeContentFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    private void replaceContentFragmentWithDetailFragment() {
        Fragment recipeDetailFragment = getRecipeDetailFragment(getSupportFragmentManager());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_recipe_main_container, recipeDetailFragment,
                        RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    private void showRecipeDetailFragmentContent() {
        RecipeDetailFragment fragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailFragment.TAG);
        if (fragment != null) {
            fragment.setCurrentDetailItem(recipeContentPosition);
        }
    }

    private void addFragment(FragmentManager fragmentManager,
                             Fragment fragment, int containerId, String tag) {
        fragmentManager
                .beginTransaction()
                .add(containerId, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @NonNull
    private Fragment getRecipeContentFragment(FragmentManager fragmentManager) {
        Fragment recipeContentFragment = fragmentManager
                .findFragmentByTag(RecipeContentFragment.TAG);
        if (recipeContentFragment == null) {
            recipeContentFragment = RecipeContentFragment.newInstance(getRecipeFragmentBundle());
        }
        return recipeContentFragment;
    }

    @NonNull
    private Fragment getRecipeDetailFragment(FragmentManager fragmentManager) {
        Fragment recipeDetailFragment = fragmentManager
                .findFragmentByTag(RecipeDetailFragment.TAG);
        if (recipeDetailFragment == null) {
            recipeDetailFragment = RecipeDetailFragment.newInstance(getRecipeFragmentBundle());
        } else {
            cleanFragment(fragmentManager, recipeDetailFragment);
        }
        return recipeDetailFragment;
    }

    @NonNull
    private Bundle getRecipeFragmentBundle() {
        String recipeKey = getString(R.string.recipe_key);
        Recipe recipe = getIntent().getParcelableExtra(recipeKey);

        Bundle bundle = new Bundle();
        bundle.putParcelable(recipeKey, recipe);
        bundle.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);
        return bundle;
    }

    private Fragment cleanFragment(FragmentManager fragmentManager, Fragment recipeContentFragment) {
        Fragment.SavedState savedState = fragmentManager.saveFragmentInstanceState(recipeContentFragment);
        fragmentManager.beginTransaction().remove(recipeContentFragment).commit();
        fragmentManager.executePendingTransactions();
        recipeContentFragment.setInitialSavedState(savedState);
        return recipeContentFragment;
    }
}
