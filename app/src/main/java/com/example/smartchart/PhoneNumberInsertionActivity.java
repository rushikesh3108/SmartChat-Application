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
   // Spinner spinner;
    @BindView(R.id.next)
    Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_insertion);
        editText = findViewById(R.id.phnumber);
        ButterKnife.bind(this);
       // spinner = findViewById(R.id.countrycode);
      //  spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));


    }

    @OnClick(R.id.next)
    public void nextbutton(View view) {
     //   String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

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
