package com.example.smartchart.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartchart.Adapter.ShedulerAdapter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.R;
import com.example.smartchart.SheduleMessageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SheduleMessageFragments extends Fragment implements View.OnClickListener {
    Context mcontext;
    RecyclerView recyclerView;


    List<Shedulermessagedata> mychatlist;
    private static final String TAG = "SheduleMessageFragments";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton floatingActionButton;
    ShedulerAdapter sheduleMessageAdapter;

    public SheduleMessageFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SheduleMessageFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static SheduleMessageFragments newInstance(String param1, String param2) {
        SheduleMessageFragments fragment = new SheduleMessageFragments();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shedule_message_fragments, container, false);

        recyclerView = view.findViewById(R.id.shedule_recycler_view);

        mcontext = container.getContext();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mcontext, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        DatabaseHandler databaseHandler = new DatabaseHandler(mcontext);
        mychatlist = new ArrayList<>();

        mychatlist = databaseHandler.getSheduleList();
        Log.d(TAG, "shedulelis  size   " + mychatlist.size() );
        floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(this);


        sheduleMessageAdapter = new ShedulerAdapter(mychatlist,mcontext);
        recyclerView.setAdapter(sheduleMessageAdapter);

        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SheduleMessageActivity.class);
        startActivity(intent);

    }
}
