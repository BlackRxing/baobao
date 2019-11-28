package com.example.baoxiaojianapp.baoxiaojianapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.User;

public class UserInfoCashUtils {
    private static UserInfoCashUtils userInfoCashUtils;


    public static UserInfoCashUtils getInstance(){
        if ( userInfoCashUtils== null){
            synchronized (UserInfoCashUtils.class){
                if (userInfoCashUtils == null){
                    userInfoCashUtils=new UserInfoCashUtils();
                }
            }
        }
        return userInfoCashUtils;
    }
    public static void setUserInfo(String key,String value){
        SharedPreferences.Editor editor=MyApplication.getContext().getSharedPreferences("userinfo_cash", Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static void setLogin(){
        SharedPreferences.Editor editor=MyApplication.getContext().getSharedPreferences("userinfo_cash", Context.MODE_PRIVATE).edit();
        editor.putBoolean("login_state",true);
        editor.apply();
    }

    public static void saveUserInfoCash(User user){
        SharedPreferences.Editor editor=MyApplication.getContext().getSharedPreferences("userinfo_cash", Context.MODE_PRIVATE).edit();
        editor.putString("username",user.getUsername());
        editor.putInt("id",user.getId());
        editor.putString("avatar_url",user.getAvatar_url());
        editor.putString("phone_num",user.getPhone_num());
        editor.putString("nick_name",user.getNick_name());
        editor.putString("sex",user.getSex());
        editor.putString("location",user.getLocation());
        editor.putInt("point",user.getPoint());
        editor.putBoolean("is_enterprise",user.getIs_enterprise());
        editor.putInt("third_party_type",user.getThird_party_type());
        editor.putString("turing_token",user.getTuring_token());
        editor.putString("weibo_token",user.getWeibo_token());
        editor.putString("weixin_token",user.getWeixin_token());
        editor.putString("open_id",user.getOpen_id());
        editor.putInt("hasPunch",user.getHasPunch());
        editor.apply();
    }

    public static boolean getUserInfoBoolean(String key){
        SharedPreferences sharedPreferences=MyApplication.getContext().getSharedPreferences("userinfo_cash",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
    public static int getUserInfoInt(String key){
        SharedPreferences sharedPreferences=MyApplication.getContext().getSharedPreferences("userinfo_cash",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }

    public static String getUserInfo(String key){
        SharedPreferences sharedPreferences=MyApplication.getContext().getSharedPreferences("userinfo_cash",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    public static boolean getUserLoginState(){
        SharedPreferences sharedPreferences=MyApplication.getContext().getSharedPreferences("userinfo_cash",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("login_state",false);
    }
    public static int getUserLoginState(String key){
        SharedPreferences sharedPreferences=MyApplication.getContext().getSharedPreferences("userinfo_cash",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }


    /*
    清空用户信息缓存
    */
    public static void clearUserInfoCash(){
        SharedPreferences.Editor editor=MyApplication.getContext().getSharedPreferences("userinfo_cash",Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
