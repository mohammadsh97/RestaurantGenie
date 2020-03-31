package com.MohammadSharabati.restaurantgenie.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.MohammadSharabati.restaurantgenie.Common.Common;
import com.MohammadSharabati.restaurantgenie.Model.Request;
import com.MohammadSharabati.restaurantgenie.OrderStatus;
import com.MohammadSharabati.restaurantgenie.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.NotificationCompat;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase database;
    DatabaseReference requests;

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // init firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this); // set up action
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Request request=dataSnapshot.getValue(Request.class);
        showNotificationManager(dataSnapshot.getKey(),request); // to create notification to user
    }
    /*this method that create Notification to user*/
    private void showNotificationManager(String key, Request request) {
        Intent intent=new Intent(getBaseContext(), OrderStatus.class);
        intent.putExtra("phone",request.getPhone());

        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("RestaurantGenie")
                .setContentTitle("Your order Was Update")
                .setContentText("Order # "+key+"was updated to status "+ Common.convertCodeToStatus(request.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
