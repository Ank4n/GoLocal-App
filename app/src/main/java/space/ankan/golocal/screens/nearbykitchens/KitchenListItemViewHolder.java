package space.ankan.golocal.screens.nearbykitchens;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import space.ankan.golocal.R;

/**
 * Created by Ankan.
 */
 class KitchenListItemViewHolder extends RecyclerView.ViewHolder {

     final View mView;
     final ImageView mKitchenImage;
     final TextView mTitle;
     final TextView mDesc;
     final TextView rating;
     final ImageView mfavourite;

     KitchenListItemViewHolder(View view) {
        super(view);
        mView = view;
        mKitchenImage = (ImageView) view.findViewById(R.id.kitchen_image);
        mTitle = (TextView) view.findViewById(R.id.kitchen_title);
        mDesc = (TextView) view.findViewById(R.id.kitchen_description);
        rating = (TextView) view.findViewById(R.id.rating_val);
        mfavourite = (ImageView) view.findViewById(R.id.favourite);

    }
}
