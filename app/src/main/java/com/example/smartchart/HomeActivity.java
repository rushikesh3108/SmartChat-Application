package com.example.smartchart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.smartchart.Adapter.MyPagerAadpter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.Chat;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.smartchart.AppConstant.LOGGED_IN_USER_ID;

public class HomeActivity extends AppCompatActivity {


    List<Chat> nchat;

    Toolbar toolbar;
    MaterialSearchView searchView;

    TabLayout tabLayout;
    ViewPager viewPager;
    MyPagerAadpter myPagerAadpter;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        toolbar = findViewById(R.id.hometoolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.search_view);


        List<String> tablist = new ArrayList<>();

        tablist.add("chats");
        tablist.add("contacts");
        tablist.add("sheduller");






        myPagerAadpter = new MyPagerAadpter(getSupportFragmentManager(), tablist);
        viewPager.setAdapter(myPagerAadpter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d(TAG, "onPageScrolled: ");
            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG, "onPageSelected: " + i);


                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d(TAG, "onQueryTextChange: " + newText);
                databaseHandler.search(newText);
                databaseHandler.Contactsearch(newText);


                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_action);

        searchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
           /* case R.id.darkmode:
               // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
               // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d(TAG, "onOptionsItemSelected: dark mode ");
               *//* Intent intent1 = new Intent( HomeActivity.this, Darkmode.class);
                startActivity(intent1);*//*

            case R.id.lightmode:
                *//*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent2);
                finish();*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void status(String status){
        SharedPreferences sharedPreferences =this.getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String base64id=sharedPreferences.getString(LOGGED_IN_USER_ID,null);
        if(base64id==null){
            Log.d(TAG, "status: ");
        }else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users").child(base64id.concat("=="));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            myRef.updateChildren(hashMap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
