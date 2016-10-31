package space.ankan.golocal.core;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public interface AppConstants {


    String TAG = "GoLocal";

    //Shared Pref
    String KITCHEN_ID = "kitchen_id";
    String CACHED_LAST_LOCATION_LAT = "last_location_lat";
    String CACHED_LAST_LOCATION_LON = "last_location_lon";
    String CACHED_LAST_ADDRESS = "last_address";
    String USER_ID = "user_id";

    //for location fetching
    int SUCCESS_RESULT = 0;
    int FAILURE_RESULT = 1;
    String PACKAGE_NAME = "space.ankan.golocal";
    String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    // pick picture from gallery requestCode
    int RC_PHOTO_PICKER = 2;
    String SHARED_PREF_FILE_NAME = "GoLocal";

    //Firebase DB Constants
    String FIREBASE_DB_CHANNELS = "channels";
    String FIREBASE_DB_USERS = "users";
    String FIREBASE_DB_KITCHENS = "kitchens";
    String FIREBASE_DB_GEOFIRE = "geoFire";

    String FIREBASE_DB_KITCHENS_DISHES = "dishes"; // dishes list inside Kitchens
    String FIREBASE_DB_USERS_CHANNELS = "channels"; // channels list inside Users
    String FIREBASE_DB_USER_REVIEWS = "reviews"; // reviews list inside users node


    //Firebase Storage Constants
    String FIREBASE_STORAGE_KITCHEN_ROOT = "kitchen";
    String FIREBASE_STORAGE_CHAT_IMAGES = "chatPhotos";
    String FIREBASE_STORAGE_BASE_URL = "https://firebasestorage.googleapis.com/";


    String FOLDER_SEPARATOR = "/";

    //KEYS
    String KEY_CHANNEL_ID = "channel_id";
    String KEY_CHANNEL_NAME = "channel_name";
    String KEY_USER_ID = "user_id";

    //Widget update
    String ACTION_DATA_UPDATED =
            "space.ankan.golocal.ACTION_DATA_UPDATED";

}
