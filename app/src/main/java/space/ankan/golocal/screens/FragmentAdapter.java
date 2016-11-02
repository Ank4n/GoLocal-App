package space.ankan.golocal.screens;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankan.
 * Fragment adapter to manage fragments(tabs) in Main Activity
 */

class FragmentAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragments;

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    FragmentAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        mFragments = new ArrayList<>(tabCount);
    }

    void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    void clearFragments() {
        mFragments.clear();
    }
}
