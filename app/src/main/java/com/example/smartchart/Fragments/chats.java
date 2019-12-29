package com.example.smartchart.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchart.Adapter.ChatAdapter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.Chat;
import com.example.smartchart.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.smartchart.MessageActivity.BROADCAST;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link chats.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link chats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chats extends Fragment {
    private static final String TAG = "chats";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<Chat> mychatlist;
    boolean isExists;
    Context mcontext;
    RecyclerView mrecyclerView;
    int indexToremove;
    ChatAdapter chatAdapter;


    public static final String BROADCAST_SEARCHBAR = "this is  broadcast is for searchBar";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public chats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chats.
     */
    // TODO: Rename and change types and number of parameters
    public static chats newInstance(String param1, String param2) {
        chats fragment = new chats();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        mcontext = container.getContext();


        // broadcast
        IntentFilter intentFilter = new IntentFilter( BROADCAST );
        mcontext.registerReceiver( broadcastReceiver, intentFilter );



        mrecyclerView = view.findViewById(R.id.chat_recycler_view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mcontext,DividerItemDecoration.VERTICAL);
        mrecyclerView.addItemDecoration(decoration);


        DatabaseHandler databaseHandler = new DatabaseHandler(mcontext);

        mychatlist=new ArrayList<>(  );
        mychatlist=databaseHandler.getChatList();

                Log.d( TAG, "list: "+mychatlist);

     Log.d(TAG, "list" + databaseHandler.getChatList());

       //ChatAdapter mchatAdapter = new ChatAdapter(mcontext, databaseHandler.getChatList());
        //Log.d(TAG, " data : "+mchatAdapter);

       //mrecyclerView.setAdapter(mchatAdapter);//
        //Log.d(TAG, "onCreateView: 123456"+mrecyclerView);

       chatAdapter=new ChatAdapter(mcontext,mychatlist);
        mrecyclerView.setAdapter(chatAdapter);



        return view;
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String ConversionID = bundle.getString( "ConversionID" );
            String MessageID = bundle.getString( "messageId" );
            Log.d( TAG, "conversion12: " + ConversionID );
            Log.d( TAG, "message id " + MessageID );
            ChatlistReceiver( ConversionID, MessageID );
        }
    };

    public void ChatlistReceiver(String ConversionID, String MessageID) {
        DatabaseHandler databaseHandler=new DatabaseHandler( mcontext );

        Chat currentChat = databaseHandler.getChatByConversationId(  ConversionID);

        for (Chat chat : mychatlist) {

            if (chat.message.getConversionId().equals( ConversionID )) {
                isExists = true;
                indexToremove = mychatlist.indexOf( chat );
                Log.d( TAG, "Receiver153: " + indexToremove );
                break;
            }

        }

        Log.d( TAG, "Final Indext to update: " + indexToremove );
        chatAdapter.updateChatList( currentChat, indexToremove, isExists );

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mcontext.unregisterReceiver( broadcastReceiver );
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private  BroadcastReceiver broadcastReceiversearch = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Chat> data = ( List<Chat> ) intent.getSerializableExtra( "data" );
            chatAdapter.setCollection( data );
            Log.d( TAG, "onReceive: "+data );

        }
    };

    @Override
    public void onResume() {
         IntentFilter intentFilter=new IntentFilter(BROADCAST_SEARCHBAR);
         mcontext.registerReceiver(broadcastReceiversearch,intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
                mcontext.unregisterReceiver( broadcastReceiversearch);

        super.onPause();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
