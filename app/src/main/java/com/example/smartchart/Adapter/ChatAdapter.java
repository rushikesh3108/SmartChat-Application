package com.example.smartchart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.smartchart.MessageActivity;
import com.example.smartchart.ModelClass.Chat;
import com.example.smartchart.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final String TAG = "ChatAdapter";

    String s;
    Chat chat;


    Context mcontext;
   List<Chat> mchats;

    public ChatAdapter(Context mcontext, List<Chat> mchats) {
        this.mcontext = mcontext;
        this.mchats = mchats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: 1");
        View view= LayoutInflater.from( mcontext ).inflate( R.layout.chatcardview,parent,false);
        Log.d(TAG, "onCreateViewHolder: 2");
        return new ViewHolder(view) ;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       chat=mchats.get( position );

        String name = chat.user.getFirstname().toString().trim().substring(0,1).toUpperCase() + chat.user.getFirstname().toString().trim().substring(1).toLowerCase();

        Log.d(TAG, "onBindViewHolder: name "+name);

        String surname = chat.user.getLastname().toString().trim().substring(0,1).toUpperCase() + chat.user.getLastname().toString().trim().substring(1).toLowerCase();

        String fullName = name + " " + surname;
        Log.d(TAG, "onBindViewHolder: fullname "+fullName);

        String userId = chat.user.getId().toString();
        Log.d(TAG, "onBindViewHolder:user id  : "+userId);

        Log.d(TAG, "onBindViewHolder: "+position);
        holder.textViewname.setText(fullName);

        Log.d(TAG, "onBindViewHolder: name 1");
        holder.textViewbody.setText( chat.message.getBody().replace("_"," ") );


//getting time stamp in number so using this method

        Long time = Long.parseLong(chat.message.getTimeStamp());
        Log.d(TAG, "onBindViewHolder: time"+time);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date result = new Date(time);
        holder.textViewTimestamp.setText(dateFormat.format(result));


        // holder.textViewTimestamp.setText( chat.message.getTimeStamp() );
        Log.d(TAG, "onBindViewHolder: time  "+chat.message.getTimeStamp());
        Log.d(TAG, "onBindViewHolder:  time "+dateFormat.format(result));

        ColorGenerator generator = ColorGenerator.DEFAULT;

        int color = generator.getRandomColor();
        String s =fullName.substring(0,1);
        TextDrawable drawable = TextDrawable.builder().buildRound(s, color);


        holder.imageView.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( mcontext, MessageActivity.class );

               /* Log.d(TAG, "onClick: ReciverUserId "+userContacts.getId());
                //Log.d(TAG, "onClick: ReciverUserId "+userId);

                Log.d(TAG, "onClick: number "+userContacts.getPhonenumber());
                Log.d(TAG, "onClick: number "+mobile);
*/


               intent.putExtra( "ReciverUserID",userId);
                Log.d(TAG, "onClick user id 123 : "+userId);
                //intent.putExtra( "number",mobile);
                intent.putExtra( "name", fullName);
                mcontext.startActivity( intent );

            }
        });



    }
    public void setCollection(List<Chat> chatCollection) {
        mchats = chatCollection;
        notifyDataSetChanged();
    }

    public void updateChatList(Chat chat, int indexToRemove, boolean isAlreaduExits) {
        Log.d( TAG, "isAlreaduExits: "+isAlreaduExits );
        if(isAlreaduExits){
            mchats.remove( indexToRemove);
        }
        mchats.add( 0, chat );
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, ": "+mchats.size());

        return mchats.size() ;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewname;
        TextView textViewbody;
        TextView textViewTimestamp;
        ImageView imageView;




        public ViewHolder(@NonNull View itemView) {


            super(itemView);

            textViewname=itemView.findViewById(R.id.name_textview);
            textViewbody=itemView.findViewById(R.id.body_textview);
            textViewTimestamp=itemView.findViewById(R.id.timestamp_textview);
            imageView=itemView.findViewById(R.id.chatpageimage);





        }
    }
}
