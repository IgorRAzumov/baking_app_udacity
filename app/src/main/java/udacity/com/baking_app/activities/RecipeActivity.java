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
import udacity.com.baking_app.utils.PreferencesUtil;


public class RecipeActivity extends AppCompatActivity
        implements RecipeContentFragment.OnFragmentInteractionListener,
        RecipeDetailFragment.OnFragmentInteractionListener {
    @BindView(R.id.tb_toolbar)
    Toolbar toolbar;

    private boolean twoPane;
    private boolean wasTwoPane;

    private int recipeContentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        checkTwoPane(wasTwoPane);

        if (savedInstanceState != null) {
            recipeContentPosition = savedInstanceState
                    .getInt(getString(R.string.recipe_content_position_key));
            wasTwoPane = savedInstanceState.getBoolean(getString(R.string.was_two_pane_key));
        }

        checkTwoPane(wasTwoPane);
        initUi();
    }

    private void initUi() {
        initToolbar();
        if (!wasTwoPane) {
            addRecipeContentFragment();
        }else if(!twoPane){
           replaceContentFragmentWithDetailFragment(recipeContentPosition);
        }

        if (twoPane) {
            addRecipeDetailFragment(R.id.fl_activity_recipe_detail_container);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);
        outState.putBoolean(getString(R.string.was_two_pane_key), wasTwoPane);
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

    private void checkTwoPane(boolean wasTwoPane) {
        twoPane = findViewById(R.id.fl_activity_recipe_detail_container) != null;
        this.wasTwoPane = wasTwoPane;
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment recipeContentFragment = getRecipeContentFragment(fragmentManager);
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_activity_recipe_main_container, recipeContentFragment,
                        RecipeContentFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private Fragment getRecipeContentFragment(FragmentManager fragmentManager) {
        Fragment recipeContentFragment = fragmentManager
                .findFragmentByTag(RecipeContentFragment.TAG);
        if (recipeContentFragment == null) {
            recipeContentFragment = RecipeContentFragment.newInstance(getRecipeFragmentBundle());
        } else {

            fragmentManager.beginTransaction().remove(recipeContentFragment).commit();
            fragmentManager.executePendingTransactions();
        }
        return recipeContentFragment;
    }

    private void addRecipeDetailFragment(int containerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment recipeDetailFragment = getRecipeDetailFragment(fragmentManager);

        fragmentManager
                .beginTransaction()
                .replace(containerId, recipeDetailFragment, RecipeDetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private Fragment getRecipeDetailFragment(FragmentManager fragmentManager) {
        Fragment recipeDetailFragment = fragmentManager
                .findFragmentByTag(RecipeDetailFragment.TAG);

        if (recipeDetailFragment == null) {
            Bundle bundle = getRecipeFragmentBundle();
            bundle.putInt(getString(R.string.recipe_content_position_key), recipeContentPosition);
            recipeDetailFragment = RecipeDetailFragment.newInstance(bundle);
        } else {
            fragmentManager.beginTransaction().remove(recipeDetailFragment).commit();
            fragmentManager.executePendingTransactions();
        }
        return recipeDetailFragment;
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
                    .replace(R.id.fl_activity_recipe_main_container, getRecipeDetailFragment(fragmentManager))
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
