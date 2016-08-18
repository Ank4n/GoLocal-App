package space.ankan.golocal.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import space.ankan.golocal.R;
import space.ankan.golocal.helpers.FirebaseHelper;
import space.ankan.golocal.model.users.User;
import space.ankan.golocal.screens.onboarding.SplashActivity;
import space.ankan.golocal.utils.RedirectionUtils;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

/**
 * Created by ankan.
 * Base Activity that all activities in the app should extend from
 */

public class BaseActivity extends AppCompatActivity implements AppConstants {
    private static final String SHARED_PREF_FILE_NAME = "GoLocal";
    private static SharedPreferences sharedPreferences;
    private FirebaseHelper firebaseHelper;
    private Boolean isUserKitchenOwner;

    protected FirebaseHelper getFirebaseHelper() {
        return firebaseHelper;
    }

    protected boolean isUserKitchenOwner() {
        if (isUserKitchenOwner == null)
            isUserKitchenOwner = getSharedPref().getInt(AppConstants.USER_TYPE, AppConstants.USER_TYPE_CUSTOMER) == AppConstants.USER_TYPE_KITCHEN_OWNER;
        return isUserKitchenOwner;
    }

    protected SharedPreferences getSharedPref() {
        if (sharedPreferences == null)
            sharedPreferences = getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseHelper = new FirebaseHelper();
    }

    protected void askToSignIn() {
        log("asking to sign in");
        if (!isNetworkAvailable()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_internet_title)
                    .setMessage(R.string.no_internet_message)
                    .setCancelable(false)
                    .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            askToSignIn();
                        }
                    })
                    .setNegativeButton("TURN ON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RedirectionUtils.openNetworkSettings(BaseActivity.this);
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();


            return;
        }
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setLogo(R.mipmap.ic_launcher)
                        .setProviders(
                                AuthUI.EMAIL_PROVIDER,
                                AuthUI.GOOGLE_PROVIDER,
                                AuthUI.FACEBOOK_PROVIDER)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        log("received activity result");
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                getFirebaseHelper().getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean isKitchenOwner = dataSnapshot.getValue(User.class).kitchen != null;
                            RedirectionUtils.redirectFromSplash(BaseActivity.this, isKitchenOwner);
                            saveUserType(isKitchenOwner);
                        }
                        //new user
                        else getFirebaseHelper().enrollNewUser();

                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            } else {
                Toast.makeText(this, "Not Signed in", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        Log.i(TAG, "User Signed out");
                        SplashActivity.createIntent(BaseActivity.this);
                        finish();
                    }
                });
    }

    protected FirebaseUser getCurrentUser() {
        return getFirebaseHelper().getFirebaseAuth().getCurrentUser();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void saveUserType(boolean isKitchenOwner) {
        getSharedPref().edit().putInt(USER_TYPE, isKitchenOwner ? USER_TYPE_KITCHEN_OWNER : USER_TYPE_CUSTOMER).commit();

    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // logging for debugs
    public static void log(String log) {
        if (BuildConfig.DEBUG) return;
        Log.wtf(AppConstants.TAG, log);
    }
}