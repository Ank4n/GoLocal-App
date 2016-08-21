package space.ankan.golocal.screens;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.AppConstants;
import space.ankan.golocal.core.LoggedInActivity;
import space.ankan.golocal.screens.chat.ChannelsFragment;
import space.ankan.golocal.screens.mykitchen.ManageKitchenFragment;
import space.ankan.golocal.screens.mykitchen.addDish.AddDishActivity;
import space.ankan.golocal.screens.nearbykitchens.KitchenListFragment;
import space.ankan.golocal.screens.setupkitchen.SetupKitchenFragment;
import space.ankan.golocal.services.FetchAddressIntentService;

public class MainActivity extends LoggedInActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int TAB_COUNT = 3;
    private static final int SETUP_KITCHEN_TAB = 0;
    private static final int OWNER_KITCHENS_NEARBY_TAB = 0;
    private static final int DEFAULT_KITCHENS_NEARBY_TAB = 1;
    private static final int MANAGE_KITCHEN_TAB = 1;
    private static final int CHAT_TAB = 2;

    private static final int GPS_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 11;

    private int setupKitchenSelect;
    private int setupKitchenDeselect;
    private int kitchenNearbySelect;
    private int kitchenNearbyDeselect;
    private int chatDeselect;
    private int chatSelect;
    private int manageKitchenDeselect;
    private int manageKitchenSelect;

    private ChannelsFragment channelsFragment;
    private SetupKitchenFragment setupKitchenFragment;
    private ManageKitchenFragment manageKitchenFragment;
    private KitchenListFragment kitchenListFragment;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private String currentAddressText;
    private AddressResultReceiver resultReceiver;

    @BindView(R.id.fab)
    FloatingActionButton addDishFab;


    private static final String PAGER_STATE = "state";

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab0)
    ImageView tab0;

    @BindView(R.id.tab1)
    ImageView tab1;

    @BindView(R.id.tab2)
    ImageView tab2;

    @BindView(R.id.tabLayout)
    View tabLayout;

    private ImageView[] tabs;
    private int[] defaultSelectIcons;
    private int[] defaultDeselectIcons;
    private int[] ownerSelectIcons;
    private int[] ownerDeselectIcons;
    private int[] defaultTitle;
    private int[] ownerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initImageResources();
        setupToolbar();
        setupViewPager(savedInstanceState);
        setupFab();
        createLocationRequest();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //FIXME add two pane mode
    }


    private void initImageResources() {
        setupKitchenSelect = R.drawable.setup_kitchen_filled;
        setupKitchenDeselect = R.drawable.setup_kitchen_black;
        kitchenNearbySelect = R.drawable.kitchens_nearby_white;
        kitchenNearbyDeselect = R.drawable.kitchens_nearby_black;
        chatDeselect = R.drawable.chat_black;
        chatSelect = R.drawable.chat_white;
        manageKitchenDeselect = R.drawable.manage_black;
        manageKitchenSelect = R.drawable.manage_white;
        tabs = new ImageView[]{tab0, tab1, tab2};
        defaultSelectIcons = new int[]{setupKitchenSelect, kitchenNearbySelect, chatSelect};
        defaultDeselectIcons = new int[]{setupKitchenDeselect, kitchenNearbyDeselect, chatDeselect};
        ownerSelectIcons = new int[]{kitchenNearbySelect, manageKitchenSelect, chatSelect};
        ownerDeselectIcons = new int[]{kitchenNearbyDeselect, manageKitchenDeselect, chatDeselect};
        defaultTitle = new int[]{R.string.setup_kitchen_title, R.string.kitchen_nearby_title, R.string.chat_title};
        ownerTitle = new int[]{R.string.kitchen_nearby_title, R.string.manage_title, R.string.chat_title};
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        toolbar.setTitle(getResources().getString(R.string.app_name));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(Bundle savedInstanceState) {

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), TAB_COUNT);
        initializeFragments(adapter);

        mViewPager.setAdapter(adapter);

        mViewPager.setSaveEnabled(true);
        mViewPager.setOffscreenPageLimit(2);
        if (savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt(PAGER_STATE));
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                formatTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1); //middle page
        formatTabs(1);
        for (int i = 0; i < tabs.length; i++) {
            final int position = i;
            tabs[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(position);
                }
            });
        }

    }

    private void initializeFragments(FragmentAdapter adapter) {
        adapter.clearFragments();

        channelsFragment = new ChannelsFragment();
        kitchenListFragment = new KitchenListFragment();

        if (isUserKitchenOwner()) {
            manageKitchenFragment = ManageKitchenFragment.newInstance();
            adapter.addFragment(kitchenListFragment);
            adapter.addFragment(manageKitchenFragment);
            adapter.addFragment(channelsFragment);

        } else {
            setupKitchenFragment = SetupKitchenFragment.newInstance();
            adapter.addFragment(setupKitchenFragment);
            adapter.addFragment(kitchenListFragment);
            adapter.addFragment(channelsFragment);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private void formatTabs(int position) {
        for (int i = 0; i < TAB_COUNT; i++) {
            if (i == position)
                selectTab(i);
            else
                deselectTab(i);
        }
        if (isUserKitchenOwner()) {
            getSupportActionBar().setTitle(ownerTitle[position]);
            if (position == MANAGE_KITCHEN_TAB)
                addDishFab.show();
            else addDishFab.hide();

        } else {
            getSupportActionBar().setTitle(defaultTitle[position]);
            addDishFab.hide();
        }
    }

    private void selectTab(int position) {

        ImageView tab = tabs[position];
        tab.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_select));

        //kitchen owner layout
        if (isUserKitchenOwner())
            tab.setImageDrawable(ContextCompat.getDrawable(this, ownerSelectIcons[position]));

            // default layout
        else
            tab.setImageDrawable(ContextCompat.getDrawable(this, defaultSelectIcons[position]));


    }

    private void deselectTab(int position) {
        ImageView tab = tabs[position];
        tab.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_deselect));

        //kitchen owner layout
        if (isUserKitchenOwner())
            tab.setImageDrawable(ContextCompat.getDrawable(this, ownerDeselectIcons[position]));

            // default layout
        else
            tab.setImageDrawable(ContextCompat.getDrawable(this, defaultDeselectIcons[position]));
    }

    private void setupFab() {
        addDishFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDishActivity.createIntent(MainActivity.this);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int state = mViewPager.getCurrentItem();
        outState.putInt(PAGER_STATE, state);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(PAGER_STATE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION);

        checkLocationSettings();
    }


    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    protected void startLocationUpdates() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationUpdates();

                break;


        }
    }

    private void createLocationRequest() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60 * 1000);// 1 minute interval
        mLocationRequest.setFastestInterval(5000); // 5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        log("location changed:" + mLastLocation);
        if (mLastLocation == null) return;
        if (kitchenListFragment != null)
            kitchenListFragment.updateLocation(mLastLocation);
        resultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(RECEIVER, resultReceiver);
        intent.putExtra(LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);

    }

    public Location getLocation() {
        return mLastLocation;
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            currentAddressText = resultData.getString(AppConstants.RESULT_DATA_KEY);
            if (kitchenListFragment != null && !TextUtils.isEmpty(currentAddressText))
                kitchenListFragment.updateLocation(currentAddressText);

            // Show a toast message if an address was found.


        }
    }
}
