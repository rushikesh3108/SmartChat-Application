package com.example.smartchart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.smartchart.Adapter.TokenUserAdapter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;
import com.example.smartchart.customViews.ContactCompletionView;
import com.tokenautocomplete.TokenCompleteTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SheduleMessageActivity extends AppCompatActivity implements TokenCompleteTextView.TokenListener<Users> {

    Button sedate, setime, shedule;

    String body, senderid;


    Toolbar toolbar;
    List<Users> usersList;
    ContactCompletionView contactCompletionView;

    TokenUserAdapter adapter;
    long millis;
    EditText message, time, mdate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String TAG = "Second";

    private List<String> userIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shedule_message);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        usersList = new ArrayList<>();
        usersList = databaseHandler.displayUserContact();
        Log.d(TAG, "contact list in sheduller" + usersList);
        adapter = new TokenUserAdapter(this, R.layout.contact_token_view, usersList);


        contactCompletionView = findViewById(R.id.name);
        contactCompletionView.setTokenListener(this);
        contactCompletionView.setAdapter(adapter);

        setime = findViewById(R.id.timebtn);
        sedate = findViewById(R.id.datbtn);
        shedule = findViewById(R.id.shedule);
        toolbar = findViewById(R.id.hometoolbar);
        setSupportActionBar(toolbar);
        message = findViewById(R.id.shipper_field);
        time = findViewById(R.id.edttime);
        mdate = findViewById(R.id.edtdate);


    }

    public void date(View view) {


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }

    public void time(View v) {


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time.setText(hourOfDay + ":" + minute);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onTokenAdded(Users token) {
        userIds.add(token.getId());

        Log.d(TAG, "onTokenAdded: " + token.getId().toString());
        senderid = token.getId().toString();

    }

    @Override
    public void onTokenRemoved(Users token) {
        userIds.remove(token.getId());
    }

    @Override
    public void onTokenIgnored(Users token) {

    }

    public void Shedule(View view) {

        String eddate = mdate.getText().toString();
        Log.d(TAG, "Shedule: date " + eddate);

        String edtime = time.getText().toString();
        Log.d(TAG, "Shedule: time " + edtime);
        body = message.getText().toString();
        Log.d(TAG, "message body: " + body);


        String toParse = eddate + " " + edtime; // Results in "2-5-2012 20:43"
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        millis = date.getTime();

        Log.d(TAG, "Shedule: milli " + millis);


        Shedulermessagedata shedulermessagedata = new Shedulermessagedata();
        shedulermessagedata.setSenderid(senderid);
        shedulermessagedata.setBody(body);
        shedulermessagedata.setTime(millis);
        Log.d(TAG, "Shedule: messagedata classs" + shedulermessagedata.toString());
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.InsertSheduleMesage(shedulermessagedata);

        time.setText("");
        mdate.setText("");
        contactCompletionView.setText("");
        message.setText("");
    }
}

