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
    String PACKAGE_NAME =
            "space.ankan.golocal";
    String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";


    int RC_PHOTO_PICKER = 2;
}
