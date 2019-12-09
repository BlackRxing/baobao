package com.example.baoxiaojianapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.classpakage.AppraisalPointItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppraisalPointAdapter extends RecyclerView.Adapter<AppraisalPointAdapter.ViewHolder>{
    private List<AppraisalPointItem> appraisalPointItemList;
    private View itemView;

    public AppraisalPointAdapter(List<AppraisalPointItem> appraisalPointItemList){
        this.appraisalPointItemList=appraisalPointItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.appraisalpoint_item,parent,false);
        itemView=view;
        final ViewHolder holder=new ViewHolder(view);
        return holder;
}

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AppraisalPointItem appraisalPointItem=appraisalPointItemList.get(position);
        Glide.with(MyApplication.getContext()).load(appraisalPointItem.getPointimageUrl()).centerCrop().into(holder.imageView);
        holder.textView.setText(appraisalPointItem.getPointtext());
    }


    @Override
    public int getItemCount() {
        return appraisalPointItemList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(View view){
            super(view);
            imageView=view.findViewById(R.id.appraisalpoint_image);
            textView=view.findViewById(R.id.appraisalpoint_text);
        }
    }
}
