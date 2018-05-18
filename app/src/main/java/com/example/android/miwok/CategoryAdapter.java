package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class CategoryAdapter extends FragmentPagerAdapter {

    private Context myContext;

    private String tabTitles[];



    CategoryAdapter(Context c, FragmentManager fm) {
        super(fm);

        if (c == null) {
            Log.e("ERROR", "NULL CONTEXT");
        }

        myContext = c;

        tabTitles = new String[] { myContext.getString(R.string.category_numbers),
                myContext.getString(R.string.category_family), myContext.getString(R.string.category_colors),
                myContext.getString(R.string.category_phrases) };
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new FamilyFragment();
        } else if (position == 2) {
            return new ColorsFragment();
        } else {
            return new PhrasesFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
}
