package space.ankan.golocal.screens.nearbykitchens;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import space.ankan.golocal.R;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * Created by anurag on 18-Dec-15.
 */
public class KitchenListItemViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public final ImageView mImageView;
    public Kitchen mBoundMovie;

    public KitchenListItemViewHolder(View view) {
        super(view);
        mView = view;
        mImageView = (ImageView) view.findViewById(R.id.kitchen_image);
    }
}
