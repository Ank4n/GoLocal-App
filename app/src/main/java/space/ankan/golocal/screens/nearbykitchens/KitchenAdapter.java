package space.ankan.golocal.screens.nearbykitchens;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseActivity;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * Created by anurag on 18-Dec-15.
 */
public class KitchenAdapter extends RecyclerView.Adapter<KitchenListItemViewHolder> {

    private Activity mActivity;
    private ArrayList<Kitchen> kitchens;

    public KitchenAdapter(Activity activity, ArrayList<Kitchen> list) {
        this.mActivity = activity;
        this.kitchens = list;
    }

    public Kitchen getItem(int position) {
        return kitchens.get(position);
    }

    public ArrayList<Kitchen> getItems() {
        return kitchens;
    }

    @Override
    public KitchenListItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(mActivity)
                .inflate(R.layout.list_item_kitchen, parent, false);
        BaseActivity.log("here " + i);
        return new KitchenListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KitchenListItemViewHolder holder, int i) {


    }

    @Override
    public int getItemCount() {
        if (kitchens == null)
            return 0;
        return kitchens.size();
    }

    public int getCount() {
        return getItemCount();
    }

    public void add(Kitchen movie) {
        this.kitchens.add(movie);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.kitchens.clear();
    }

    public void addAll(List<Kitchen> movies) {
        if (!this.kitchens.isEmpty())
            return;
        this.kitchens.addAll(movies);
        this.notifyDataSetChanged();
    }

}
