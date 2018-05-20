package udacity.com.baking_app.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.adapters.RecipesListAdapter;
import udacity.com.baking_app.asyncTasksLoaders.RecipesLoader;
import udacity.com.baking_app.data.Recipe;
import udacity.com.baking_app.utils.Utils;
import udacity.com.baking_app.widgets.SpacingItemDecorator;


public class RecipesListFragment extends BaseFragment {
    private static final int RECIPES_LOADER_ID = 3;

    @BindView(R.id.rv_fragment_recipes_list)
    RecyclerView recipesRecycler;
    @BindView(R.id.pb_fragment_recipes_list_progress)
    ProgressBar progressBar;

    private OnFragmentInteractionListener fragmentInteractionListener;


    public RecipesListFragment() {

    }

    public static RecipesListFragment newInstance(@Nullable Bundle params) {
        RecipesListFragment fragment = new RecipesListFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, params);
        fragment.setArguments(args);
        return fragment;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getSupportLoaderManager()
                    .initLoader(RECIPES_LOADER_ID, null, createRecipesLoaderCallback());
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recipes_list;
    }

    @Override
    protected void initUi() {
        initRecipesRecycler();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }


    private void initRecipesRecycler() {
        Resources resources = getResources();
        int spanCount = resources.getInteger(
                resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? R.integer.list_recipes_landscape_span_count
                        : R.integer.list_recipes_portrait_span_coun);
        int spacingPx = Utils.convertDpToPx(
                resources.getDimension(R.dimen.recipes_fragment_rv_item_decorator_spacing),
                resources.getDisplayMetrics().density);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        recipesRecycler.setLayoutManager(gridLayoutManager);
        recipesRecycler.addItemDecoration(new SpacingItemDecorator(spanCount, spacingPx, true));
    }

    private LoaderManager.LoaderCallbacks<List<Recipe>> createRecipesLoaderCallback() {
        return new LoaderManager.LoaderCallbacks<List<Recipe>>() {
            @NonNull
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
                return new RecipesLoader(getContext());
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
                if (data == null) {
                    showMessage(R.string.error_load_recipes, Snackbar.LENGTH_LONG);
                } else if (data.size() == 0) {
                    showMessage(R.string.error_empty_data, Snackbar.LENGTH_LONG);
                } else {
                    completeDataLoad(data);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

            }
        };
    }

    private void completeDataLoad(List<Recipe> data) {
        progressBar.setVisibility(View.GONE);
        recipesRecycler.setAdapter(createRecipesListAdapter(data));
    }


    private void showMessage(int messageResId, int duration) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, messageResId, duration).show();
        }
    }

    @NonNull
    private RecipesListAdapter createRecipesListAdapter(List<Recipe> data) {
        return new RecipesListAdapter(data,
                new RecipesListAdapter.RecyclerViewCallback() {
                    @Override
                    public void onRecipeClick(Recipe recipe) {
                        fragmentInteractionListener.onRecipeClick(recipe);
                    }
                });
    }

    public interface OnFragmentInteractionListener {
        void onRecipeClick(Recipe recipe);
    }
}
