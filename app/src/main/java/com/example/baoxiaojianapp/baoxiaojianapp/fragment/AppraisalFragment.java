package com.example.baoxiaojianapp.baoxiaojianapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.MediaType;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.AppraisalNoticeActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.LoginActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.MainActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.SubclassitemAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.Subclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AppraisalFragment extends Fragment implements View.OnClickListener {

    private CardView shoeCard;
    private CardView bagCard;
    private CardView watchCard;
    private LinearLayout subclassLayout;
    private RelativeLayout mainappraisalLayout;
    private RelativeLayout subclassButton;
    private TextView subclassText;
    private ImageView bannerImage;
    private RecyclerView recyclerView;


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
                    String banner=jsonObject.getString("bannerList");
                    showPic(jsonArray,banner);
                    //   Log.i("return info",jsonObject.toString());
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
        List<Subclass> subclassList=new ArrayList<>();
        int subkind=jsonArray.length();
        String subclassImageUrl=null;
        String brandName="";
        String kindKey="";
        for(int i=0;i<subkind;i++){
            try {
                subclassImageUrl=jsonArray.getJSONObject(i).getString("imageUrl");
                brandName=jsonArray.getJSONObject(i).getString("brandName");
                kindKey=jsonArray.getJSONObject(i).getString("kindKey");
            }catch (JSONException j){
                j.printStackTrace();
            }
            Subclass subclass=new Subclass();
            subclass.setSubclassImage(subclassImageUrl);
            subclass.setSubclassText(brandName);
            subclass.setKindKey(kindKey);
            subclassList.add(subclass);
        }
        SubclassitemAdapter subclassitemAdapter=new SubclassitemAdapter(subclassList);
        subclassitemAdapter.setMyAdapterClick(new SubclassitemAdapter.MyAdapterClick() {
            @Override
            public void onItemClick(String brandName,String imageUrl,String kindKey) {
                if(UserInfoCashUtils.getUserLoginState()){
                    Intent intent=new Intent(getContext(), AppraisalNoticeActivity.class);
                    intent.putExtra("brandName",brandName);
                    intent.putExtra("imageUrl",imageUrl);
                    intent.putExtra("kindKey",kindKey);
                    startActivity(intent);
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setMessage(getText(R.string.logintip)).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton(getText(R.string.loginnow), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getContext(), LoginActivity.class));
                                }
                            }).create();
                    alertDialog.show();
                }
            }
        });
        recyclerView.setAdapter(subclassitemAdapter);
        subclassLayout.setVisibility(View.VISIBLE);
        mainappraisalLayout.setVisibility(View.INVISIBLE);

        MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }



    private void showPic(JSONArray jsonArray,String banner){
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
            Glide.with(getView()).load(banner).centerCrop().into(bannerImage);
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
        bannerImage=view.findViewById(R.id.main_banner);

        recyclerView=view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        subclassButton=view.findViewById(R.id.mainappraisal_subclass_button);
        subclassText=view.findViewById(R.id.subclass_text);
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
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
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