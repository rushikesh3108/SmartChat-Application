package com.example.smartchart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneNumberInsertionActivity extends AppCompatActivity {
    EditText editText;

    @BindView(R.id.next)
    Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_insertion);
        editText = findViewById(R.id.phnumber);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.next)
    public void nextbutton(View view) {

        String number = editText.getText().toString();
        if (number.isEmpty() || number.length() < 10) {
            editText.setError(" Valid number is required");
            editText.requestFocus();
            return;

        }
        String phonenumber = "+91"+ number;
        Intent intent = new Intent(this, PhonenumberVerificationActivity.class);
        intent.putExtra("phonenumber", phonenumber);
        startActivity(intent);

    }
}
