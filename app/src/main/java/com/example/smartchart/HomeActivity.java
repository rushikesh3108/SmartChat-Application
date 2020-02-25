package com.example.smartchart;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
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

                Log.d(TAG, "onQueryTextChange: "+newText);
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
}
