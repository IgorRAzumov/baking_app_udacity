package udacity.com.baking_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.adapters.RecipeDetailPageAdapter;
import udacity.com.baking_app.data.Recipe;


public class RecipeDetailFragment extends BaseFragment
        implements RecipeStepFragment.OnFragmentInteractionListener {
    public static final String TAG = RecipeDetailFragment.class.getCanonicalName();

    @BindView(R.id.vp_fragment_recipe_detail)
    ViewPager recipeViewPager;

    private int viewPagerPosition;
    private RecipeDetailPageAdapter recipeDetailAdapter;
    private OnFragmentInteractionListener fragmentInteractionListener;


    public static Fragment newInstance(@NonNull Bundle bundle) {
        Fragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDetailFragment() {
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
        Context context = getContext();
        if (context != null) {
            Bundle bundle = Objects.requireNonNull(getArguments())
                    .getBundle(getString(R.string.bundle_fragment_params_key));
            String recipeKey = getString(R.string.recipe_key);
            Recipe recipe = Objects.requireNonNull(bundle).getParcelable(recipeKey);
            viewPagerPosition = bundle
                    .getInt(getString(R.string.recipe_content_position_key));

            recipeDetailAdapter = new RecipeDetailPageAdapter(context, getChildFragmentManager(),
                    Objects.requireNonNull(recipe));
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recipe_detail;
    }

    @Override
    protected void initUi() {
        initViewPager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }


    public void showFullScreenMode() {
        Fragment fragment = recipeDetailAdapter.getCurrentFragment();
        if (fragment == null) {
            return;
        }
        ((RecipeStepFragment) fragment).showFullscreenMode();
    }


    public void showDefaultMode() {
        Fragment fragment = recipeDetailAdapter.getCurrentFragment();
        if (fragment == null) {
            return;
        }
        ((RecipeStepFragment) fragment).showDefaultMode();

    }

    private void initViewPager() {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            return;
        }
        recipeViewPager.setOffscreenPageLimit(getResources()
                .getInteger(R.integer.recipe_detail_fr_offscreen_page_limit));
        recipeViewPager.setAdapter(recipeDetailAdapter);
        recipeViewPager.setCurrentItem(viewPagerPosition, false);
        recipeViewPager.addOnPageChangeListener(createViewPagerListener());

    }



    @NonNull
    private ViewPager.OnPageChangeListener createViewPagerListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println(position);
            }

            @Override
            public void onPageSelected(int position) {
            /*    if (position != ingredientsFragmentPosition) {
                    ((RecipeStepFragment) recipeDetailAdapter.getItem(position))
                            .imVisibleNow();
                }*/
/*
                if (viewPagerPosition != ingredientsFragmentPosition) {
                    ((RecipeStepFragment) recipeDetailAdapter.getItem(viewPagerPosition))
                            .imHiddenNow();
                }*/

                viewPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }


    public int getRecipeDetailPosition() {
        return recipeViewPager.getCurrentItem();
    }

    public boolean isCurrentItemContainsVideo() {
        boolean isContain = false;
        Fragment fragment = recipeDetailAdapter.getCurrentFragment();
        if (fragment != null && fragment instanceof RecipeStepFragment) {
            isContain = ((RecipeStepFragment) fragment).isContainsVideo();
        }
        return isContain;
    }

    public void setCurrentDetailItem(int recipeDetailPosition) {
        recipeViewPager.setCurrentItem(recipeDetailPosition);
    }


    public interface OnFragmentInteractionListener {
    }
}
