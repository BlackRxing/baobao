package com.example.baoxiaojianapp.baoxiaojianapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.jsonclass.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class AppraisalFragment extends Fragment {

    private CardView shoeCard;
    private CardView bagCard;
    private CardView watchCard;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPic();
    }

    private void bindView(){
        shoeCard=getView().findViewById(R.id.shoe_card);
        bagCard=getView().findViewById(R.id.bag_card);
        watchCard=getView().findViewById(R.id.watch_card);
    }

    private void loadPic(){

        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        okHttpUtils.post("https://dev2.turingsenseai.com/appraisalIndex",requestBody,new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray=jsonObject.getJSONArray("appraisalKindList");
                    showPic(jsonArray);
                    Log.i("return info",jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j){
                    j.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void showPic(JSONArray jsonArray){
        try {
            String imageurl=jsonArray.getJSONObject(0).getString("imageUrl");
            Glide.with(getView()).load(imageurl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    bagCard.setBackground(resource);
                }
            });
            String imageurl2=jsonArray.getJSONObject(1).getString("imageUrl");
            Glide.with(getView()).load(imageurl2).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    shoeCard.setBackground(resource);
                }
            });
            String imageurl3=jsonArray.getJSONObject(2).getString("imageUrl");
            Glide.with(getView()).load(imageurl3).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    watchCard.setBackground(resource);
                }
            });
        }catch (JSONException j){
            j.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_appraisal, container, false);
        shoeCard=view.findViewById(R.id.shoe_card);
        bagCard=view.findViewById(R.id.bag_card);
        watchCard=view.findViewById(R.id.watch_card);
        return view;
    }

}
