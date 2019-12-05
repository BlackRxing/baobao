package com.example.baoxiaojianapp.baoxiaojianapp.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.AppraisalResultActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalResult;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.AppraisalFragment;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.GenuineFragment;

import java.util.List;
import java.util.SimpleTimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class AppraisalItemAdapter extends RecyclerView.Adapter<AppraisalItemAdapter.ViewHolder>{
    private List<AppraisalResult> appraisalResults;
    private View itemView;
    private AppraisalItemAdapterClick appraisalItemAdapterClick;
    private long mLastClickTime=0;
    public static final long TIME_INTERVAL = 500L;



    public AppraisalItemAdapter(List<AppraisalResult> appraisalResults){
        this.appraisalResults=appraisalResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.appraisal_item,parent,false);
        itemView=view;
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        AppraisalResult appraisalResult=appraisalResults.get(position);
        holder.appraisalBrand.setText(appraisalResult.getAppraisalBrand());
        holder.appraisalId.setText("鉴定号:"+appraisalResult.getAppraisalId());
        holder.appraisalData.setText(appraisalResult.getAppraisalData());
        Glide.with(MyApplication.getContext()).load(appraisalResult.getAppraisalImage()).centerCrop().into(holder.appraisalImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nowTime=System.currentTimeMillis();
                if (nowTime-mLastClickTime>TIME_INTERVAL){
                    mLastClickTime=nowTime;
                    //appraisalItemAdapterClick.onItemClick(position);
                    AppraisalResult appraisalResult=appraisalResults.get(position);
                    Intent intent=new Intent(GenuineFragment.mContext, AppraisalResultActivity.class);
                    intent.putExtra("imageUrl",appraisalResult.getAppraisalImage());
                    intent.putExtra("brandName",appraisalResult.getAppraisalBrand());
                    intent.putExtra("modelNumber",appraisalResult.getAppraisalId());
                    intent.putExtra("timestamp",appraisalResult.getAppraisalData());
                    intent.putExtra("type",appraisalResult.getType());
                    intent.putExtra("detailModels",appraisalResult.getDetailModels());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appraisalResults.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appraisalImage;
        TextView appraisalBrand;
        TextView appraisalData;
        TextView appraisalId;
        public ViewHolder(View view){
            super(view);
            appraisalImage=view.findViewById(R.id.appraisal_image);
            appraisalBrand=view.findViewById(R.id.appraisal_brand);
            appraisalData=view.findViewById(R.id.appraisal_data);
            appraisalId=view.findViewById(R.id.appraisal_id);
        }
    }

    public interface AppraisalItemAdapterClick{
        void onItemClick(int position);
    }

    public void setItemClick(AppraisalItemAdapterClick appraisalItemAdapterClick){
        this.appraisalItemAdapterClick=appraisalItemAdapterClick;
    }



}
