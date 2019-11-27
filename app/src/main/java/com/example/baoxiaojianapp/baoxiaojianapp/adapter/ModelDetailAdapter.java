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
import com.example.baoxiaojianapp.baoxiaojianapp.activity.MainActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.DetailModels;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ModelDetailAdapter extends RecyclerView.Adapter<ModelDetailAdapter.ViewHolder>{
    List<DetailModels> detailModelsList;
    private View itemView;

    public ModelDetailAdapter(List<DetailModels> detailModels){
        this.detailModelsList=detailModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.detailmodel_layout,parent,false);
        itemView=view;
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DetailModels detailMoels=detailModelsList.get(position);

        Glide.with(MyApplication.getContext()).load(detailMoels.getImageUrl()).centerCrop().into(holder.cardView);
        if (detailMoels.getFakeOrReal()==0){
            Glide.with(MyApplication.getContext()).load(R.drawable.tick).into(holder.pointState);
        }else {
            Glide.with(MyApplication.getContext()).load(R.drawable.fork).into(holder.pointState);
        }
        holder.pointGrade.setText(detailMoels.getGrade()+"");
        holder.pointName.setText(detailMoels.getTitle());
    }

    @Override
    public int getItemCount() {
        return detailModelsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardView;
        ImageView pointState;
        TextView pointName;
        TextView pointGrade;
        public ViewHolder(View view){
            super(view);
            cardView=view.findViewById(R.id.detailmodel_cardview);
            pointState=view.findViewById(R.id.detail_state);
            pointName=view.findViewById(R.id.pointName);
            pointGrade=view.findViewById(R.id.detail_grade);
        }
    }
}
