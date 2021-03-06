package utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.aptitude.quiz.craftystudio.aptitudequiz.MainActivity;
import app.aptitude.quiz.craftystudio.aptitudequiz.R;
import app.aptitude.quiz.craftystudio.aptitudequiz.TopicActivity;


/**
 * Created by bunny on 07/07/17.
 */

public class FireBasePushNotificationService extends FirebaseMessagingService {
    String editorialID;

    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        /*if (!PushNotificationManager.getPushNotification(this)) {
            return;
        }*/

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {






             intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("check", 0);
            bundle.putString("Topic", remoteMessage.getData().get("topic"));
            bundle.putString("questionUID", remoteMessage.getData().get("questionId"));
            intent.putExtra("pushNotification",true);

            intent.putExtras(bundle);

            showNotification(remoteMessage.getData().get("notificationT"), remoteMessage.getData().get("notificationB"));

        }


    }


    private void showNotification(String title, String body) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int id = (int) System.currentTimeMillis();

        notificationManager.notify(id, notificationBuilder.build());
    }
}
