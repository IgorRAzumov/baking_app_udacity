package udacity.com.baking_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.fragments.RecipeDetailFragment;


public class RecipeDetailActivity extends AppCompatActivity {
    @BindView(R.id.tb_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        initToolbar();

        if (savedInstanceState == null) {
            String recipeKey = getString(R.string.recipe_key);
            String recipeItemPosition = getString(R.string.recipe_detail_item_key);

            Intent intent = getIntent();
            Bundle arguments = new Bundle();
            arguments.putParcelable(recipeKey, intent.getParcelableExtra(recipeKey));
            arguments.putInt(recipeItemPosition, intent.getIntExtra(recipeItemPosition, 0));


            Fragment fragment = RecipeDetailFragment.newInstance(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_activity_detail_recipe_container, fragment)
                    .commit();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
