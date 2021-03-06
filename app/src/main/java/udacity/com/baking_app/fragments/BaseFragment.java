package udacity.com.baking_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    final static String BUNDLE_FRAGMENT_PARAMS_KEY = "bundle-fragment-params-key";
    private Unbinder unbinder;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        checkSavedInstanceStateOnCreateView(savedInstanceState);
        initUi();
        return view;
    }


    protected  void checkSavedInstanceStateOnCreateView(Bundle savedInstanceState){
    }

    protected abstract int getFragmentLayout();

    protected  void initUi(){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
