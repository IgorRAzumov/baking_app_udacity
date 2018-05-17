package udacity.com.baking_app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class RecipeDetailPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList;

    public RecipeDetailPageAdapter(ArrayList<Fragment> fragmentList, FragmentManager fm) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null
                ? 0
                : fragmentList.size();
    }
}
