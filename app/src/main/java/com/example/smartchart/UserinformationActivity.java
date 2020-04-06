package com.example.smartchart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartchart.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserinformationActivity extends AppCompatActivity {


    Users users;

    TextInputEditText txt_firstname, txt_lastname;
    Button btn_save;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    private static final String TAG = "UserinformationActivity";

    String phonenumber;
    String Base64id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);
        txt_firstname = findViewById(R.id.firstname);
        txt_lastname = findViewById(R.id.lastname);
        btn_save = findViewById(R.id.submit);

        phonenumber = getIntent().getStringExtra("phonenumber");

        Log.d(TAG, "onCreate: ");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = txt_firstname.getText().toString();
                if (firstname.isEmpty()) {
                    txt_firstname.setError("required");
                    txt_firstname.requestFocus();
                    return;
                }
                String lastname = txt_lastname.getText().toString();
                /*users.setProfileImageURI("default");
                 */

                if (lastname.isEmpty()) {
                    txt_lastname.setError("required");
                    txt_lastname.requestFocus();
                    return;


                }
                // String id=databaseReference.push().getKey(); get genrate id

                byte[] bytesEncoded = Base64.encode(phonenumber.getBytes(), Base64.DEFAULT);
                String Base64id = new String(bytesEncoded);


                String id = Base64id.replace("==", "").trim(); //base 64 id to send for messeage


                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(UserinformationActivity.this, "please enter firstname", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(UserinformationActivity.this, "please enter lastname", Toast.LENGTH_SHORT).show();
                }
                String profileimageURI = "default";
                String status="default";

                Log.d(TAG, "onComplete after");
                Users information = new Users(firstname, lastname, id, phonenumber, profileimageURI,status);
                Log.d(TAG, "onClick: userinfo " + information);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(new String(bytesEncoded).trim())
                        .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(UserinformationActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //intent of new activity
                        Toast.makeText(UserinformationActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }
}
