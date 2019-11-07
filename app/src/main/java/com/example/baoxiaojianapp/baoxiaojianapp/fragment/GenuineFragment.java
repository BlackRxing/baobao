package com.example.baoxiaojianapp.baoxiaojianapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.RecyclerViewSpacesItemDecoration;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.AppraisalItemAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalResult;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.LoginRequest;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.User;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GenuineFragment extends Fragment {

    private SuperSwipeRefreshLayout swipeRefreshLayout;
    public static RecyclerView recyclerView;
    public static AppraisalItemAdapter appraisalItemAdapter;
    private static boolean hasMoreData=true;


    public static List<AppraisalResult> appraisalResults=new ArrayList<>();

    // Footer View
    private ProgressBar footerProgressBar;
    private TextView footerTextView;
    private ImageView footerImageView;
    private View footerView;
    private static GenuineFragment genuineFragment;

    private int currentPage=1;



    public static GenuineFragment getInstance(){
        if(genuineFragment==null){
            synchronized (GenuineFragment.class){
                if (genuineFragment==null){
                    genuineFragment=new GenuineFragment();
                }
            }
        }
        return genuineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginTest();
        Callback.loadData(getActivity());
    }


    public void loadData(){
        SharedPreferences sharedPreferences1= MyApplication.getContext().getSharedPreferences("userinfo_cash",MODE_PRIVATE);
        String token=sharedPreferences1.getString("turing_token","");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建RequestBody
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(NetInterface.TSPersonCenterPageRequest)
                .post(requestBody).addHeader("Authorization","Token "+token)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.i("response", result);
                    JSONObject jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("realList");
                    for (int i=0;i<jsonArray.length();i++){
                        AppraisalResult appraisalResult=new AppraisalResult();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        appraisalResult.setAppraisalBrand(jsonObject1.getString("brandName"));
                        appraisalResult.setAppraisalData(jsonObject1.getString("timestamp"));
                        appraisalResult.setAppraisalId(jsonObject1.getString("modelNumber"));
                        appraisalResult.setAppraisalImage(jsonObject1.getString("imageUrl"));
                        appraisalResults.add(appraisalResult);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppraisalItemAdapter appraisalItemAdapter=new AppraisalItemAdapter(appraisalResults);
                            recyclerView.setAdapter(appraisalItemAdapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException j){
                    j.printStackTrace();
                }
            }
        }).start();

//        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
//        RequestBody requestBody = RequestBody.create(null, new byte[]{});
//        okHttpUtils.post(NetInterface.TSPersonCenterPageRequest, requestBody, new OkHttpUtils.RealCallback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                if (response.isSuccessful()) {
//                    try {
//
////                        JSONObject jsonObject = new JSONObject(response.body().string());
////                        User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
////                        UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
////                        userInfoCashUtils.clearUserInfoCash();
////                        userInfoCashUtils.saveUserInfoCash(user);
//                        Log.i("userCenter",response.body().string());
////                        Log.i("return info", user.getTuring_token());
//                        ToastUtils.showShort("sucesslogin");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
////                    catch (JSONException j) {
////                        j.printStackTrace();
////                    }
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("error", e.toString());
//            }
//        },true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_genuine, container, false);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh);
        recyclerView=view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,7);//top间距
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION,7);//底部间距
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setFooterView(createFooterView());
        swipeRefreshLayout.setTargetScrollWithLayout(true);
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });
        swipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener(){
            @Override
            public void onLoadMore() {
                currentPage++;
                Callback.loadMore(getActivity(),currentPage);
                footerTextView.setText("正在加载...");
                footerImageView.setVisibility(View.GONE);
                footerProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setLoadMore(false);
                        appraisalItemAdapter.notifyItemRangeInserted(appraisalItemAdapter.getItemCount(),Callback.itemlength);
                    }
                }, 3000);
            }

            @Override
            public void onPushEnable(boolean enable) {
                footerView.setVisibility(View.VISIBLE);
                footerTextView.setText(enable ? "松开加载" : "上拉加载");
                footerImageView.setVisibility(View.VISIBLE);
                footerImageView.setRotation(enable ? 0 : 180);
            }

            @Override
            public void onPushDistance(int distance) {
                // TODO Auto-generated method stub
                footerView.setVisibility(View.VISIBLE);
            }

        });
        return view;
    }


    public void LoginTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginType(1);
        loginRequest.setSms("1116");
        loginRequest.setPhoneNum("18051982306");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        final RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson,Callback.LoginTestCallback);
    }

    private View createFooterView() {
        footerView = LayoutInflater.from(swipeRefreshLayout.getContext())
                .inflate(R.layout.layout_footer, null);
        footerProgressBar = (ProgressBar) footerView
                .findViewById(R.id.footer_pb_view);
        footerImageView = (ImageView) footerView
                .findViewById(R.id.footer_image_view);
        footerTextView = (TextView) footerView
                .findViewById(R.id.footer_text_view);
        footerProgressBar.setVisibility(View.GONE);
        footerImageView.setVisibility(View.VISIBLE);
        footerImageView.setImageResource(R.drawable.down_arrow);
        footerTextView.setText("上拉加载更多...");
        return footerView;
    }





}
