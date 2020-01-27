package com.example.smartchart.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;
import com.tokenautocomplete.TokenCompleteTextView;

public class ContactCompletionView extends TokenCompleteTextView<Users> {

    private Context mContext;
    CardView cardView;
    TextView textView;


    public ContactCompletionView(Context context) {
        super(context);
        mContext = context;
    }

    public ContactCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ContactCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected View getViewForObject(Users user) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_token_view, null, false);


        textView = view.findViewById(R.id.show_contact);

        String name = user.getFirstname().toString().trim().substring(0,1).toUpperCase() + user.getFirstname().toString().trim().substring(1).toLowerCase();
        String surname = user.getLastname().toString().trim().substring(0,1).toUpperCase() + user.getLastname().toString().trim().substring(1).toLowerCase();
        String fullName = name + " " + surname;

        textView.setText(fullName);
        return view;
    }

    @Override
    protected Users defaultObject(String completionText) {
        return null;
    }
}
