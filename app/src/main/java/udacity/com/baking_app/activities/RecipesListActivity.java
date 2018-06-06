package udacity.com.baking_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.fragments.RecipesListFragment;
import udacity.com.baking_app.utils.PreferencesUtil;
import udacity.com.baking_app.utils.SimpleIdlingResource;
import udacity.com.baking_app.widgets.ingredientsWidget.UpdateIngredientsWidgetService;

public class RecipesListActivity extends AppCompatActivity
        implements RecipesListFragment.OnFragmentInteractionListener {
    @BindView(R.id.tb_toolbar)
    Toolbar toolbar;

    @Nullable
    private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        ButterKnife.bind(this);

        initToolbar();
        initIdlingResources();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_recipes_list_container,
                            RecipesListFragment.newInstance(null))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

    }


    @Override
    public void onRecipeClick(Recipe recipe) {
        startRecipeActivity(recipe);
        PreferencesUtil.saveRecipe(this, recipe);
        UpdateIngredientsWidgetService.startActionUpdateIngredientsWidgets(this);
    }

    @VisibleForTesting
    @Override
    public void onRecipesLoadComplete() {
        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }
    }

    @VisibleForTesting
    private void initIdlingResources() {
        getIdlingResource();
        assert idlingResource != null;
        idlingResource.setIdleState(false);
    }

    private void initToolbar() {
        toolbar.setTitle(getString(R.string.baking_time));
        setSupportActionBar(toolbar);
    }

    private void startRecipeActivity(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(getString(R.string.recipe_key), recipe);
        startActivity(intent);
    }

}
