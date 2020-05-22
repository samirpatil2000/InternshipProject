package com.example.intern;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> gFragmentList = new ArrayList<>();

    public SectionPagerAdapter(@NonNull FragmentManager fm) {

        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return gFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return gFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        gFragmentList.add(fragment);
    }
}
