package space.ankan.golocal.screens;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.LoggedInActivity;
import space.ankan.golocal.screens.mykitchen.adddish.AddDishActivity;
import space.ankan.golocal.screens.nearbykitchens.KitchenListFragment;

public class MainActivity extends LoggedInActivity {

    public static final int TAB_COUNT = 3;
    private static final int SETUP_KITCHEN_TAB = 0;
    private static final int OWNER_KITCHENS_NEARBY_TAB = 0;
    private static final int DEFAULT_KITCHENS_NEARBY_TAB = 1;
    private static final int MANAGE_KITCHEN_TAB = 1;
    private static final int CHAT_TAB = 2;

    private int setupKitchenSelect;
    private int setupKitchenDeselect;
    private int kitchenNearbySelect;
    private int kitchenNearbyDeselect;
    private int chatDeselect;
    private int chatSelect;
    private int manageKitchenDeselect;
    private int manageKitchenSelect;

    private Fragment chatFragment;
    private Fragment setupKitchenFragment;
    private Fragment manageKitchenFragment;
    private Fragment kitchensNearbyFragment;

    @BindView(R.id.fab)
    FloatingActionButton fab;


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

    private void setupViewPager(Bundle savedInstanceState) {

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        //FIXME fix order dependency using indexes
        chatFragment = new KitchenListFragment();
        setupKitchenFragment = new KitchenListFragment();
        kitchensNearbyFragment = new KitchenListFragment();
        adapter.addFragment(setupKitchenFragment);
        adapter.addFragment(kitchensNearbyFragment);
        adapter.addFragment(chatFragment);
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
                fab.show();
        } else
            getSupportActionBar().setTitle(defaultTitle[position]);

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
        //fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
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
}
