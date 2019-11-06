package com.example.baoxiaojianapp.baoxiaojianapp.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalResult;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.AppraisalFragment;

import java.util.List;
import java.util.SimpleTimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AppraisalItemAdapter extends RecyclerView.Adapter<AppraisalItemAdapter.ViewHolder>{
    private List<AppraisalResult> appraisalResults;
    private View itemView;

    public AppraisalItemAdapter(List<AppraisalResult> appraisalResults){
        this.appraisalResults=appraisalResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appraisal_item,parent,false);
        itemView=view;
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AppraisalResult appraisalResult=appraisalResults.get(position);
        holder.appraisalBrand.setText(appraisalResult.getAppraisalBrand());
        holder.appraisalId.setText(appraisalResult.getAppraisalId());
        holder.appraisalData.setText(appraisalResult.getAppraisalData());
        Glide.with(MyApplication.getContext()).load(appraisalResult.getAppraisalImage()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.appraisalImage.setBackground(resource);
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

}
