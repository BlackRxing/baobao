package com.example.baoxiaojianapp.baoxiaojianapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.MainActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalPointItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GreenHandAdapter extends RecyclerView.Adapter<GreenHandAdapter.ViewHolder>{
    private List<AppraisalPointItem> appraisalPointItemList;
    private View itemView;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.greenhanditemlayout,parent,false);
        itemView=view;
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AppraisalPointItem appraisalPointItem=appraisalPointItemList.get(position);
        Glide.with(MyApplication.getContext()).load(appraisalPointItem.getPointimageUrl()).centerCrop().into(holder.imageView);
        holder.pointtitle.setText(appraisalPointItem.getPointtext());
        holder.pointcontent.setText(appraisalPointItem.getPointcontent());
    }

    @Override
    public int getItemCount() {
        return appraisalPointItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView pointtitle;
        TextView pointcontent;
        public ViewHolder(View view){
            super(view);
            imageView=view.findViewById(R.id.pointImage);
            pointtitle=view.findViewById(R.id.pointtitle);
            pointcontent=view.findViewById(R.id.pointcontent);
        }
    }
}
