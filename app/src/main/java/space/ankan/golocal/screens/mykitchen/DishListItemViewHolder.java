package space.ankan.golocal.screens.mykitchen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import space.ankan.golocal.R;

/**
 * Created by Ankan.
 */
class DishListItemViewHolder extends RecyclerView.ViewHolder {

    final View mView;
    final TextView mTitle;
    final TextView mPrice;
    final ImageView foodType;

    DishListItemViewHolder(View view) {
        super(view);
        mView = view;
        mTitle = (TextView) view.findViewById(R.id.dish_name);
        mPrice = (TextView) view.findViewById(R.id.dish_price);
        foodType = (ImageView) view.findViewById(R.id.foodType);

    }
}
