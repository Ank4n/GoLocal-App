package space.ankan.golocal.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import space.ankan.golocal.core.AppConstants;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.persistence.DBContract;
import space.ankan.golocal.persistence.DBContract.*;
import space.ankan.golocal.persistence.DBProvider;

import static space.ankan.golocal.core.AppConstants.SHARED_PREF_FILE_NAME;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class DBUtils {

    private static final String[] KITCHEN_PROJECTION = new String[]{
            KitchenEntry.COLUMN_KITCHEN_ID,
            KitchenEntry.COLUMN_TITLE,
            KitchenEntry.COLUMN_USER_ID,
            KitchenEntry.COLUMN_DESCRIPTION,
            KitchenEntry.COLUMN_LATITUDE,
            KitchenEntry.COLUMN_LONGITUDE,
            KitchenEntry.COLUMN_IMAGE_URL,
            KitchenEntry.COLUMN_RATED_USER_COUNT,
            KitchenEntry.COLUMN_OVERALL_RATING,
            KitchenEntry.COLUMN_IS_FAVOURITE,
            KitchenEntry.COLUMN_USER_RATING,
            KitchenEntry.COLUMN_ADDRESS};

    public static ContentValues getContentValuesFromKitchen(Kitchen kitchen) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KitchenEntry.COLUMN_KITCHEN_ID, kitchen.key);
        contentValues.put(KitchenEntry.COLUMN_TITLE, kitchen.name);
        contentValues.put(KitchenEntry.COLUMN_USER_ID, kitchen.userId);
        contentValues.put(KitchenEntry.COLUMN_DESCRIPTION, kitchen.description);
        contentValues.put(KitchenEntry.COLUMN_LATITUDE, kitchen.latitude);
        contentValues.put(KitchenEntry.COLUMN_LONGITUDE, kitchen.longitude);
        contentValues.put(KitchenEntry.COLUMN_IMAGE_URL, kitchen.imageUrl);
        contentValues.put(KitchenEntry.COLUMN_RATED_USER_COUNT, kitchen.ratedUserCount);
        contentValues.put(KitchenEntry.COLUMN_OVERALL_RATING, kitchen.overallRating);
        contentValues.put(KitchenEntry.COLUMN_IS_FAVOURITE, 1);
        //FIXME
        contentValues.put(KitchenEntry.COLUMN_USER_RATING, 2);
        contentValues.put(KitchenEntry.COLUMN_ADDRESS, kitchen.address);
        return contentValues;
    }

    public static Kitchen getKitchenFromCursor(Cursor c) {
        if (c.getCount() == 0) return null;
        return new Kitchen(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getDouble(5), c.getDouble(6), c.getString(7), c.getInt(8), c.getDouble(9), c.getInt(10), c.getString(12));

    }

    public static Uri insertKitchen(ContentResolver contentResolver, Kitchen kitchen) {
        Log.v("DBProvider", "inserting kitchen with id " + kitchen.key);
        return contentResolver.insert(DBContract.KitchenEntry.CONTENT_URI, getContentValuesFromKitchen(kitchen));

    }

    public static int deleteKitchen(ContentResolver contentResolver, String id) {
        Log.v("DBProvider", "deleting kitchen with id " + id);
        return contentResolver.delete(DBContract.KitchenEntry.CONTENT_URI, DBContract.KitchenEntry.COLUMN_KITCHEN_ID + " = ?", new String[]{id});

    }

    public static Kitchen queryKitchenById(ContentResolver contentResolver, String id) {
        Cursor c = contentResolver.query(DBContract.KitchenEntry.CONTENT_URI, null, DBContract.KitchenEntry.COLUMN_KITCHEN_ID + " = ?", new String[]{id}, null);
        if (!c.moveToFirst()) return null;
        return getKitchenFromCursor(c);

    }

    public static Cursor queryFavouriteKitchens(ContentResolver contentResolver) {
        Cursor c = contentResolver.query(DBContract.KitchenEntry.CONTENT_URI, null, KitchenEntry.COLUMN_IS_FAVOURITE + " = ?", new String[]{"1"}, null);
        Log.v("DBProvider", c.getCount() + " favorite kitchens found");
        return c;
    }

    public static void persistUserId(Context c, String userId) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AppConstants.USER_ID, userId).commit();
    }

    public static String getUserId(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.USER_ID, null);
    }
}
