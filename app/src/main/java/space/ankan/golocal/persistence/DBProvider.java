package space.ankan.golocal.persistence;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import space.ankan.golocal.persistence.DBContract.*;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class DBProvider extends ContentProvider {

    private static final String LOG_CAT = DBProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;
    static final int KITCHENS = 100;
    static final int KITCHEN_BY_ID = 102;
    static final int DISHES = 103;


    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case KITCHENS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.KitchenEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            case KITCHEN_BY_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.KitchenEntry.TABLE_NAME,
                        projection,
                        KitchenEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder

                );
                break;
            case DISHES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DBContract.DishEntry.TABLE_NAME,
                        projection,
                        DBContract.DishEntry.COLUMN_KITCHEN_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder

                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case (KITCHENS):
                return KitchenEntry.CONTENT_TYPE;
            case (KITCHEN_BY_ID):
                return KitchenEntry.CONTENT_ITEM_TYPE;
            case (DISHES):
                return DishEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case KITCHENS: {
                long _id = db.insert(KitchenEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = KitchenEntry.buildKitchenUri(_id);
                    Log.v(LOG_CAT, "Row inserted Successfully with id " + _id);
                } else {
                    Log.v(LOG_CAT, "Failed to insert row into" + uri);
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case KITCHENS:
                rowsDeleted = mOpenHelper.getWritableDatabase().delete(KitchenEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            Log.v(LOG_CAT, rowsDeleted + " rows deleted");
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case KITCHENS:
                rowsUpdated = mOpenHelper.getWritableDatabase().update(KitchenEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DBContract.PATH_KITCHENS, KITCHENS);
        Log.wtf(LOG_CAT, matcher.toString());
        //matcher.addURI(authority, DBContract.PATH_KITCHENS, FAVOURITE_KITCHENS);
        matcher.addURI(authority, DBContract.PATH_KITCHENS + "/*", KITCHEN_BY_ID);
        matcher.addURI(authority, DBContract.PATH_DISHES + "/*", DISHES);
        return matcher;
    }
}
