package space.ankan.golocal.screens.nearbykitchens;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import space.ankan.golocal.R;
import space.ankan.golocal.core.TwoPaneListener;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.utils.CommonUtils;
import space.ankan.golocal.utils.DBUtils;

/**
 * Created by Ankan.
 */
public class KitchenAdapter extends RecyclerView.Adapter<KitchenListItemViewHolder> {

    private Context mContext;
    private ArrayList<Kitchen> kitchens;
    private Set<String> favouriteKitchenIdList;
    private TwoPaneListener twoPaneListener;
    private int selected = -1;

    KitchenAdapter(Context context, ArrayList<Kitchen> list, Set<String> favouriteKitchenIdList, TwoPaneListener twoPaneListener) {
        this.mContext = context;
        this.kitchens = list;
        this.favouriteKitchenIdList = favouriteKitchenIdList;
        this.twoPaneListener = twoPaneListener;
    }

    void setTwoPaneListener(TwoPaneListener twoPaneListener) {
        this.twoPaneListener = twoPaneListener;
    }

    @SuppressWarnings("unused")
    public Kitchen getItem(int position) {
        return kitchens.get(position);
    }

    @SuppressWarnings("unused")
    public ArrayList<Kitchen> getItems() {
        return kitchens;
    }

    @Override
    public KitchenListItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_kitchen, parent, false);
        return new KitchenListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KitchenListItemViewHolder holder, int i) {
        final Kitchen kitchen = kitchens.get(i);
        final int index = i;
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (twoPaneListener != null && twoPaneListener.isTwoPane()) {
                    twoPaneListener.setupKitchenDetail(kitchen);
                    configureSelection(index);
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
        else {
            holder.mKitchenImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.manage_black));
            holder.mKitchenImage.setAlpha(0.4f);
        }
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

    private void configureSelection(int position) {
        if (selected != -1)
            kitchens.get(selected).isSelected = false;
        selected = position;
        kitchens.get(selected).isSelected = true;
        notifyDataSetChanged();
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
        }
        else {
            holder.mfavourite.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_light));
            holder.mfavourite.setContentDescription(mContext.getString(R.string.cd_not_favourite));
        }

    }

    @Override
    public int getItemCount() {
        if (kitchens == null)
            return 0;
        return kitchens.size();
    }

    @SuppressWarnings("unused")
    public int getCount() {
        return getItemCount();
    }

    @SuppressWarnings("unused")
    public void setFavouriteKitchenIdList(Set<String> favouriteKitchenIdList) {
        this.favouriteKitchenIdList = favouriteKitchenIdList;
    }

    void reformatView() {

        for (Kitchen k : kitchens) {
            k.isFavourite = favouriteKitchenIdList != null && favouriteKitchenIdList.contains(k.key);
        }
        notifyDataSetChanged();
    }

    public void add(Kitchen kitchen) {
        kitchen.isFavourite = (favouriteKitchenIdList != null && favouriteKitchenIdList.contains(kitchen.key));
        this.kitchens.add(kitchen);
        this.notifyDataSetChanged();
    }

    void clear() {
        this.kitchens.clear();
        this.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void addAll(List<Kitchen> kitchens) {
        if (!this.kitchens.isEmpty())
            return;
        this.kitchens.addAll(kitchens);
        this.notifyDataSetChanged();
    }


}
