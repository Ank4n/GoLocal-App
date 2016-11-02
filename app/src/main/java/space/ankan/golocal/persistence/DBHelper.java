package space.ankan.golocal.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import space.ankan.golocal.persistence.DBContract.*;

/**
 * Created by ankan.
 * SQLite Open Helper subclass to create and update db at version changes
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "golocal.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_KITCHEN_TABLE = "CREATE TABLE " + KitchenEntry.TABLE_NAME + " (" +
                KitchenEntry._ID + " INTEGER PRIMARY KEY," +
                KitchenEntry.COLUMN_KITCHEN_ID + " TEXT UNIQUE NOT NULL," +
                KitchenEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                KitchenEntry.COLUMN_USER_ID + " TEXT NOT NULL," +
                KitchenEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                KitchenEntry.COLUMN_LATITUDE + " REAL NOT NULL," +
                KitchenEntry.COLUMN_LONGITUDE + " REAL NOT NULL," +
                KitchenEntry.COLUMN_IMAGE_URL + " TEXT," +
                KitchenEntry.COLUMN_RATED_USER_COUNT + " INTEGER NOT NULL," +
                KitchenEntry.COLUMN_OVERALL_RATING + " REAL NOT NULL," +
                KitchenEntry.COLUMN_IS_FAVOURITE + " INTEGER NOT NULL," +
                KitchenEntry.COLUMN_USER_RATING + " INTEGER," +
                KitchenEntry.COLUMN_ADDRESS + " TEXT NOT NULL);";

        final String SQL_CREATE_DISH_TABLE = "CREATE TABLE " + DishEntry.TABLE_NAME + " (" +
                DishEntry._ID + " INTEGER PRIMARY KEY," +
                DishEntry.COLUMN_KITCHEN_ID + " TEXT NOT NULL," +
                DishEntry.COLUMN_DESCRIPTION + " TEXT," +
                DishEntry.COLUMN_IMAGE_URL + " TEXT," +
                DishEntry.COLUMN_NAME + " TEXT NOT NULL," +
                DishEntry.COLUMN_PRICE + " INTEGER NOT NULL," +
                DishEntry.COLUMN_IS_NONVEG + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_KITCHEN_TABLE);
        db.execSQL(SQL_CREATE_DISH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KitchenEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DishEntry.TABLE_NAME);
        onCreate(db);
    }
}
