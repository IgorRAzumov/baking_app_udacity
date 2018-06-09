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
import udacity.com.baking_app.data.StepMedia;


public class RecipeDetailFragment extends BaseFragment {
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

    public void setCurrentDetailItem(int recipeDetailPosition) {
        viewPagerPosition = recipeDetailPosition;
        if (recipeViewPager != null) {
            recipeViewPager.setCurrentItem(viewPagerPosition);
        }

    }

    private void showMedia() {
        Fragment fragment = getChildFragmentManager()
                .findFragmentByTag(getString(R.string.player_fragment_tag));
        if (fragment == null) {
            return;
        }
        ((PlayerFragment) fragment).initMedia(
                (viewPagerPosition == RecipeDetailPageAdapter.INGREDIENTS_POSITION)
                        ? null
                        : StepMedia.initStepMedia(recipeDetailAdapter.getRecipeDetail(viewPagerPosition)));
    }

    private void initViewPager() {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            return;
        }
        recipeViewPager.setOffscreenPageLimit(getResources()
                .getInteger(R.integer.recipe_detail_fr_offscreen_page_limit));
        recipeViewPager.setAdapter(recipeDetailAdapter);
        recipeViewPager.addOnPageChangeListener(createViewPagerListener());
        recipeViewPager.setCurrentItem(viewPagerPosition, false);

    }

    @NonNull
    private ViewPager.OnPageChangeListener createViewPagerListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPosition = position;
                fragmentInteractionListener.changedContent(position);
                showMedia();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }

    public interface OnFragmentInteractionListener {
        void changedContent(int position);
    }
}
