package com.example.smartchart.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchart.AppConstant;
import com.example.smartchart.Adapter.ContactsRecyclerAdapter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class
Contacts extends Fragment {

    public static final String THIS_BROADCAST_FOR_CONTACT_SEARCHBAR = "this is for contact searchBar";

    public ContactsRecyclerAdapter adapter;

    public Context context;
    String userID, userName, userSurName, userMobile, image;
    public DatabaseReference databaseUser;

    public FirebaseDatabase firebaseDatabase;

    public View view;

    RecyclerView recyclerView;
    List<Users> userList;

    List<String> userContactList;

    private SharedPreferences mSharedPreferences;

    String mLoggedInUserContactNumber;


    Users user;

    private static final String TAG = "Contacts";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Contacts() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Contacts newInstance(String param1, String param2) {
        Contacts fragment = new Contacts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: of ContactFragment");

        //  Log.d(TAG, "onCreateView: "+user);
        //Log.d(TAG, "onCreateView: "+userList);

        context = container.getContext();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        user = new Users();
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayout);

        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        mSharedPreferences = context.getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        mLoggedInUserContactNumber = mSharedPreferences.getString(AppConstant.LOGGED_IN_USER_CONTACT_NUMBER, null);


        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        //fetch Data From Database
        firebaseDatabase = FirebaseDatabase.getInstance();

        userList = new ArrayList<>();


        userContactList = databaseHandler.displayUserID();
        databaseUser = firebaseDatabase.getReference("Users");
        try {

            Log.d(TAG, "onDataChange: ");
            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChanging " + dataSnapshot);
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        user = userSnapshot.getValue(Users.class);
                        Log.d(TAG, "ondatvdcu   " + user);
                        if (mLoggedInUserContactNumber != null && !mLoggedInUserContactNumber.equalsIgnoreCase(user.getPhonenumber())) {


                            userID = user.getId().toString().trim();
                            userName = user.getFirstname().toString().trim();
                            userSurName = user.getLastname().toString().trim();
                            userMobile = user.getPhonenumber().toString().trim();
                            image = user.getProfileImageURI();


                            Log.d(TAG, "onDataChange: user Data" + userID + "," + userName + "," + userSurName + "," + userMobile);

                            Log.d(TAG, "onDataChange: " + user.toString());

                            //Offline data will save in databas
                            Users userContact = new Users();
                            userContact.setFirstname(userName);
                            userContact.setLastname(userSurName);
                            userContact.setPhonenumber(userMobile);
                            userContact.setId(userID);
                            userContact.setProfileImageURI(image);
                            Log.d(TAG, "onDataChange: ");
                            if (!userContactList.contains(userID)) {
                                Log.d(TAG, "onDataChange: inside If" + userList + "\n" + userID);
                                databaseHandler.insertUser(userContact);
                            } else {
                                Log.d(TAG, "onDataChange: inside else" + userList + "\n" + userID);
                            }
                            Log.d(TAG, "Insert Tag");
                            userList.add(user);
                        } else {
                            checkCurrentUser();

                            //Log.d(TAG, "onDataChange: inside Else");
                        }
                        Log.d(TAG, "onDataChange: UserId  " + user.getId());
                    }
                    Log.d(TAG, " UserContactList " + databaseHandler.displayUserContact());
                    adapter.setContactList(userList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.toString());
        }

        adapter = new ContactsRecyclerAdapter(context, databaseHandler.displayUserContact());
        Log.d(TAG, "after Send Adapter: " + adapter);
        Log.d(TAG, "onCreateView: " + userList);
        recyclerView.setAdapter(adapter);

        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void checkCurrentUser() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AppConstant.LOGGED_IN_USER_NAME, user.getFirstname().trim() + " " + user.getLastname().trim());
        editor.putString(AppConstant.LOGGED_IN_USER_ID, user.getId());
        editor.apply();
        Log.d(TAG, "checkCurrentUser: ");
        FirebaseMessaging.getInstance().subscribeToTopic(user.getId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: 1");
                        String msg = getString(R.string.msg_subscribed);
                        Log.d(TAG, "onComplete: " + msg);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, "onComplete:1 " + msg);
                        Log.d(TAG, msg);
                    }
                });


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Users> data = (List<Users>) intent.getSerializableExtra("contactdata");
            Log.d(TAG, "message_sended: " + data);
            adapter.setCollection(data);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter(THIS_BROADCAST_FOR_CONTACT_SEARCHBAR);
        getActivity().registerReceiver(broadcastReceiver, intentFilter1);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);

    }
}
