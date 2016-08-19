package space.ankan.golocal.core;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import space.ankan.golocal.model.users.User;
import space.ankan.golocal.utils.RedirectionUtils;

/**
 * Created by ankan.
 * All the activities that can be accessed only once User is logged in should extend this
 */

public abstract class LoggedInActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getFirebaseHelper().isUserLoggedIn())
            askToSignIn();

        getFirebaseHelper().getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    setUser(dataSnapshot.getValue(User.class));
                    saveUserType(getUser().kitchen != null);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
