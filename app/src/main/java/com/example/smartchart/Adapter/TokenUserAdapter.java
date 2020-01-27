package com.example.smartchart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;


public class TokenUserAdapter  extends FilteredArrayAdapter<Users> {


    Context mcontext;
    public TokenUserAdapter(Context context, int resource, Users[] objects) {
        super(context, resource, objects);
        mcontext=context;

    }

    public TokenUserAdapter(Context context, int resource, int textViewResourceId, Users[] objects) {
        super(context, resource, textViewResourceId, objects);
        mcontext=context;

    }

    public TokenUserAdapter(Context context, int resource, List<Users> objects) {
        super(context, resource, objects);
        mcontext=context;

    }

    public TokenUserAdapter(Context context, int resource, int textViewResourceId, List<Users> objects) {
        super(context, resource, textViewResourceId, objects);
        mcontext=context;

    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {


            LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = l.inflate(R.layout.suggestiontoken, parent, false);

        }

        Users p = getItem(position);


        String name = p.getFirstname().toString().trim().substring(0,1).toUpperCase() + p.getFirstname().toString().trim().substring(1).toLowerCase();


        String surname = p.getLastname().toString().trim().substring(0,1).toUpperCase() + p.getLastname().toString().trim().substring(1).toLowerCase();


        String fullName = name + " " + surname;




        ((TextView)convertView.findViewById(R.id.suggestion)).setText(fullName);

        return convertView;
    }

    @Override
    protected boolean keepObject(Users obj, String mask) {

        mask= mask.toLowerCase();

        return obj.getFirstname().toLowerCase().startsWith(mask) || obj.getPhonenumber().startsWith(mask);
    }
}
