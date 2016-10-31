package space.ankan.golocal.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import space.ankan.golocal.R;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.utils.CommonUtils;
import space.ankan.golocal.utils.DBUtils;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = WidgetRemoteViewsService.class.getSimpleName();

    private void dump(String message) {
        Log.wtf("Widget", message);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
                dump("onCreate");
            }

            @Override
            public void onDataSetChanged() {
                dump("onDataSetChanged");
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                data = DBUtils.queryFavouriteKitchens(getContentResolver());
                dump("Count " + getCount());
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                dump("getViewAt " + position + " and cursor at " + data.getPosition());
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);
                Kitchen kitchen = DBUtils.getKitchenFromCursor(data);
                dump("Kitchen found: " + kitchen.name);

                if (kitchen == null) return null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, kitchen.description);
                }
                views.setTextViewText(R.id.widget_kitchen_title, kitchen.name);
                views.setTextViewText(R.id.widget_kitchen_description, kitchen.description);
                views.setTextViewText(R.id.widget_rating_val, String.valueOf(CommonUtils.roundTwoDecimals(kitchen.overallRating)));
                views.setImageViewResource(R.id.widget_favourite, R.drawable.ic_favorite_red_300_18dp);

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra("kitchen", kitchen);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_kitchen_image, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                dump("getLoadingView");
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position)) {
                    dump("Item Id " + data.getLong(0));
                    return data.getLong(0);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                dump("hasStableIds");
                return true;
            }
        };
    }
}
