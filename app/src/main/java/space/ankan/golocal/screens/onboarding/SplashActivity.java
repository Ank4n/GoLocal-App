package space.ankan.golocal.screens.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseActivity;
import space.ankan.golocal.utils.RedirectionUtils;

public class SplashActivity extends BaseActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        textView = (TextView) findViewById(R.id.message);
        Log.wtf("GoLocal", "Splash Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!getFirebaseHelper().isUserLoggedIn()) {
                    askToSignIn();
                } else {
                    RedirectionUtils.redirectFromSplash(SplashActivity.this);
                    getFirebaseHelper().subscribe();
                    finish();
                }

            }
        }, 1000);


    }

    public static void createIntent(Context context) {
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        textView.setText(R.string.logging_in_text);
        textView.setVisibility(View.VISIBLE);
        super.onActivityResult(requestCode, resultCode, data);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }
}
