package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baoxiaojianapp.R;

public class EditNickNameActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView backTextView;
    private TextView saveTextView;
    private TextView nicknameText;
    private EditText nicknameedit;
    private Button   deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick_name);
        bindView();
    }

    private void bindView(){
        backTextView=findViewById(R.id.back_text);
        saveTextView=findViewById(R.id.save_text);
        nicknameedit=findViewById(R.id.nickname_edit);
        nicknameText=findViewById(R.id.nickname_text);
        deleteButton=findViewById(R.id.delete_button);
        nicknameedit.setOnClickListener(this);
        nicknameText.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        nicknameedit.addTextChangedListener(this);
        saveTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_button:
                nicknameedit.setText("");
                break;
            case R.id.save_text:
                saveNickName();
                break;
        }
    }

    public void saveNickName(){

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        nicknameText.setText("昵称"+s.length()+"/20");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
