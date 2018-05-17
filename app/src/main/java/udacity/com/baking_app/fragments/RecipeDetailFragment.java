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
import udacity.com.baking_app.data.Step;


public class RecipeDetailFragment extends BaseFragment {
    public static final String TAG = RecipeDetailFragment.class.getCanonicalName();

    @BindView(R.id.vp_fragment_recipe_detail)
    ViewPager recipeViewPager;

    private int viewPagerPosition;

    private RecipeDetailPageAdapter recipeDetailAdapter;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle recipeDetailBundle = Objects.requireNonNull(getArguments())
                .getBundle(getString(R.string.bundle_fragment_params_key));


        Context context = getContext();
        if (context != null) {
            Recipe recipe = Objects.requireNonNull(recipeDetailBundle)
                    .getParcelable(getString(R.string.recipe_key));
            recipeDetailAdapter = new RecipeDetailPageAdapter(context, getFragmentManager(),
                    Objects.requireNonNull(recipe));
            viewPagerPosition = recipeDetailBundle.getInt(getString(R.string.recipe_detail_item_key));
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

    private void initViewPager() {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            return;
        }
        recipeViewPager.setAdapter(recipeDetailAdapter);
        recipeViewPager.setCurrentItem(viewPagerPosition, false);
        recipeViewPager.addOnPageChangeListener(createViewPagerListener());

        //recipeViewPager.setOffscreenPageLimit(recipeDetailAdapter.getCount());
    }

    @NonNull
    private ViewPager.OnPageChangeListener createViewPagerListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != RecipeDetailPageAdapter.INGREDIENTS_POSITION) {
                    ((RecipeStepFragment) recipeDetailAdapter.getItem(viewPagerPosition))
                            .imHiddenNow();

                }

                viewPagerPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                if (position != RecipeDetailPageAdapter.INGREDIENTS_POSITION) {
                    ((RecipeStepFragment) recipeDetailAdapter.getItem(position))
                            .imVisibleNow();
                }
                viewPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }


    public void showStepInfo(Step step) {

    }
}
