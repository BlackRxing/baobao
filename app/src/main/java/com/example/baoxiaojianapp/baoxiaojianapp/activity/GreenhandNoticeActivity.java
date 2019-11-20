package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GeneratedAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.GreenHandAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalPointItem;

import java.util.List;

import static com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication.getContext;

public class GreenhandNoticeActivity extends AppCompatActivity {

    private RelativeLayout backlayout;
    private RecyclerView recyclerView;
    private TextView greenhandText;
    private List<AppraisalPointItem> appraisalPointItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenhand_notice);
        backlayout=findViewById(R.id.back_layout);
        recyclerView=findViewById(R.id.greenhand_recyclerView);
        greenhandText=findViewById(R.id.greenhandtext);
        backlayout=findViewById(R.id.back_layout);
        backlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindData();
    }
    private void bindData(){
        Bundle bundle=getIntent().getExtras();
        greenhandText.setText(bundle.getString("brandName")+" 鉴定");
        appraisalPointItemList=(List<AppraisalPointItem>) bundle.getSerializable("points");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        GreenHandAdapter greenHandAdapter=new GreenHandAdapter(appraisalPointItemList);
        recyclerView.setAdapter(greenHandAdapter);
    }
}
