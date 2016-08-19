package space.ankan.golocal.screens.nearbykitchens;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import space.ankan.golocal.R;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * Created by anurag on 18-Dec-15.
 */
public class KitchenListItemViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public final ImageView mKitchenImage;
    public final TextView mTitle;
    public final TextView mDesc;
    public final TextView rating;
    public final ImageView mfavourite;

    public KitchenListItemViewHolder(View view) {
        super(view);
        mView = view;
        mKitchenImage = (ImageView) view.findViewById(R.id.kitchen_image);
        mTitle = (TextView) view.findViewById(R.id.kitchen_title);
        mDesc = (TextView) view.findViewById(R.id.kitchen_description);
        rating = (TextView) view.findViewById(R.id.rating_val);
        mfavourite = (ImageView) view.findViewById(R.id.favourite);

    }
}
