package viettel.cyberspace.assitant.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import viettel.cyberspace.assitant.utils.TextUtil;

/**
 * Created by brwsr on 28/05/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.v("trungbd", remoteMessage.getNotification().getBody());
        Log.v("trungbd", remoteMessage.getNotification().getTitle());
        TextUtil.pushNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        getApplicationContext().sendBroadcast(new Intent("NewsFromServer"));
    }
}
