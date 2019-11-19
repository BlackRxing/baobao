package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baoxiaojianapp.R;

public class AppraisalNoticeActivity extends AppCompatActivity {

    private RelativeLayout backLayout;
    private ImageView brandImage;
    private TextView brandText;
    private TextView classText;
    private RecyclerView recyclerView;
    private Button appraisalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal_notice);
        bindView();
    }

    private void bindView() {
        backLayout = findViewById(R.id.back_layout);
        brandImage = findViewById(R.id.brandImage);
        brandText = findViewById(R.id.brandName);
        classText = findViewById(R.id.className);
        recyclerView = findViewById(R.id.recycler_view);
        appraisalButton = findViewById(R.id.appraisal_button);
    }
}
