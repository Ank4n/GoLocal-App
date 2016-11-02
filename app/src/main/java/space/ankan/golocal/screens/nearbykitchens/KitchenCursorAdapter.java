package space.ankan.golocal.screens.nearbykitchens;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Set;

import space.ankan.golocal.R;
import space.ankan.golocal.core.TwoPaneListener;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.utils.CommonUtils;
import space.ankan.golocal.utils.DBUtils;


/**
 * Created by Ankan.
 * Recycler View Adapter compatible with Loader
 */


class KitchenCursorAdapter extends RecyclerView.Adapter<KitchenListItemViewHolder> {

    private Context mContext;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;
    private TwoPaneListener twoPaneListener;
    private Set<String> favouriteKitchenIdList;


    KitchenCursorAdapter(Context context, Cursor cursor, Set<String> favouriteKitchenIdList, TwoPaneListener twoPaneListener) {
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
        this.favouriteKitchenIdList = favouriteKitchenIdList;
        this.twoPaneListener = twoPaneListener;
    }

    @SuppressWarnings("unused")
    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    @Override
    public KitchenListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_kitchen, parent, false);
        return new KitchenListItemViewHolder(view);
    }

    @SuppressWarnings("unused")
    public void setTwoPaneListener(TwoPaneListener twoPaneListener) {
        this.twoPaneListener = twoPaneListener;
    }

    @Override
    public void onBindViewHolder(final KitchenListItemViewHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        final Kitchen kitchen = DBUtils.getKitchenFromCursor(mCursor);
        if (kitchen == null) return;
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (twoPaneListener != null && twoPaneListener.isTwoPane()) {
                    twoPaneListener.setupKitchenDetail(kitchen);
                } else
                    KitchenDetailActivity.createIntent(mContext, kitchen);
            }
        });

        int bgColor = R.color.white;
        if (kitchen.isSelected)
            bgColor = R.color.item_selected_background;

        holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, bgColor));

        if (!TextUtils.isEmpty(kitchen.imageUrl))
            Picasso.with(mContext).load(kitchen.imageUrl).into(holder.mKitchenImage);
        else holder.mKitchenImage.setAlpha(0.4f);

        formatIcon(kitchen, holder);
        holder.mfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteListenerAction(kitchen, holder);
            }
        });
        holder.mTitle.setText(kitchen.name);
        holder.mDesc.setText(kitchen.description);
        holder.rating.setText(String.valueOf(CommonUtils.roundTwoDecimals(kitchen.overallRating)));

    }

    private void favouriteListenerAction(Kitchen kitchen, KitchenListItemViewHolder holder) {
        kitchen.isFavourite = (!kitchen.isFavourite);
        //firebaseHelper.updateFavourite(kitchen);

        if (!kitchen.isFavourite) {
            DBUtils.deleteKitchen(mContext.getContentResolver(), kitchen.key);
            favouriteKitchenIdList.remove(kitchen.key);
            Toast.makeText(mContext, kitchen.name + " deleted from favourites.", Toast.LENGTH_SHORT).show();
        } else {
            DBUtils.insertKitchen(mContext.getContentResolver(), kitchen);
            Toast.makeText(mContext, kitchen.name + " saved to favourites.", Toast.LENGTH_SHORT).show();
            favouriteKitchenIdList.add(kitchen.key);
            // addFavKitchen(kitchen.key);
        }
        formatIcon(kitchen, holder);
        CommonUtils.updateWidgets(mContext);
    }

    private void formatIcon(Kitchen kitchen, KitchenListItemViewHolder holder) {
        if (kitchen.isFavourite) {
            holder.mfavourite.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_red_300_18dp));
            holder.mfavourite.setContentDescription(mContext.getString(R.string.cd_favourite));
        }else {
            holder.mfavourite.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_light));
            holder.mfavourite.setContentDescription(mContext.getString(R.string.cd_not_favourite));
        }
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    @SuppressWarnings("unused")
    void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

}
