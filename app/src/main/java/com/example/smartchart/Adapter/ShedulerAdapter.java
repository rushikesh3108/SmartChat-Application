package com.example.smartchart.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;
import com.example.smartchart.UserinformationActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ShedulerAdapter extends RecyclerView.Adapter<ShedulerAdapter.ViewHolder> {

    Shedulermessagedata shedulermessagedata;

    String fullname;
    List<Shedulermessagedata> shedulermessagedataList;
    Context mcontext;
    Dialog mydialog;


    private static final String TAG = "ShedulerAdapter";

    public ShedulerAdapter(List<Shedulermessagedata> shedulermessageList, Context context) {
        this.shedulermessagedataList = shedulermessageList;
        Log.d(TAG, "ShedulerAdapter: " + shedulermessagedataList.size());
        this.mcontext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        View view = LayoutInflater.from(mcontext).inflate(R.layout.shedulecardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mydialog = new Dialog(mcontext);
        mydialog.setContentView(R.layout.shedulerdialog);


        Log.d(TAG, "onCreateViewHolder: ");
        // return new ShedulerAdapter.ViewHolder(view);;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        shedulermessagedata = shedulermessagedataList.get(position);
        String name = shedulermessagedata.getUsers().getFirstname().toString().trim().substring(0, 1).toUpperCase() + shedulermessagedata.getUsers().getFirstname().toString().trim().substring(1).toLowerCase();
        Log.d(TAG, "onBindViewHolder:  shedlist name " + name);
        String surname = shedulermessagedata.getUsers().getLastname().toString().trim().substring(0, 1).toUpperCase() + shedulermessagedata.getUsers().getLastname().toString().trim().substring(1).toLowerCase();

        fullname = name + " " + surname;
        String body = shedulermessagedata.getBody().toString();
        String var = convertDate(String.valueOf(shedulermessagedata.getTime()), "dd/MM/yyyy hh:mm aa");
        String shedulepc = shedulermessagedata.getUsers().getProfileImageURI();
        Log.d(TAG, "onBindViewHolder: shedule pic : " + shedulepc);
        String s = fullname.substring(0, 1);
        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(s, color);
        Drawable d = new BitmapDrawable(drawableToBitmap(drawable));

        Glide.with(mcontext)
                .load(shedulepc)
                .placeholder(d)
                .into(holder.imageView);


        holder.name.setText(fullname);
        holder.body.setText(body);
        holder.timestamp.setText(var);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: shedulemessage " + fullname);
                TextView tv1 = mydialog.findViewById(R.id.dialog_name);
                TextView tv2 = mydialog.findViewById(R.id.message);
                TextView tv3 = mydialog.findViewById(R.id.showdate);
                TextView tv4 = mydialog.findViewById(R.id.timeshow);

                String showeddate = convertDate(String.valueOf(shedulermessagedataList.get(position).getTime()), "dd/MM/yyyy");
                String showedtime = convertDate(String.valueOf(shedulermessagedataList.get(position).getTime()), "hh:mm aa");

                Log.d(TAG, "onClick: date of shedule popup " + showeddate);
                String toname = shedulermessagedataList.get(position).getUsers().getFirstname().toString();

                String tosurname = shedulermessagedataList.get(position).getUsers().getLastname().toString();
                String tofullname = toname + " " + tosurname;
                Log.d(TAG, "onClick: toname " + tofullname);
                String body = shedulermessagedataList.get(position).getBody().toString();
                tv1.setText(tofullname);
                tv2.setText(body);
                tv3.setText(showeddate);
                tv4.setText(showedtime);

                mydialog.show();
                //Toast.makeText(mcontext, "Dialog box opened ", Toast.LENGTH_SHORT).show();
                //Toast.makeText(mcontext, "dialogboxyy " + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();

    }
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
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


    @Override
    public int getItemCount() {

        return shedulermessagedataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        TextView body, name, timestamp;
        CircularImageView imageView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            cardView = itemView.findViewById(R.id.shedulercardview);
            body = itemView.findViewById(R.id.body_shedule);
            name = itemView.findViewById(R.id.name_shedule);
            timestamp = itemView.findViewById(R.id.timestamp_shedule);
            imageView = itemView.findViewById(R.id.shedulepageimage);

        }
    }
}
