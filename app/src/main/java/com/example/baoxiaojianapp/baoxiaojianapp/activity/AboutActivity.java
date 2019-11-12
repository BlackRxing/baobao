package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.PersonFragment;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rateLayout;
    private RelativeLayout corporateInfoLayout;
    private Button shareButton;
    private RelativeLayout backLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
    }
    private void bindView(){
        rateLayout=findViewById(R.id.RateLayout);
        shareButton=findViewById(R.id.share_button);
        backLayout=findViewById(R.id.back_layout);
        corporateInfoLayout=findViewById(R.id.corporateInfoLayout);
        rateLayout.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        corporateInfoLayout.setOnClickListener(this);
        corporateInfoLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.share_button:
                break;
            case R.id.RateLayout:
                break;
            case R.id.corporateInfoLayout:
                break;
        }
    }
}
