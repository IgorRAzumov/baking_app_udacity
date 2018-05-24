package udacity.com.baking_app.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.adapters.RecipeIngredientsAdapter;
import udacity.com.baking_app.data.Ingredient;
import udacity.com.baking_app.utils.Utils;
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
        Bundle ingredientsListBundle = Objects.requireNonNull(getArguments())
                .getBundle(getString(R.string.bundle_fragment_params_key));
        List<Ingredient> ingredientsList = Objects.requireNonNull(ingredientsListBundle)
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
        Resources resources = getResources();
        int spanCount = resources.getInteger(R.integer.ingredients_fragment_rv_span_count);
        int spacingPx = Utils.convertDpToPx(
                resources.getDimension(R.dimen.ingredients_fragment_rv_item_decorator_spacing),
                resources.getDisplayMetrics().density);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredientsRecycler.setLayoutManager(layoutManager);
        ingredientsRecycler.addItemDecoration(new SpacingItemDecorator(spanCount, spacingPx, true));
        ingredientsRecycler.setAdapter(recipeIngredientsAdapter);
    }
}
