package com.example.smartchart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    boolean login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Intent intent=new Intent(this,GetStartedActivity.class);
        startActivity(intent);
        finish();*/
        sharedPreferences=getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME,MODE_PRIVATE);
        login=sharedPreferences.getBoolean(AppConstant.Isloggedin,false);
        if (!login){
            Intent intent=new Intent(this,GetStartedActivity.class);
            startActivity(intent);

        }
        else {
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        finish();

    }
}
