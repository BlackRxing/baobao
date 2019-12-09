package com.example.baoxiaojianapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.classpakage.Subclass;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubclassitemAdapter extends RecyclerView.Adapter<SubclassitemAdapter.ViewHolder> {
    private List<Subclass> subclassList;
    private View itemView;
    public MyAdapterClick myAdapterClick;
    private long mLastClickTime=0;
    public static final long TIME_INTERVAL = 500L;

    public SubclassitemAdapter(List<Subclass> subclassList){
        this.subclassList=subclassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.subclass_item,parent,false);
         itemView=view;
         ViewHolder holder=new ViewHolder(view);
         return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Subclass subclass=subclassList.get(position);
//        Glide.with(MyApplication.getContext()).load(subclass.getSubclassImage()).centerCrop().into(holder.subclassImage);
        holder.subclassText.setText(subclass.getSubclassText());
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));//图片圆角为30
        Glide.with(MyApplication.getContext()).load(subclass.getSubclassImage()).centerCrop().apply(options).into(holder.subclassImage);
        holder.subclassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nowTime=System.currentTimeMillis();
                if (nowTime-mLastClickTime>TIME_INTERVAL){
                    myAdapterClick.onItemClick(subclassList.get(position).getSubclassText(),subclassList.get(position).getSubclassImage(),subclassList.get(position).getKindKey());
                    mLastClickTime=nowTime;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return subclassList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView subclassImage;
        TextView  subclassText;
        public ViewHolder(View view){
            super(view);
            subclassImage=view.findViewById(R.id.subclass_image);
            subclassText=view.findViewById(R.id.subclass_text);
        }
    }

    public interface MyAdapterClick{
        void onItemClick(String brandName,String ImageUrl,String kindKey);
    }

    public void setMyAdapterClick(MyAdapterClick myAdapterClick){
        this.myAdapterClick=myAdapterClick;
    }
}
