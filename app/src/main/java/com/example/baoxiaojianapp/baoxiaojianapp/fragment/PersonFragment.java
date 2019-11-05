package com.example.baoxiaojianapp.baoxiaojianapp.fragment;

import android.content.Context;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baoxiaojianapp.R;
import com.google.android.material.tabs.TabLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;


public class PersonFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> titleList=new ArrayList<>();
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private GenuineFragment genuineFragment;
    private FakeFragment fakeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    private void init(){
        titleList.add("真货");
        titleList.add("假货");
        genuineFragment=new GenuineFragment();
        fakeFragment=new FakeFragment();
        fragments.add(genuineFragment);
        fragments.add(fakeFragment);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titleList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_person, container, false);
        tabLayout=view.findViewById(R.id.person_tablayout);
        viewPager=view.findViewById(R.id.person_viewpager);
        init();
        return view;
    }

}
