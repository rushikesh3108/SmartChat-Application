package com.example.smartchart.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
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
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ShedulerAdapter extends RecyclerView.Adapter<ShedulerAdapter.ViewHolder> {

    Shedulermessagedata shedulermessagedata;

    Calendar calendar;
    List<Shedulermessagedata> shedulermessagedataList;
    Context mcontext;
    Users users;

    private static final String TAG = "ShedulerAdapter";

    public ShedulerAdapter( List<Shedulermessagedata> shedulermessageList, Context context) {
       shedulermessagedataList = shedulermessageList;
        Log.d(TAG, "ShedulerAdapter: "+shedulermessagedataList.size());
       mcontext = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        View view= LayoutInflater.from(mcontext).inflate(R.layout.shedulecardview,parent,false);

        Log.d(TAG, "onCreateViewHolder: ");
        return new  ShedulerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        shedulermessagedata=shedulermessagedataList.get(position);

        String name = shedulermessagedata.getUsers().getFirstname().toString().trim().substring(0,1).toUpperCase() +shedulermessagedata.getUsers().getFirstname().toString().trim().substring(1).toLowerCase();


        Log.d(TAG, "onBindViewHolder:  shedlist name "+name);


        String surname = shedulermessagedata.getUsers().getLastname().toString().trim().substring(0,1).toUpperCase() + shedulermessagedata.getUsers().getLastname().toString().trim().substring(1).toLowerCase();

        String fullname= name+" "+surname;




        String body= shedulermessagedata.getBody().toString();



        String var= convertDate(String.valueOf(shedulermessagedata.getTime()),"dd/MM/yyyy hh:mm aa");



        String s =fullname.substring(0,1);



        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRound(s, color);
        holder.imageView.setImageDrawable(drawable);


        holder.name.setText(fullname);
        holder.body.setText(body);
        holder.timestamp.setText(var);



    }

   /* private String getdate(long time, String s) {



        SimpleDateFormat formatter = new SimpleDateFormat(s);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }
*/


    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();

    }






    @Override
    public int getItemCount() {

        return shedulermessagedataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView body,name,timestamp;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            body=itemView.findViewById(R.id.body_shedule);
            name=itemView.findViewById(R.id.name_shedule);
            timestamp=itemView.findViewById(R.id.timestamp_shedule);
            imageView=itemView.findViewById(R.id.shedulepageimage);

        }
    }
}
