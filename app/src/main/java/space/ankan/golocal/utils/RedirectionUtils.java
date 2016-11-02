package space.ankan.golocal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import space.ankan.golocal.screens.MainActivity;

/**
 * Created by ankan.
 * Methods to help with navigation in the app..
 */

public class RedirectionUtils {

    public static void redirectFromSplash(Context c) {
        MainActivity.createIntent(c);
    }

    public static void openNetworkSettings(Context c) {
        c.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    }

    public static void onBackPressed(Activity activity) {

        if (activity == null) return;
        if (activity.isTaskRoot())
            redirectFromSplash(activity);
        activity.finish();
    }

}
