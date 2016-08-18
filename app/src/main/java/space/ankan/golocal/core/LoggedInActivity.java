package space.ankan.golocal.core;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import space.ankan.golocal.model.users.User;

/**
 * Created by ankan.
 * All the activities that can be accessed only once User is logged in should extend this
 */

public class LoggedInActivity extends BaseActivity {

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getFirebaseHelper().isUserLoggedIn())
            askToSignIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
