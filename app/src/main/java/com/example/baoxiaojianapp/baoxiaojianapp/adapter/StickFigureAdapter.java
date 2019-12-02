package com.example.baoxiaojianapp.baoxiaojianapp.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.AppraisalActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalPointItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickFigureAdapter extends RecyclerView.Adapter<StickFigureAdapter.ViewHolder>{
    private List<AppraisalPointItem> appraisalPointItemList;

    public  List<StickFigureAdapter.ViewHolder> holders=new ArrayList<>();


    public StickFigureAdaterClick stickFigureAdaterClick;
    public StickFigureAdapter(List<AppraisalPointItem> appraisalPointItemList){
        this.appraisalPointItemList=appraisalPointItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.stcikfigure_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AppraisalPointItem appraisalPointItem=appraisalPointItemList.get(position);
        Glide.with(MyApplication.getContext()).load(appraisalPointItem.getStickFigureURL()).fitCenter().into(holder.stickFigure);
        if (appraisalPointItem.getType() == 0) {
            holder.typeImage.setVisibility(View.INVISIBLE);
        } else {
            holder.typeImage.setVisibility(View.VISIBLE);
        }
        holder.pointName.setText(appraisalPointItem.getPointtext());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickFigureAdaterClick.onItemClick(position,holder);
            }
        });
        holders.add(holder);
    }

    @Override
    public int getItemCount() {
        return appraisalPointItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView stickFigure;
        TextView pointName;
        ImageView typeImage;
        public ViewHolder(View view){
            super(view);
            stickFigure=view.findViewById(R.id.stickFigure);
            pointName=view.findViewById(R.id.pointName);
            typeImage=view.findViewById(R.id.vitalpoint);
        }
    }

    public interface StickFigureAdaterClick{
        void onItemClick(int position,ViewHolder viewHolder);
    }

    public void setItemClick(StickFigureAdaterClick stickFigureAdaterClick){
        this.stickFigureAdaterClick=stickFigureAdaterClick;
    }
}
