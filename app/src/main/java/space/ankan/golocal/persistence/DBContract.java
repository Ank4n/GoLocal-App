package space.ankan.golocal.persistence;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class DBContract {

    public static final String CONTENT_AUTHORITY = "space.ankan.golocal";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_KITCHENS = "kitchen";
    public static final String PATH_DISHES = "dish";
    public static final String PATH_REVIEWS = "review";

    public static final class KitchenEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_KITCHENS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KITCHENS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KITCHENS;

        public static final String TABLE_NAME = "kitchen";

        //columns kitchen
        public static final String COLUMN_KITCHEN_ID = "kitchen_id";
        public static final String COLUMN_TITLE = "kitchen_title";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_RATED_USER_COUNT = "rated_user_count";
        public static final String COLUMN_OVERALL_RATING = "overall_rating";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_USER_RATING = "user_rating";

        public static Uri buildKitchenUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class DishEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_KITCHENS).appendPath(PATH_DISHES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KITCHENS + "/" + PATH_DISHES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KITCHENS + "/" + PATH_DISHES;

        public static final String TABLE_NAME = "dish";

        //columns dish
        public static final String COLUMN_KITCHEN_ID = "kitchen_id";
        public static final String COLUMN_DESCRIPTION = "dish_description";
        public static final String COLUMN_IMAGE_URL = "dish_image_url";
        public static final String COLUMN_NAME = "dish_name";
        public static final String COLUMN_PRICE = "dish_price";
        public static final String COLUMN_IS_NONVEG = "dish_is_nonveg";

        public static Uri buildDishUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
