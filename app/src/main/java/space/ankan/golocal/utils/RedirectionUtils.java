package space.ankan.golocal.utils;

import android.content.Context;
import android.content.Intent;

import space.ankan.golocal.screens.MainActivity;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class RedirectionUtils {

    public static void redirectFromSplash(Context c, boolean kitchenOwner) {

        MainActivity.createIntent(c);


    }

    public static void openNetworkSettings(Context c) {
        c.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    }
}
