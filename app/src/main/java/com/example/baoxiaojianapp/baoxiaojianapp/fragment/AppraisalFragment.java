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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.jsonclass.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class AppraisalFragment extends Fragment implements View.OnClickListener {

    private CardView shoeCard;
    private CardView bagCard;
    private CardView watchCard;
    private LinearLayout subclassLayout;
    private RelativeLayout mainappraisalLayout;
    private Button subclassButton;
    private TextView subclassText;
    private RelativeLayout subclassCardViews;
    private static final String[] subClasscategory=new String[]{"bag","shoe","watch"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPic();
    }

    public void init(){
        shoeCard.setOnClickListener(this);
        bagCard.setOnClickListener(this);
        watchCard.setOnClickListener(this);
        subclassButton.setOnClickListener(this);
    }

    private void loadPic(){

        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        okHttpUtils.post(NetInterface.TSAppraisalPageReques,requestBody,new OkHttpUtils.RealCallback() {
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

    private void showSubclassPic(JSONArray jsonArray){
        int subkind=jsonArray.length();
        String subclassImageUrl=null;
        String brandName="";
        int i;
        Log.d("cardView",String.valueOf(subkind));
        for(i=0;i<subkind;i++){
            try {
                subclassImageUrl=jsonArray.getJSONObject(i).getString("imageUrl");
                brandName=jsonArray.getJSONObject(i).getString("brandName");
            }catch (JSONException j){
                j.printStackTrace();
            }
            final CardView cardView=(CardView)subclassCardViews.getChildAt(i);
            cardView.setVisibility(View.VISIBLE);
            TextView textView=(TextView) cardView.getChildAt(1);
            textView.setText(brandName);
            Glide.with(getView()).load(subclassImageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                     cardView.getChildAt(0).setBackground(resource);
                }
            });
        }
        for (;i<6;i++){
            subclassCardViews.getChildAt(i).setVisibility(View.INVISIBLE);
        }


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
        mainappraisalLayout=view.findViewById(R.id.mainappraisal_layout);
        subclassLayout=view.findViewById(R.id.mainappraisal_subclass_layout);
        subclassButton=view.findViewById(R.id.mainappraisal_subclass_button);
        subclassText=view.findViewById(R.id.subclass_text);
        subclassCardViews=view.findViewById(R.id.subclass_cardviews);
        init();
        return view;
    }

    private void showSubclass(int i){
        switch (i){
            case 0:
                subclassText.setText("包");
                break;
            case 1:
                subclassText.setText("鞋");
                break;
            case 2:
                subclassText.setText("表");
                break;
        }
        subclassLayout.setVisibility(View.VISIBLE);
        mainappraisalLayout.setVisibility(View.INVISIBLE);
        JSONObject json = new JSONObject();
        try {
            json.put("type", subClasscategory[i]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        String subclassrequest=NetInterface.TSCategoryPageRequest;
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.post(subclassrequest,requestBodyJson,new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray=jsonObject.getJSONArray("appraisalBrand");
                    showSubclassPic(jsonArray);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainappraisal_subclass_button:
                subclassLayout.setVisibility(View.INVISIBLE);
                mainappraisalLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.bag_card:
                showSubclass(0);
                break;
            case R.id.shoe_card:
                showSubclass(1);
                break;
            case R.id.watch_card:
                showSubclass(2);
                break;
        }
    }
}
