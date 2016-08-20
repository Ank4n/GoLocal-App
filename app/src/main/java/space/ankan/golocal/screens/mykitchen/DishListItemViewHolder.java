package space.ankan.golocal.screens.mykitchen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import space.ankan.golocal.R;

/**
 * Created by anurag on 18-Dec-15.
 */
public class DishListItemViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public final TextView mTitle;
    public final TextView mPrice;
    public final ImageView foodType;

    public DishListItemViewHolder(View view) {
        super(view);
        mView = view;
        mTitle = (TextView) view.findViewById(R.id.dish_name);
        mPrice = (TextView) view.findViewById(R.id.dish_price);
        foodType = (ImageView) view.findViewById(R.id.foodType);

    }
}
