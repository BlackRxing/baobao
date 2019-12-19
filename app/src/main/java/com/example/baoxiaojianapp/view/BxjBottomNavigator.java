package com.example.baoxiaojianapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baoxiaojianapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BxjBottomNavigator extends FrameLayout {
    private Context mcontext;
    private View mView;
    private TextView leftText;
    private ImageView leftImage;
    private TextView rightText;
    private ImageView rightImage;
    private Button centerButton;
    private LinearLayout leftItem;
    private LinearLayout rightItem;


    private void setLeftText(String text){
        leftText.setText(text);
    }

    private void setRightText(String text){
        rightText.setText(text);
    }

    private void setLeftImage(int imageId){
        leftImage.setImageResource(imageId);
    }

    private void setRightImage(int imageId){
        rightImage.setImageResource(imageId);
    }

    private void setCenterButtonBackground(int imageId){
        centerButton.setBackground(getResources().getDrawable(imageId));
    }


    public BxjBottomNavigator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        initView(context,attrs);
    }

//    private void initView(Context context,AttributeSet attributeSet){
//        mcontext=context;
//        LayoutInflater inflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mView=inflater.inflate(R.layout.bxjbottomnavigator_layout,this,true);
//        leftImage=mView.findViewById(R.id.left_image);
//        rightImage=mView.findViewById(R.id.right_image);
//        leftText=mView.findViewById(R.id.left_text);
//        rightText=mView.findViewById(R.id.right_text);
//        centerButton=mView.findViewById(R.id.center_button);
//        leftItem=mView.findViewById(R.id.left_item);
//        rightItem=mView.findViewById(R.id.right_item);
//
//        TypedArray typedArray=mcontext.obtainStyledAttributes(attributeSet,R.styleable.BxjBottomNavigator);
//        setLeftImage(typedArray.getResourceId(R.styleable.BxjBottomNavigator_leftItemResource,-1));
//        setRightImage(typedArray.getResourceId(R.styleable.BxjBottomNavigator_RightItemResource,-1));
//        setLeftText(typedArray.getString(R.styleable.BxjBottomNavigator_leftItemText));
//        setRightText(typedArray.getString(R.styleable.BxjBottomNavigator_RightItemText));
//        setCenterButtonBackground(typedArray.getResourceId(R.styleable.BxjBottomNavigator_centerItemResource,-1));
//    }

    public void setLeftItemClick(View.OnClickListener onClickListener){
        leftItem.setOnClickListener(onClickListener);
    }

    public void setRightItemClick(View.OnClickListener onClickListener){
        rightItem.setOnClickListener(onClickListener);
    }

    public void setCenterButtonClick(View.OnClickListener onClickListener){
        centerButton.setOnClickListener(onClickListener);
    }


    
}
