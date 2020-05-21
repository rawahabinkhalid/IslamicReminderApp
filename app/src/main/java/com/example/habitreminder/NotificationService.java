package com.example.habitreminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.habitreminder.login.LoginActivity;
import com.example.habitreminder.userhome.UserDashboardActivity;
//import com.facebook.common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private String habitKey = "", UserID = "", Type = "";
    private Context context;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = this;
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message_data_payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification BodyMa: " + remoteMessage.getNotification());
            habitKey = remoteMessage.getData().get("HabitKey");
            UserID = remoteMessage.getData().get("UserID");
            Type = remoteMessage.getData().get("Type");
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]
//    private void showNotificationStatusDialog() {
//        FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
//        dbMain.collection("habits").document(habitKey)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.getResult() != null)
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot d = task.getResult();
//                                String habit = d.getString("Name");
//
//
////                                showAlertDialog(habit);
//
//                            } else {
//                                Log.d("TagJournal", "Error getting documents: ", task.getException());
//                            }
//                    }
//                });
//
//    }

//    private void showAlertDialog(String habit) {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        //Yes button clicked
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        //No button clicked
//                        break;
//                }
//            }
//        };
//
////        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////        builder.setMessage("Did you perform '" + habit + "' habit?").setPositiveButton("Yes", dialogClickListener)
////                .setNegativeButton("No", dialogClickListener).show();
////        AlertDialog alertDialog = new AlertDialog.Builder(context)
////                .setTitle("Title")
////                .setMessage("Are you sure?")
////                .setPositiveButton("Yes", dialogClickListener)
////                .setNegativeButton("No", dialogClickListener)
////                .create();
////
////        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY);
////        alertDialog.show();
//
////        AlertDialog.Builder builder = new AlertDialog.Builder(context);
////        builder.setMessage("Did the dialog display?")
////                .setPositiveButton("Yes", dialogClickListener)
////                .setNegativeButton("No", dialogClickListener);
////        AlertDialog alertDialog = builder.create();
////        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
////        alertDialog.show();
//    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        context = this;
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(final String title, final String messageBody) {
        if (!habitKey.equals("")) {
            FirebaseFirestore dbMain = FirebaseFirestore.getInstance();
            dbMain.collection("habits").document(habitKey)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult() != null)
                                if (task.isSuccessful()) {
                                    DocumentSnapshot d = task.getResult();
                                    String habit = d.getString("Name");

                                    SharedPreferences companyId = context.getSharedPreferences("NotificationData",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = companyId.edit();
                                    editor.putString("habitKey", habitKey);
                                    editor.putString("UserID", UserID);
                                    editor.putString("Type", Type);
                                    editor.putString("habitName", habit);
                                    editor.putString("title", title);
                                    editor.putString("body", messageBody);
                                    editor.commit();

//                                showAlertDialog(habit);

                                } else {
                                    Log.d("TagJournal", "Error getting documents: ", task.getException());
                                }
                        }
                    });


        } else {

            SharedPreferences companyId = context.getSharedPreferences("NotificationData",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = companyId.edit();
            editor.putString("habitKey", "");
            editor.putString("UserID", UserID);
            editor.putString("Type", Type);
            editor.putString("habitName", "");
            editor.putString("title", title);
            editor.putString("body", messageBody);
            editor.commit();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

//        Handler h = new Handler(Looper.getMainLooper());
//        h.post(new Runnable() {
//            public void run() {
//                displayAlert(messageBody);
//            }
//        });
    }

//    private void displayAlert(String messageBody) {
//        Common.displayAlert(context, messageBody);
//    }
}