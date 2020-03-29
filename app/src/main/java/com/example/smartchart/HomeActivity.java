package com.example.smartchart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.smartchart.Adapter.MyPagerAadpter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.Chat;
import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

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
            case R.id.darkmode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
               // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent1);
                finish();
            case R.id.lightmode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent2);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
