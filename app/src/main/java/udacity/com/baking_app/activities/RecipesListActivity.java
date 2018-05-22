package udacity.com.baking_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.fragments.RecipesListFragment;
import udacity.com.baking_app.services.UpdateIngredientsWidgetService;
import udacity.com.baking_app.utils.PreferencesUtil;

public class RecipesListActivity extends AppCompatActivity
        implements RecipesListFragment.OnFragmentInteractionListener {
    @BindView(R.id.tb_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        ButterKnife.bind(this);

        initToolbar();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_recipes_list_container,
                            RecipesListFragment.newInstance(null))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

    }

    private void initToolbar() {
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        startRecipeActivity(recipe);

        PreferencesUtil.saveRecipeIngredientList(this, recipe.getIngredients());
        UpdateIngredientsWidgetService.startActionUpdateIngredientsWidgets(this);
    }

    private void startRecipeActivity(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(getString(R.string.recipe_key), recipe);
        startActivity(intent);
    }
}
