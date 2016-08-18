package space.ankan.golocal.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import space.ankan.golocal.screens.mykitchen.MyKitchenActivity;
import space.ankan.golocal.screens.MainActivity;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class ActivityUtils {

    public static void startKitchenListActivity(Context context) {
        Log.wtf("GoLocal", "Starting kitchen list activity");
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void startMyKitchenActivity(Context context) {
        context.startActivity(new Intent(context, MyKitchenActivity.class));
    }
}
