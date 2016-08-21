package space.ankan.golocal.core;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public interface AppConstants {


    public static final String TAG = "GoLocal";
    // user-types
    public static final String KITCHEN_ID = "user-type";

    //for location fetching
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "space.ankan.golocal";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";



}
