package com.example.smartchart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetStartedActivity extends AppCompatActivity {

@BindView(R.id.getstarted)
    Button getstarted_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.getstarted)
    public void getstareted(View view) {
        Intent intent=new Intent(this,PhoneNumberInsertionActivity.class);
        startActivity(intent);
    }



}
