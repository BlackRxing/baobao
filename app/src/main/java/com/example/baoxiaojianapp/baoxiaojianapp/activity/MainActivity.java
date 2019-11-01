package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.AppraisalFragment;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity{

    private FrameLayout mainframelayout;
    private BottomNavigationView bottomNavigationView;
    private AppraisalFragment appraisalFragmentProvider;
    private PersonFragment personFragmentProvider;
    private List<Fragment> fragments;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switchfragment(menuItem.getItemId());
            return true;
        }
    };

    private void initFragments(){
        fragments=new ArrayList<>();
        fragments.add(new AppraisalFragment());
        fragments.add(new PersonFragment());
    }

    private void switchfragment(int itemId){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        switch (itemId){
            case R.id.main_appraisal:
                fragmentTransaction.replace(R.id.fragment_frame,new AppraisalFragment());
                break;
            case R.id.main_person:
                fragmentTransaction.replace(R.id.fragment_frame,new PersonFragment());
                break;
        }
    }
    private void setFragmentsPosition(int position){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(position));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }
}
