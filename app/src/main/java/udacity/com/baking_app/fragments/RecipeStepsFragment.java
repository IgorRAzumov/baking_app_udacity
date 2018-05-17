package udacity.com.baking_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.adapters.RecipeStepsAdapter;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.data.Step;
import udacity.com.baking_app.widgets.SpacingItemDecorator;


public class RecipeStepsFragment extends BaseFragment {
    @BindView(R.id.rv_fragment_recipes_steps)
    RecyclerView stepsRecycler;

    private RecipeStepsAdapter recipeStepsAdapter;
    private OnFragmentInteractionListener fragmentInteractionListener;

    public static RecipeStepsFragment newInstance(@NonNull Bundle params) {
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, params);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeStepsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.error_implements_fragment_interaction_listener));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle recipeStepsBundle = Objects
                .requireNonNull(getArguments()).getBundle(getString(R.string.bundle_fragment_params_key));
        Recipe recipe = Objects
                .requireNonNull(recipeStepsBundle).getParcelable(getString(R.string.recipe_key));
        createRecipeAdapter(recipe);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recipe;
    }

    @Override
    protected void initUi() {
        initStepsRecycle();
    }

    private void initStepsRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stepsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        stepsRecycler.addItemDecoration(new SpacingItemDecorator(1, 5, true));
        stepsRecycler.setAdapter(recipeStepsAdapter);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    public void showRecipesStepsList(List<Step> stepsList) {

    }

    private void createRecipeAdapter(Recipe recipe) {
        recipeStepsAdapter = new RecipeStepsAdapter(recipe,
                new RecipeStepsAdapter.RecyclerViewCallback() {
                    @Override
                    public void onStepClick(Recipe recipe, int selectedStepPosition) {
                        fragmentInteractionListener.onStepClick(recipe, selectedStepPosition);
                    }

                    @Override
                    public void onIngredientsClick(Recipe recipe) {
                        fragmentInteractionListener.onIngredientsClick(recipe);
                    }
                });
    }

    public interface OnFragmentInteractionListener {

        void onIngredientsClick(Recipe recipe);

        void onStepClick(Recipe recipe, int selectedStepPosition);
    }
}
