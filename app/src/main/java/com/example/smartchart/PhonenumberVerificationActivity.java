package com.example.smartchart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartchart.ModelClass.Users;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhonenumberVerificationActivity extends AppCompatActivity {

    @BindView(R.id.verify)
    Button verifybutton;

    SharedPreferences sharedPreferences;
    String phonenumber;
    Boolean numberexists;

    DatabaseReference databaseReference;


    FirebaseAuth firebaseAuth;
    Users users;

    private String verification_id;

    /*
        EditText editText;
    */
    Pinview pinview;

    private static final String TAG = "PhonenumberVerification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber_verification);
        phonenumber = getIntent().getStringExtra("phonenumber");
        firebaseAuth = FirebaseAuth.getInstance();
        PhonenumberVerification(phonenumber);
        pinview = findViewById(R.id.pinview);

        sharedPreferences = getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, MODE_PRIVATE);

        ButterKnife.bind(this);

    }

    private void verify_code(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, code);
        SignIn(credential);
        Log.d(TAG, "verify_code: ");

    }

    private void SignIn(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.

                            Log.d(TAG, "onDataChange: 1");
                            numberexists = false;

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(AppConstant.Isloggedin, true);
                            editor.putString(AppConstant.LOGGED_IN_USER_CONTACT_NUMBER, phonenumber);
                            editor.apply();

                            for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                                users = usersnapshot.getValue(Users.class);
                                if (users.phonenumber.equals(phonenumber)) {
                                    numberexists = true;
                                }

                            }
                            if (numberexists.equals(true)) {

                                Intent intent = new Intent(PhonenumberVerificationActivity.this, HomeActivity.class);
                                intent.putExtra("phonenumber", phonenumber);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d(TAG, "onDataChange: ");
                                Intent intent = new Intent(PhonenumberVerificationActivity.this, UserinformationActivity.class);
                                intent.putExtra("phonenumber", phonenumber);
                                startActivity(intent);
                                finish();
                                Log.d(TAG, "onComplete: 1");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                   /* Intent intent = new Intent(PhonenumberVerificationActivity.this,UserinformationActivity.class);
                    intent.putExtra("phonenumber", phonenumber);
                    startActivity(intent);
                    Log.d(TAG, "onComplete: 1");*/
                } else {
                    Toast.makeText(PhonenumberVerificationActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void PhonenumberVerification(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        Log.d(TAG, "PhonenumberVerification: ");
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    // Toast.makeText(. this, "Verification Complete", Toast.LENGTH_SHORT).show();
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinview.setValue(code);
                        verify_code(code);
                        Log.d(TAG, "onVerificationCompleted: ");
                        Toast.makeText(PhonenumberVerificationActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Log.d(TAG, "onCodeSent: 0" + s);


                    verification_id = s;
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    Log.d(TAG, "onVerificationFailed: " + e);
                    //Toast.makeText(Sms.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    Toast.makeText(PhonenumberVerificationActivity.this, "verifivation failed", Toast.LENGTH_SHORT).show();
                }
            };

    @OnClick(R.id.verify)
    public void verify_button() {
        String code = pinview.getValue().toString();
        verify_code(code);


        Log.d(TAG, "onClick: " + code);
    }
}