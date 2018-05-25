package udacity.com.baking_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;


public class RecipeStepFragment extends BaseFragment {
    public static final String TAG = RecipeStepFragment.class.getCanonicalName();
    @BindView(R.id.tv_fragment_recipe_step_description)
    TextView stepDescriptionText;

    private String stepText;

    public RecipeStepFragment() {

    }

    public static RecipeStepFragment newInstance(@NonNull Bundle bundle) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle stepBundle = Objects.requireNonNull(getArguments())
                .getBundle(BUNDLE_FRAGMENT_PARAMS_KEY);
        stepText = Objects.requireNonNull(stepBundle)
                .getString(getString(R.string.step_text_key));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recipe_step;
    }

    @Override
    protected void initUi() {
        stepDescriptionText.setText(stepText);
    }
}
