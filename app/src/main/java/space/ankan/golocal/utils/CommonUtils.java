package space.ankan.golocal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.firebase.ui.auth.BuildConfig;

import java.text.DecimalFormat;

import space.ankan.golocal.core.AppConstants;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class CommonUtils implements AppConstants {

    public static void imagePickerForResult(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        activity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), AppConstants.RC_PHOTO_PICKER);
    }

    public static void imagePickerForResult(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        fragment.startActivityForResult(Intent.createChooser(intent, "Complete action using"), AppConstants.RC_PHOTO_PICKER);
    }

    public static void closeKeyBoard(Activity activity) {

        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view != null)

        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public static Double getLastLocationLatitude(SharedPreferences sharedPreferences) {
        double lat;
        try {
            lat = Double.parseDouble(sharedPreferences.getString(CACHED_LAST_LOCATION_LAT, ""));
        } catch (NumberFormatException nfe) {
            return null;
        }
        return lat;
    }

    public static Double getLastLocationLongitude(SharedPreferences sharedPreferences) {
        double lon;
        try {
            lon = Double.parseDouble(sharedPreferences.getString(CACHED_LAST_LOCATION_LON, ""));
        } catch (NumberFormatException nfe) {
            return null;
        }
        return lon;
    }


    public static void cacheLocation(SharedPreferences.Editor edit, Location location) {
        edit.putString(CACHED_LAST_LOCATION_LAT, String.valueOf(location.getLatitude()));
        edit.putString(CACHED_LAST_LOCATION_LON, String.valueOf(location.getLongitude()));
        edit.commit();
    }
}
