package space.ankan.golocal.screens.nearbykitchens;

import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.AppConstants;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.persistence.DBContract;
import space.ankan.golocal.screens.MainActivity;
import space.ankan.golocal.utils.CommonUtils;
import space.ankan.golocal.utils.DBUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class KitchenListFragment extends BaseFragment implements GeoQueryEventListener, LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.content_kitchen_list)
    RecyclerView mRecyclerView;

    private View mRootView;
    private KitchenAdapter adapter;
    private double range = 10; //default
    private Location location;
    private GeoQuery mQuery;
    private AlertDialog rangePicker;
    private EditText rangeInput;
    private Double lon = 77.5, lat = 12.9;
    private boolean showingFavourites;
    private KitchenCursorAdapter favouriteAdapter;
    private MenuItem favouriteMenu;
    private String currentHeaderText;
    private Set<String> favouriteKitchens;

    @BindView(R.id.current_location)
    TextView currentLocation;

    @BindView(R.id.address_card)
    View addressCard;

    public KitchenListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = super.inflate(inflater, container, savedInstanceState, R.layout.fragment_kitchen_list);
        ButterKnife.bind(this, mRootView);
        init();
        dLog("starting kitchen list fragment");
        setupRecycler();
        syncWithFirebase();
        setHasOptionsMenu(true);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    private void init() {
        lat = CommonUtils.getLastLocationLatitude(getSharedPref());
        lon = CommonUtils.getLastLocationLongitude(getSharedPref());
        if (lat == null || lon == null) {
            lon = 77.6036207;
            lat = 12.9049946;
        }
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null) return;
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).checkLocationSettings();
            }
        });

        favouriteKitchens = new HashSet<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_kitchen_list, menu);
        favouriteMenu = menu.findItem(R.id.favourites);
        setFavouriteIcon();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.range_set:
                pickRange();
                return true;
            case R.id.favourites:
                showHideFavourites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showHideFavourites() {
        showingFavourites = !showingFavourites;
        setFavouriteIcon();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void setFavouriteIcon() {
        if (favouriteMenu == null) return;

        if (showingFavourites) {
            setupFavourites();
            favouriteMenu.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_red_300_18dp));
            mRecyclerView.setAdapter(favouriteAdapter);
            CommonUtils.setupTextRemoveIfEmpty(currentLocation, R.string.showing_favourites, addressCard);
            //favouriteAdapter.reformatView();
        } else {
            favouriteMenu.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white_18dp));
            mRecyclerView.setAdapter(adapter);
            if (TextUtils.isEmpty(this.currentHeaderText))
                this.currentHeaderText = getSharedPref().getString(AppConstants.CACHED_LAST_ADDRESS, null);
            CommonUtils.setupTextRemoveIfEmpty(currentLocation, currentHeaderText, addressCard);
            adapter.reformatView();
        }
    }

    private void pickRange() {

        if (rangePicker == null) {
            rangeInput = new EditText(getActivity());
            rangeInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rangeInput.setLayoutParams(lp);

            rangePicker = new AlertDialog.Builder(getActivity())
                    .setMessage("Enter range in Kms")
                    .setView(rangeInput)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            CommonUtils.closeKeyBoard(getActivity());
                        }
                    })
                    .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            double r;
                            try {
                                r = Double.parseDouble(rangeInput.getText().toString());
                            } catch (Exception e) {
                                return;
                            }

                            if (r <= 0) {
                                Toast.makeText(getActivity(), R.string.range_less_than_zero, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (r == range) return;
                            range = r;
                            CommonUtils.closeKeyBoard(getActivity());
                            syncWithFirebase();

                        }
                    }).create();
        }

        rangeInput.setText(String.valueOf((int) range));
        rangeInput.setSelection(rangeInput.getText().length());
        rangePicker.show();

    }

    private void setupRecycler() {
        if (adapter == null) {
            adapter = new KitchenAdapter(getActivity(), new ArrayList<Kitchen>(), favouriteKitchens, mTwoPaneListener);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        location = ((MainActivity) getActivity()).getLocation();

    }


    private void setupFavourites() {
        if (favouriteAdapter == null) {
            favouriteAdapter = new KitchenCursorAdapter(getActivity(), null, favouriteKitchens, mTwoPaneListener);
        } //else
        //      Cursor c = DBUtils.queryFavouriteKitchens(getActivity().getContentResolver());
        //     if (!c.moveToFirst()) return;

//        do {
//            Kitchen k = DBUtils.getKitchenFromCursor(c);
//            favouriteKitchens.add(k.key);
//            favouriteAdapter.add(k);
//        } while (c.moveToNext());


    }

    public void updateLocation(Location location) {

        dLog("kitchen list updateLocation");

        if (this.location != null && location.getLatitude() == this.location.getLatitude() && location.getLongitude() == this.location.getLongitude())
            return;
        if (location != null) {
            this.location = location;
            lon = location.getLongitude();
            lat = location.getLatitude();
        }
        syncWithFirebase();
    }

    @Override
    public void onResume() {
        super.onResume();
        DBUtils.queryFavouriteKitchenIDList(getActivity().getContentResolver(), favouriteKitchens);
        adapter.reformatView();
        syncWithFirebase();
        if (showingFavourites)
            setupFavourites();

    }


    private void syncWithFirebase() {
        //dLog("syncing with firebase kitchen list | lat= " + lat + " lon= " + lon);

        if (adapter == null) {
            adapter = new KitchenAdapter(getActivity(), new ArrayList<Kitchen>(), favouriteKitchens, mTwoPaneListener);
            adapter.setTwoPaneListener(mTwoPaneListener);
        }
        if (mQuery != null)
            mQuery.removeAllListeners();

        adapter.clear();

        if (lon == null || lat == null)
            Toast.makeText(getActivity(), R.string.enable_gps, Toast.LENGTH_LONG);

        else {
            mQuery = getFirebaseHelper().buildQueryForKitchens(new GeoLocation(lat, lon),
                    range);
            mQuery.addGeoQueryEventListener(this);
        }
        CommonUtils.closeKeyBoard(getActivity());

    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        getFirebaseHelper().getKitchenReference(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Kitchen kitchen = dataSnapshot.getValue(Kitchen.class);
                    if (kitchen == null) return;
                    kitchen.key = dataSnapshot.getKey();
                    adapter.add(kitchen);
                    //dLog("kitchen key: " + kitchen.key);
                } catch (DatabaseException e) {
                    Log.e(AppConstants.TAG, "Error retrieving item with key " + dataSnapshot.getKey() + ": " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onKeyExited(String key) {

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }

    public void updateLocation(String currentAddressText) {
        if (currentLocation == null) return;
        this.currentHeaderText = currentAddressText;
        CommonUtils.cacheLocationAddress(getSharedPref().edit(), currentAddressText);
        if (showingFavourites) return;
        CommonUtils.setupTextRemoveIfEmpty(currentLocation, this.currentHeaderText, addressCard);

    }

    public void onError(String errorMessage) {
        currentHeaderText = errorMessage;
        CommonUtils.setupTextRemoveIfEmpty(currentLocation, errorMessage, addressCard);

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), DBContract.KitchenEntry.CONTENT_URI,
                DBUtils.KITCHEN_PROJECTION, DBContract.KitchenEntry.COLUMN_IS_FAVOURITE + " = ?", new String[]{"1"},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setupFavourites();
        favouriteAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        setupFavourites();
        favouriteAdapter.swapCursor(null);
    }
}
