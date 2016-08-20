package space.ankan.golocal.screens.nearbykitchens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.screens.mykitchen.DishAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class KitchenDetailFragment extends BaseFragment {

    private static final String LOG_CAT = KitchenDetailFragment.class.getSimpleName();
    public static final String MOVIE_INFO = "movie";

    private Kitchen mKitchen;

    private ShareActionProvider mShareActionProvider;

    @BindView(R.id.kitchen_description)
    TextView kitchenDescription;

    @BindView(R.id.dish_list)
    RecyclerView mDishesRecycler;
    private DishAdapter adapter;


    @BindView(R.id.kitchen_address)
    TextView kitchenAddress;

    public KitchenDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kitchen_detail, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        fill();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_kitchen_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_share_kitchen);
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void fill() {

        if (getArguments() != null)
            mKitchen = (Kitchen) getArguments().getSerializable("kitchen");
        else
            Log.e(LOG_CAT, "Null arguments in the fragment");

        setShareIntent();
        if (mKitchen == null) return;
        kitchenDescription.setText(mKitchen.description);
        kitchenAddress.setText(mKitchen.address);
        setupDishRecycler();

    }

    private void setupDishRecycler() {
        adapter = new DishAdapter(getActivity(), new ArrayList<Dish>(), true);
        mDishesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mDishesRecycler.setAdapter(adapter);
        syncWithFirebase();
    }

    private void syncWithFirebase() {
        getFirebaseHelper().getDishesReference(mKitchen.key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Dish dish = dataSnapshot.getValue(Dish.class);
                adapter.add(dish);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setShareIntent() {

        String shareText = "Share";

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        if (mShareActionProvider != null)
            mShareActionProvider.setShareIntent(shareIntent);
    }


}
