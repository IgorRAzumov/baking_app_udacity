package udacity.com.baking_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.adapters.RecipeIngredientsAdapter;
import udacity.com.baking_app.data.Ingredient;
import udacity.com.baking_app.widgets.SpacingItemDecorator;

public class IngredientsFragment extends BaseFragment {
    @BindView(R.id.rv_fragment_ingredients)
    RecyclerView ingredientsRecycler;

    private RecipeIngredientsAdapter recipeIngredientsAdapter;

    public IngredientsFragment() {

    }


    public static IngredientsFragment newInstance(@NonNull Bundle bundle) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        Bundle ingredientsListBundle = getArguments()
                .getBundle(getString(R.string.bundle_fragment_params_key));
        assert ingredientsListBundle != null;
        List<Ingredient> ingredientsList = ingredientsListBundle
                .getParcelableArrayList(getString(R.string.ingredients_key));
        recipeIngredientsAdapter = new RecipeIngredientsAdapter(ingredientsList);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ingredients;
    }

    @Override
    protected void initUi() {
        initIngredientsRecycler();
    }

    private void initIngredientsRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredientsRecycler.setLayoutManager(layoutManager);
        ingredientsRecycler.addItemDecoration(new SpacingItemDecorator(1, 16, false));
        ingredientsRecycler.setAdapter(recipeIngredientsAdapter);
    }
}
