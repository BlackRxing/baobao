package com.example.baoxiaojianapp.Utils;

import java.lang.reflect.Field;

import androidx.recyclerview.widget.RecyclerView;

public class ViewUtils {
    public static void setMaxFlingVelocity(RecyclerView recyclerView,int velocity){
        try{
            Field field=recyclerView.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recyclerView,velocity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
