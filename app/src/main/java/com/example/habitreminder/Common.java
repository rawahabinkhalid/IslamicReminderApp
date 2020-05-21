package com.example.habitreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.habitreminder.userhome.UserDashboardActivity;

public class Common {
    public static void displayAlert(final Context context, String habitKey, String habitName, String title, String respMsg) {
//        final NotificationService activity = (NotificationService) context;

//        try {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setCancelable(false);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    Intent splashIntent = new Intent(context, Notification_Alert.class);
//                    splashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(splashIntent);
//                }
//            });
//            builder.setTitle(title);
//            builder.setMessage("Did you perform '" + habitName + "' habit?");
//            builder.create().show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        final FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view_popup = inflater.inflate(R.layout.notification_alert, null);
        alertDialog.setView(view_popup);
        TextView title_tv = (TextView) view_popup.findViewById(R.id.title_tv);
        TextView body_tv = (TextView) view_popup.findViewById(R.id.body_tv);
        title_tv.setText(title);
//        body_tv.setText(respMsg);
        body_tv.setText("Did you perform '" + habitName + "' habit?");
        Button btn_yes = (Button) view_popup.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context, "Yes", Toast.LENGTH_LONG).show();
            }
        });

        Button btn_no = (Button) view_popup.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "No", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();

    }
}
