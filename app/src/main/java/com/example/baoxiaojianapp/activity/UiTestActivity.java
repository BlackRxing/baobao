package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.view.BxjBottomNavigator;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UiTestActivity extends AppCompatActivity {

    private BxjBottomNavigator bxjBottomNavigator;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_test);
        bxjBottomNavigator=findViewById(R.id.bxjbottomnavigator);
        bxjBottomNavigator.setLeftItemClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("hahahha");
            }
        });
        bxjBottomNavigator.setCenterButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBottomView();
            }
        });
    }

    private void initBottomView(){
        View bottomview=View.inflate(this,R.layout.share_bottomsheet_layout,null);

        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomview);
        ViewGroup viewGroup=(ViewGroup) bottomview.getParent();
        viewGroup.setBackgroundResource(R.color.transparent);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        bottomSheetDialog.show();//test
    }
}
