package com.example.smartchart.Adapter;

import android.app.Dialog;
import android.content.Context;
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
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;
import com.example.smartchart.UserinformationActivity;

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
        shedulermessagedataList = shedulermessageList;
        Log.d(TAG, "ShedulerAdapter: " + shedulermessagedataList.size());
        mcontext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        View view = LayoutInflater.from(mcontext).inflate(R.layout.shedulecardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mydialog = new Dialog(mcontext);
        mydialog.setContentView(R.layout.shedulerdialog);
      //  String name = shedulermessagedata.getUsers().getFirstname().toString().trim().substring(0, 1).toUpperCase() + shedulermessagedata.getUsers().getFirstname().toString().trim().substring(1).toLowerCase();

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mcontext, "Dialog box oped ", Toast.LENGTH_SHORT).show();
                TextView tv1 = mydialog.findViewById(R.id.dialog_name);
                TextView tv2 = mydialog.findViewById(R.id.dialog_number);
                tv1.setText("name");
                mydialog.show();

            }
        });


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


        String s = fullname.substring(0, 1);


        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRound(s, color);
        holder.imageView.setImageDrawable(drawable);


        holder.name.setText(fullname);
        holder.body.setText(body);
        holder.timestamp.setText(var);


/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tv1 = mydialog.findViewById(R.id.dialog_name);
                TextView tv2 = mydialog.findViewById(R.id.dialog_number);
                tv1.setText("hello how are you");
                Toast.makeText(mcontext, "dialogboxyy " + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
*/

    }


    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();

    }


    @Override
    public int getItemCount() {

        return shedulermessagedataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        TextView body, name, timestamp;
        ImageView imageView;


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
