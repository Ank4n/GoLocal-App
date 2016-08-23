package space.ankan.golocal.core;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public interface AppConstants {


    String TAG = "GoLocal";
    // user-types
    String KITCHEN_ID = "user-type";

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


    //Firebase Storage Constants
    String FIREBASE_STORAGE_KITCHEN_ROOT = "kitchen";
    String FIREBASE_STORAGE_CHAT_IMAGES = "chatPhotos";

    String FOLDER_SEPARATOR = "/";
}
