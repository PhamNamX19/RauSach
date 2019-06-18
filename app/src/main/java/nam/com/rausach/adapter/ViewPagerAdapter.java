package nam.com.rausach.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

import nam.com.rausach.fragment.DetailFragment;
import nam.com.rausach.fragment.OverviewFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<String> titles;

    public ViewPagerAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new OverviewFragment();
            case 1:
                return new DetailFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if (position == 0) {
            title = this.titles.get(0);
        } else {
            title = this.titles.get(1);
        }
        return title;
    }
}