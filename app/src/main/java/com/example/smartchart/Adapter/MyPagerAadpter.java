package com.example.smartchart.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.smartchart.Fragments.Contacts;
import com.example.smartchart.Fragments.chats;

import java.util.List;

public class MyPagerAadpter extends FragmentStatePagerAdapter {
    private List<String> mtablist;

    public MyPagerAadpter(FragmentManager fm, List<String> tablist) {
        super(fm);

     mtablist=tablist;


    }


    @Override
    public Fragment getItem(int i) {
      if (i==0){
          return new chats();

      }
      else {
          return new Contacts();

      }
    }
    @Override
    public CharSequence getPageTitle(int i)
    {
        return mtablist.get(i);

    }

    @Override
    public int getCount() {
        return mtablist.size();

    }


}
