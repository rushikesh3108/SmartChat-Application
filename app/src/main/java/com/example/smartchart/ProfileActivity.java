package com.example.smartchart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.smartchart.AppConstant.LOGGED_IN_USER_ID;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    TextView Uname, Unumber;
    private CircleImageView Profileimage;

    private static final int PICK_IMAGE = 1;
    private String userID;
    private StorageReference filepath;
    private Uri imageUri;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Uname = findViewById(R.id.Name);
        Unumber = findViewById(R.id.Number);
        Profileimage = findViewById(R.id.circularimage);
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: profile");
        Log.d(TAG, "onCreate:  profile  "+mAuth.getCurrentUser().getUid());
        userID = mAuth.getCurrentUser().getUid();

        //getUserInfo();
       // mcontext = getActivity();
        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String Name = sharedPreferences.getString(AppConstant.LOGGED_IN_USER_NAME, null);
        String number = sharedPreferences.getString(AppConstant.LOGGED_IN_USER_CONTACT_NUMBER, null);
        Uname.setText(Name);
        Unumber.setText(String.valueOf(number));
        Profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*"); // we set type of images
                startActivityForResult(gallery, PICK_IMAGE);

            }
        });
        String url = sharedPreferences.getString(AppConstant.ImageURI.ProfileImageUri, null);
        if (url == null) {
            Profileimage.setImageResource(R.drawable.account);
        } else {

            Glide.with(this.getApplicationContext()).load(url).into(Profileimage);
        }

       // button.setOnClickListener(this::onClick);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null)   {
            imageUri = data.getData();
            Profileimage.setImageURI(imageUri);

            uploadFile();
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    Log.d(TAG, "onSuccessuri: " + uri);
                    SharedPreferences sharedPreferences = getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppConstant.ImageURI.ProfileImageUri, url);
                    editor.apply();


                    String base64id=sharedPreferences.getString(LOGGED_IN_USER_ID,null);
                    FirebaseDatabase database= FirebaseDatabase.getInstance();
                    DatabaseReference myRef =database.getReference("Users").child(base64id.concat("=="));

                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("profileImageURI",url);
                    myRef.updateChildren(hashMap);
                }
            });

        }

    }

    private void uploadFile() {


        if (imageUri != null)  {

            filepath = FirebaseStorage.getInstance().getReference().child("Profile_Imamge").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            Toast.makeText(this, "Image Sucessfull Uploaded", Toast.LENGTH_LONG).show();
            byte[] data1 = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data1);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                }
            });
        }

    }


}
