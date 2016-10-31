package space.ankan.golocal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

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

    public static void cacheLocationAddress(SharedPreferences.Editor edit, String address) {
        if (TextUtils.isEmpty(address)) return;
        edit.putString(CACHED_LAST_ADDRESS, address).commit();
    }

    public static void removeViews(View... views) {
        if (views == null || views.length == 0) return;
        for (View v : views)
            if (v != null)
                v.setVisibility(View.GONE);
    }

    public static void showViews(View... views) {
        if (views == null || views.length == 0) return;
        for (View v : views) {
            if (v != null)
                v.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(View... views) {
        if (views == null || views.length == 0) return;
        for (View v : views) {
            if (v != null)
                v.setVisibility(View.INVISIBLE);
        }
    }

    public static void setupTextRemoveIfEmpty(TextView view, String text, View optionalHideView) {
        if (view == null) return;
        if (TextUtils.isEmpty(text)) {
            removeViews(view, optionalHideView);
        } else {
            view.setText(text);
            showViews(view, optionalHideView);
        }

    }

    public static void setupTextRemoveIfEmpty(TextView view, String text) {
        setupTextRemoveIfEmpty(view, text, null);

    }

    public static void setupTextRemoveIfEmpty(TextView view, @StringRes int text) {
        setupTextRemoveIfEmpty(view, text, null);
    }

    public static void setupTextRemoveIfEmpty(TextView view, @StringRes int text, View optionalHideView) {
        if (view == null) return;
        view.setText(text);
        showViews(view, optionalHideView);
    }

    public static void updateWidgets(Context context) {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}
