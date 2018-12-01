package mobile.ufc.br.novosispu.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.entities.Like;
import mobile.ufc.br.novosispu.entities.User;

import static mobile.ufc.br.novosispu.Constants.CHANNEL_ID;
import static mobile.ufc.br.novosispu.Constants.FIREBASE_CHILD_LIKES;

public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LikeService likeService = new LikeService();
        final UserService userService = new UserService();
        final DemandService demandService = new DemandService();
        DatabaseReference ref = likeService.getLikesRef();

        final String currentUserKey = userService.getCurrentUserKey();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    final Like like = item.getValue(Like.class);

                    demandService.getDemandRef().child(like.getDemandKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Demand demand = dataSnapshot.getValue(Demand.class);

                            if(demand.getUser().getKey().equals(currentUserKey) && !like.isNotified()) {
                                userService.getUsersRef().child(like.getUserKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        sendNotifications(user.getName() + " curtiu sua demanda");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void sendNotifications(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Notify", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Descricao");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("SISPU")
                .setContentText(message)
                .setContentInfo("Info");

        Random r = new Random();
        notificationManager.notify(r.nextInt(), notificationBuilder.build());
    }
}
