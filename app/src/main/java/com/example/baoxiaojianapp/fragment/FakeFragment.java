package com.example.baoxiaojianapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.Utils.RecyclerViewSpacesItemDecoration;
import com.example.baoxiaojianapp.adapter.AppraisalItemAdapter;
import com.example.baoxiaojianapp.classpakage.AppraisalResult;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FakeFragment extends Fragment {


    private static SuperSwipeRefreshLayout swipeRefreshLayout;
    public static RecyclerView recyclerView;
    public static AppraisalItemAdapter appraisalItemAdapter;
    public static boolean hasMoreData;
    private static ImageView holderImage;


    public static List<AppraisalResult> appraisalResults=new ArrayList<>();

    // Footer View
    private ProgressBar footerProgressBar;
    private TextView footerTextView;
    private ImageView footerImageView;
    private View footerView;
    private static GenuineFragment genuineFragment;
    private int currentPage=1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static void init(){
        if(hasMoreData){
            holderImage.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }else{
            holderImage.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
        }
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
        holderImage=view.findViewById(R.id.image_noresult_holder);
        //设置item间距
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
                if (hasMoreData){
                    currentPage++;
                    Callback.FakeloadMore(getActivity(),currentPage);
                    footerTextView.setText("正在加载...");
                    footerImageView.setVisibility(View.GONE);
                    footerProgressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            footerView.setVisibility(View.GONE);
                            swipeRefreshLayout.setLoadMore(false);
                            appraisalItemAdapter.notifyItemRangeInserted(appraisalItemAdapter.getItemCount(),Callback.fakeitemlength);
                        }
                    }, 3000);
                }else {
                    ToastUtils.showShort("没有更多数据");
                    footerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setLoadMore(false);
                }

            }

            @Override
            public void onPushEnable(boolean enable) {
                footerView.setVisibility(View.VISIBLE);
                footerTextView.setText(enable ? "松开加载" : "上拉加载");
                footerImageView.setVisibility(View.VISIBLE);
                footerImageView.setRotation(enable ? 0 : 180);
                footerProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPushDistance(int distance) {
                // TODO Auto-generated method stub
                footerView.setVisibility(View.VISIBLE);
            }

        });
        return view;
    }


    @Override
    public void onResume() {
        appraisalResults.clear();
        Callback.FakeloadData(getActivity());
        super.onResume();
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
