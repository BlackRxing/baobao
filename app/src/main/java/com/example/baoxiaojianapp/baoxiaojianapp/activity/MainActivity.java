package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.AppraisalFragment;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private FrameLayout mainframelayout;
    public static BottomNavigationView bottomNavigationView;
    private AppraisalFragment appraisalFragmentProvider;
    private PersonFragment personFragmentProvider;
    private List<Fragment> fragments;
    private int lastFragment=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // FRAGMENTS_TAG
            savedInstanceState.remove("android:support:fragments");
            savedInstanceState.remove("android:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        init();
    }

    private void init(){
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        initFragments();
    }

    private void bindView(){
        if (getIntent().getExtras()!=null&&getIntent().getExtras().getString("success")!=null)
            ToastUtils.showShort(getIntent().getExtras().getString("success"));
        bottomNavigationView=findViewById(R.id.bottom_navigation);
    }

    private void initFragments(){
        fragments=new ArrayList<>();
        fragments.add(new AppraisalFragment());
        fragments.add(new PersonFragment());
        lastFragment=0;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame,fragments.get(0)).show(fragments.get(0)).commit();
    }




    private void switchfragment(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        if(lastFragment==0){
            lastFragment=1;
            fragmentTransaction.hide(fragments.get(0));
            if (!fragments.get(1).isAdded()) {
                fragmentTransaction.add(R.id.fragment_frame,fragments.get(1));
            }
            fragmentTransaction.show(fragments.get(1)).commit();
        }else {
            lastFragment=0;
            fragmentTransaction.hide(fragments.get(1));
            if (!fragments.get(0).isAdded()) {
                fragmentTransaction.add(R.id.fragment_frame,fragments.get(0));
            }
            fragmentTransaction.show(fragments.get(0)).commit();
        }
    }




    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.main_appraisal:
                if(lastFragment!=0){
                    switchfragment();
                }
                return true;
            case R.id.main_person:
                if(lastFragment!=1){
                    switchfragment();
                }
                return true;
        }
        return false;
    }



}