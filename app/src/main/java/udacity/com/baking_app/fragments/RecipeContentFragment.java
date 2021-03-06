package udacity.com.baking_app.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.adapters.RecipeContentAdapter;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.utils.Utils;
import udacity.com.baking_app.widgets.SpacingItemDecorator;


public class RecipeContentFragment extends BaseFragment {
    public static final String TAG = RecipeContentFragment.class.getCanonicalName();
    @BindView(R.id.rv_fragment_recipe_content)
    RecyclerView contentRecycler;

    private RecipeContentAdapter recipeContentAdapter;
    private OnFragmentInteractionListener fragmentInteractionListener;

    public static RecipeContentFragment newInstance(@NonNull Bundle params) {
        RecipeContentFragment fragment = new RecipeContentFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, params);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeContentFragment() {
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

        int contentPosition;
        if (savedInstanceState == null) {
            contentPosition = recipeStepsBundle
                    .getInt(getString(R.string.recipe_content_position_key));
        } else {
            contentPosition = savedInstanceState.getInt(
                    getString(R.string.recipe_content_position_key));
            /* recipeContentAdapter.setSelectedPosition(savedPosition);
             *//* contentRecycler.scrollToPosition(savedPosition);*/
        }
        createRecipeAdapter(recipe, contentPosition);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recipe_content;
    }

    @Override
    protected void initUi() {
        initContentRecycle();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(getString(R.string.recipe_content_position_key),
                recipeContentAdapter.getSelectedPosition());
        super.onSaveInstanceState(outState);
    }

    private void savePositionInStartArguments() {
        Bundle startBundle = Objects.requireNonNull(getArguments())
                .getBundle(BUNDLE_FRAGMENT_PARAMS_KEY);
        Objects.requireNonNull(startBundle).putInt(getString(R.string.recipe_content_position_key),
                recipeContentAdapter.getSelectedPosition());
    }

    @Override
    public void onPause() {
        super.onPause();
        savePositionInStartArguments();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    public void setContentPosition(int position) {
        recipeContentAdapter.changeSelectedItem(position);
        if (contentRecycler != null) {
            contentRecycler.scrollToPosition(position);
        }
    }

    private void initContentRecycle() {
        Resources resources = getResources();
        int spanCount = resources.getInteger(R.integer.recipe_content_rv_span_count);
        int spacingPx = Utils.convertDpToPx(
                resources.getDimension(R.dimen.recipes_content_rv_item_decorator_spacing),
                resources.getDisplayMetrics().density);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        contentRecycler.addItemDecoration(new SpacingItemDecorator(spanCount, spacingPx, true));
        contentRecycler.setAdapter(recipeContentAdapter);
        contentRecycler.scrollToPosition(recipeContentAdapter.getSelectedPosition());
    }

    private void createRecipeAdapter(Recipe recipe, int contentPosition) {
        recipeContentAdapter = new RecipeContentAdapter(contentPosition, recipe,
                new RecipeContentAdapter.RecyclerViewCallback() {
                    @Override
                    public void onRecipeDetailItemClick(int selectedStepPosition) {
                        recipeContentAdapter.changeSelectedItem(selectedStepPosition);
                        contentRecycler.scrollToPosition(selectedStepPosition);
                        fragmentInteractionListener
                                .onRecipeContentItemClick(selectedStepPosition);
                    }
                });
    }

    public interface OnFragmentInteractionListener {
        void onRecipeContentItemClick(int selectedStepPosition);
    }
}
