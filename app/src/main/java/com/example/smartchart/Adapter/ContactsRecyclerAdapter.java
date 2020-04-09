package com.example.smartchart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.Glide;
import com.example.smartchart.MessageActivity;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;

import java.util.List;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.MyViewHolder> {
    private static final String TAG = "ContactsRecyclerAdapter";

    private List<Users> mUserList;
    private Context mContext;

    Users userContacts;

    public ContactsRecyclerAdapter(Context context, List<Users> userlist) {
        mUserList = userlist;
        Log.d(TAG, "ContactsRecyclerAdapter: ");
        Log.d(TAG, "MyContactAdapter: " + mUserList);
        mContext = context;
        Log.d(TAG, "MyContactAdapter: " + mContext);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(mContext).inflate(R.layout.contactscardview, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        userContacts = mUserList.get(position);

        String name = userContacts.getFirstname().toString().trim().substring(0, 1).toUpperCase() + userContacts.getFirstname().toString().trim().substring(1).toLowerCase();

        Log.d(TAG, "onBindViewHolder: name " + name);

        String surname = userContacts.getLastname().toString().trim().substring(0, 1).toUpperCase() + userContacts.getLastname().toString().trim().substring(1).toLowerCase();

        Log.d(TAG, "onBindViewHolder: surname " + surname);

        String fullName = name + " " + surname;
        Log.d(TAG, "onBindViewHolder: status" + fullName+","+userContacts.getStatus());

        String mobile = userContacts.getPhonenumber().toString();
       String status= userContacts.getStatus().toString();
        //Log.d(TAG, "onBindViewHolder: status :  "+userContacts.getStatus());

        String userId = userContacts.getId().toString();
        Log.d(TAG, "onBindViewHolder: " + userId);
String imageurl = userContacts.getProfileImageURI();
        Log.d(TAG, "onBindViewHolder: image url  : "+imageurl);
        String s = fullName.substring(0, 1);

        Log.d(TAG, "onBindViewHolder: s = " + s);

        holder.txtUserName.setText(fullName);
        holder.txtUserContact.setText(mobile);

           /* ColorGenerator generator = ColorGenerator.DEFAULT;
            int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(s, color);

            holder.image.setImageDrawable(drawable);
*/
            ColorGenerator generator = ColorGenerator.DEFAULT;

            int color = generator.getRandomColor();
            String si =fullName.substring(0,1);
            TextDrawable drawable = TextDrawable.builder().buildRound(si, color);




        Drawable d = new BitmapDrawable(drawableToBitmap(drawable));

        Glide.with(mContext)
                .load(userContacts.getProfileImageURI())
                .placeholder(d)
                .into(holder.image);

        //String umob = userContacts.getUserMobileno().toString();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ReciverUserId " + userContacts.getId());
                Log.d(TAG, "onClick: ReciverUserId " + userId);

                Log.d(TAG, "onClick: number " + userContacts.getPhonenumber());
                Log.d(TAG, "onClick: number123 " + mobile+","+status);
               // Log.d(TAG, "onClick:  status " +userContacts.getStatus());

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("ReciverUserID", userId);
                intent.putExtra("number", mobile);
                intent.putExtra("status",status);
                intent.putExtra("name", fullName);
                mContext.startActivity(intent);

            }
        });

    }

    //converter is required for circleimageview does not support the textdrawable to drawable
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void setContactList(List<Users> contactList) {
        if (mUserList != null)
            mUserList.clear();

        mUserList = contactList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setCollection(List<Users> data) {
        mUserList = data;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName;
        TextView txtUserContact;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.d(TAG, "MyViewHolder: ");
            txtUserName = itemView.findViewById(R.id.username);
            txtUserContact = itemView.findViewById(R.id.contact_number);
            image = itemView.findViewById(R.id.user_dp);


        }
    }
}
