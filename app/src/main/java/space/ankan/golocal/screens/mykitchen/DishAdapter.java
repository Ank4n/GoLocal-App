package space.ankan.golocal.screens.mykitchen;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.model.kitchens.Dish;

/**
 * Created by anurag on 18-Dec-15.
 */
public class DishAdapter extends RecyclerView.Adapter<DishListItemViewHolder> {

    private Context mContext;
    private ArrayList<Dish> dishes;
    private boolean horizontal; //for detail page

    public DishAdapter(Context context, ArrayList<Dish> list) {
        this(context, list, false);
    }

    public DishAdapter(Context context, ArrayList<Dish> list, boolean horizontal) {
        this.mContext = context;
        this.dishes = list;
        this.horizontal = horizontal;
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
    public void onBindViewHolder(final DishListItemViewHolder holder, int i) {
        Dish item = dishes.get(i);
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "The editor is not implemented yet.", Toast.LENGTH_LONG).show();
            }
        });

        holder.mTitle.setText(item.name);
        holder.mPrice.setText("Rs. " + String.valueOf(item.price));
        int foodTypeImage = item.nonVeg ? R.drawable.ic_non_vegetarian : R.drawable.ic_vegetarian;
        holder.foodType.setImageDrawable(ContextCompat.getDrawable(mContext, foodTypeImage));

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

    public void clear() {
        this.dishes.clear();
    }

    public void addAll(List<Dish> dishes) {
        if (!this.dishes.isEmpty())
            return;
        this.dishes.addAll(dishes);
        this.notifyDataSetChanged();
    }

}
