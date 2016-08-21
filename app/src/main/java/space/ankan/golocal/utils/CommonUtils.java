package space.ankan.golocal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firebase.ui.auth.BuildConfig;

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
}
