package space.ankan.golocal.screens.mykitchen;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.core.TwoPaneListener;
import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.screens.mykitchen.addDish.AddDishActivity;

/**
 * Created by Ankan.
 */
public class DishAdapter extends RecyclerView.Adapter<DishListItemViewHolder> {

    private Context mContext;
    private ArrayList<Dish> dishes;
    private boolean horizontal; //for detail page
    private TwoPaneListener mTwoPaneListener;
    private int selected;

    DishAdapter(Context context, ArrayList<Dish> list) {
        this(context, list, false);
    }

    public DishAdapter(Context context, ArrayList<Dish> list, boolean horizontal) {
        this.mContext = context;
        this.dishes = list;
        this.horizontal = horizontal;
    }

    void setmTwoPaneListener(TwoPaneListener mTwoPaneListener) {
        this.mTwoPaneListener = mTwoPaneListener;
    }

    @SuppressWarnings("unused")
    public Dish getItem(int position) {
        return dishes.get(position);
    }

    @SuppressWarnings("unused")
    public ArrayList<Dish> getItems() {
        return dishes;
    }

    @Override
    public DishListItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view;

        if (!horizontal) view = LayoutInflater.from(mContext)
                .inflate(R.layout.dish_list_item, parent, false);
        else view = LayoutInflater.from(mContext)
                .inflate(R.layout.dish_list_item_horizontal, parent, false);
        return new DishListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DishListItemViewHolder holder, int i) {
        final Dish item = dishes.get(i);
        final int index = i;
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (horizontal) return;

                if (mTwoPaneListener != null && mTwoPaneListener.isTwoPane()) {
                    mTwoPaneListener.setupManageDishView(item);
                    configureSelection(index);
                } else
                    AddDishActivity.createIntent(mContext, item);
            }
        });

        int bgColor = R.color.white;
        if (item.isSelected)
            bgColor = R.color.item_selected_background;

        holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, bgColor));

        holder.mTitle.setText(item.name);
        holder.mPrice.setText(mContext.getString(R.string.price_with_rupees_currency, String.valueOf(item.price)));
        int foodTypeImage = item.nonVeg ? R.drawable.ic_non_vegetarian : R.drawable.ic_vegetarian;
        String contentDesc = item.nonVeg ? mContext.getString(R.string.cd_food_type_non_vegetarian) : mContext.getString(R.string.cd_food_type_vegetarian);
        holder.foodType.setImageDrawable(ContextCompat.getDrawable(mContext, foodTypeImage));
        holder.foodType.setContentDescription(contentDesc);

    }

    private void configureSelection(int position) {
        if (selected != -1)
            dishes.get(selected).isSelected = false;
        selected = position;
        dishes.get(selected).isSelected = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dishes == null)
            return 0;
        return dishes.size();
    }

    @SuppressWarnings("unused")
    public int getCount() {
        return getItemCount();
    }

    public void add(Dish dish) {
        this.dishes.add(dish);
        this.notifyDataSetChanged();
    }

    void replace(final Dish dish) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < dishes.size(); i++) {
                    if (dish.key.equals(dishes.get(i).key)) {
                        dishes.remove(i);
                        break;
                    }
                }
                dishes.add(dish);
                notifyDataSetChanged();
            }
        });

    }

    @SuppressWarnings("unused")
    public void clear() {
        this.dishes.clear();
    }

    @SuppressWarnings("unused")
    public void addAll(List<Dish> dishes) {
        if (!this.dishes.isEmpty())
            return;
        this.dishes.addAll(dishes);
        this.notifyDataSetChanged();
    }

    void clearSelection() {
        if (selected == -1) return;
        dishes.get(selected).isSelected = false;
        selected = -1;
        notifyDataSetChanged();

    }
}
