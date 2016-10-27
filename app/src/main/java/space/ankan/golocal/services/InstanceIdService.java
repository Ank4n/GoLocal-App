package space.ankan.golocal.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance(FirebaseApp.getInstance()).getCurrentUser().getUid());
    }
}
