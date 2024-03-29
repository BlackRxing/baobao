package com.example.baoxiaojianapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.activity.InfoSettingActivity;
import com.example.baoxiaojianapp.activity.LoginActivity;
import com.example.baoxiaojianapp.activity.SettingActivity;
import com.example.baoxiaojianapp.classpakage.User;
import com.example.baoxiaojianapp.view.SigninDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class PersonFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> titleList=new ArrayList<>();
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private GenuineFragment genuineFragment;
    private FakeFragment fakeFragment;

    private View view;

    private LinearLayout settingButton;
    private CircleImageView profileImage;
    private TextView usernameText;
    private LinearLayout editLinearLayout;

    private RelativeLayout onlineTop;
    private RelativeLayout offlineTop;
    private Button loginButton;

    private RelativeLayout personcreditLayout;
    private TextView creditText;
    private Button signinButton;
    private Button hasSigninButton;
    private TextView enterpriseLayout;

    private long mLastClickTime=0;
    public static final long TIME_INTERVAL = 500L;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_person, container, false);
        tabLayout=view.findViewById(R.id.person_tablayout);
        viewPager=view.findViewById(R.id.person_viewpager);
        settingButton=view.findViewById(R.id.setting_button);
        profileImage=view.findViewById(R.id.profile_image);
        usernameText=view.findViewById(R.id.username_text);
        editLinearLayout=view.findViewById(R.id.editinfo_layout);
        onlineTop=view.findViewById(R.id.online_top);
        offlineTop=view.findViewById(R.id.offline_top);
        loginButton=view.findViewById(R.id.login_button);

        personcreditLayout=view.findViewById(R.id.personcredit_layout);
        enterpriseLayout=view.findViewById(R.id.enterprise_layout);
        creditText=view.findViewById(R.id.credit);
        signinButton=view.findViewById(R.id.signin_button);
        hasSigninButton=view.findViewById(R.id.hassignin_button);

        editLinearLayout.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        signinButton.setOnClickListener(this);


        return view;
    }



    private void initCredit(){
        if (UserInfoCashUtils.getUserInfoInt("is_enterprise")== Constants.ENTERPRISE){
            personcreditLayout.setVisibility(View.GONE);
            enterpriseLayout.setVisibility(View.VISIBLE);
        }else{
            personcreditLayout.setVisibility(View.VISIBLE);
            enterpriseLayout.setVisibility(View.GONE);
            creditText.setText(UserInfoCashUtils.getUserInfoInt("point")+"");
            if (UserInfoCashUtils.getUserInfoInt("hasPunch")==Constants.hasPunch){
                hasSigninButton.setVisibility(View.VISIBLE);
                signinButton.setVisibility(View.GONE);
            }else{
                hasSigninButton.setVisibility(View.GONE);
                signinButton.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    {
    }



    private void showUserInfo() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        Glide.with(getView()).load(sharedPreferences.getString("avatar_url","")).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                profileImage.setImageDrawable(resource);
            }
        });
        usernameText.setText(sharedPreferences.getString("nick_name",""));
    }



    private void initTop(){
        onlineTop.setVisibility(View.VISIBLE);
        offlineTop.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        Callback.refreshUserinfo(new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                    UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                    userInfoCashUtils.saveUserInfoCash(user);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showUserInfo();
                            initCredit();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    private void setOfflineTop(){
        onlineTop.setVisibility(View.GONE);
        offlineTop.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewTopinit();
        init();
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        viewTopinit();
        super.onResume();
    }

    private void viewTopinit(){
        if (UserInfoCashUtils.getUserLoginState()){
            initTop();
        }else{
            setOfflineTop();
        }
    }

    private void signin(){
        Callback.signin(new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try{
                    Log.d("signin",jsonObject.getInt("code")+"");
                    if (jsonObject.getInt("code")==Constants.CODE_SUCCESS){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final SigninDialog signinDialog=new SigninDialog(getContext());
                                signinDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                signinDialog.findViewById(R.id.dismiss_button).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        signinDialog.dismiss();
                                    }
                                });
                                signinDialog.show();
                                ((TextView)signinDialog.findViewById(R.id.signintext)).setText(getText(R.string.signinstring1));
                                viewTopinit();
                            }
                        });

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    ToastUtils.showShort("签到失败");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        long nowTime=System.currentTimeMillis();
        if (nowTime-mLastClickTime>TIME_INTERVAL){
            mLastClickTime=nowTime;
            switch (v.getId()){
                case R.id.editinfo_layout:
                    startActivity(new Intent(getContext(), InfoSettingActivity.class));
                    break;
                case R.id.setting_button:
                    startActivity(new Intent(getContext(), SettingActivity.class));
                    break;
                case R.id.login_button:
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    break;
                case R.id.signin_button:
                    signin();
                    break;
            }
        }
    }
}
