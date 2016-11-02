package space.ankan.golocal.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseUser;

import space.ankan.golocal.R;
import space.ankan.golocal.helpers.FirebaseHelper;
import space.ankan.golocal.model.channels.ChatMessage;
import space.ankan.golocal.model.notifications.Notification;
import space.ankan.golocal.screens.chat.ChatActivity;
import space.ankan.golocal.screens.chat.ChatActivityFragment;

/**
 * Created by ankan.
 * Helper class to create notifications and redirect from them
 */

public class NotificationUtils {
    private static final int TYPE_CHAT_ACTIVITY = 1;
    private static FirebaseHelper firebaseHelper = new FirebaseHelper();
    private static NotificationManager mNotificationManager;


    public static void pushNotification(ChatMessage message, String receiverId, String channelName, String channelId) {
        FirebaseUser sender = firebaseHelper.getFirebaseAuth().getCurrentUser();
        String senderId = null, displayName = null;
        if (sender != null) {
            senderId = sender.getUid();
            displayName = sender.getDisplayName();
        }
        Notification notification = new Notification(senderId, displayName, message.message, NotificationUtils.TYPE_CHAT_ACTIVITY, channelName, channelId);
        firebaseHelper.postNotification(notification, receiverId);
    }

    private static NotificationManager getNotificationManager(Context context) {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        return mNotificationManager;
    }


    public static void notifyUser(Context context, Notification notification) {

        if (isChatScreenActive(notification.channelId)) return;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_status)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification))
                        .setContentTitle(notification.title)
                        .setContentText(notification.message)
                        .setDefaults(android.app.Notification.DEFAULT_ALL)
                        .setAutoCancel(true);

        Intent resultIntent = ChatActivity.getCreateIntent(context, notification.channelId, notification.channelName, notification.senderId);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        getNotificationManager(context).notify(notification.senderId.hashCode(), mBuilder.build());
    }

    private static boolean isChatScreenActive(String channel) {
        return ChatActivityFragment.isScreenActive(channel);
    }

}
