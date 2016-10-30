package space.ankan.golocal.screens.mykitchen;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.core.TwoPaneListener;
import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.screens.mykitchen.addDish.AddDishActivity;
import space.ankan.golocal.screens.mykitchen.addDish.AddDishFragment;

/**
 * Created by anurag on 18-Dec-15.
 */
public class DishAdapter extends RecyclerView.Adapter<DishListItemViewHolder> {

    private Context mContext;
    private ArrayList<Dish> dishes;
    private boolean horizontal; //for detail page
    private TwoPaneListener mTwoPaneListener;
    private int selected;

    public DishAdapter(Context context, ArrayList<Dish> list) {
        this(context, list, false);
    }

    public DishAdapter(Context context, ArrayList<Dish> list, boolean horizontal) {
        this.mContext = context;
        this.dishes = list;
        this.horizontal = horizontal;
    }

    public void setmTwoPaneListener(TwoPaneListener mTwoPaneListener) {
        this.mTwoPaneListener = mTwoPaneListener;
    }

    public Dish getItem(int position) {
        return dishes.get(position);
    }

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
    public void onBindViewHolder(final DishListItemViewHolder holder, final int i) {
        final Dish item = dishes.get(i);
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (horizontal) return;

                if (mTwoPaneListener.isTwoPane()) {
                    mTwoPaneListener.setupManageDishView(item);
                    configureSelection(i);
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
        holder.foodType.setImageDrawable(ContextCompat.getDrawable(mContext, foodTypeImage));

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

    public int getCount() {
        return getItemCount();
    }

    public void add(Dish dish) {
        this.dishes.add(dish);
        this.notifyDataSetChanged();
    }

    public void replace(final Dish dish) {
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
            }
        });
        this.dishes.add(dish);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.dishes.clear();
    }

    public void addAll(List<Dish> dishes) {
        if (!this.dishes.isEmpty())
            return;
        this.dishes.addAll(dishes);
        this.notifyDataSetChanged();
    }

    public void clearSelection() {
        if (selected == -1) return;
        dishes.get(selected).isSelected = false;
        selected = -1;
        notifyDataSetChanged();

    }
}
