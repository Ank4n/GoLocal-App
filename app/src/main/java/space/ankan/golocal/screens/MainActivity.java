package space.ankan.golocal.screens;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.LoggedInActivity;

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

    private ImageView[] tabs = new ImageView[]{tab0, tab1, tab2};
    private int[] defaultSelectIcons = new int[]{setupKitchenSelect, kitchenNearbySelect, chatSelect};
    private int[] defaultDeselectIcons = new int[]{setupKitchenDeselect, kitchenNearbyDeselect, chatDeselect};
    private int[] ownerSelectIcons = new int[]{kitchenNearbySelect, manageKitchenSelect, chatSelect};
    private int[] ownerDeselectIcons = new int[]{kitchenNearbyDeselect, manageKitchenDeselect, chatDeselect};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupViewPager(savedInstanceState);

        //FIXME add two pane mode
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        toolbar.setTitle(getResources().getString(R.string.app_name));

    }

    private void setupViewPager(Bundle savedInstanceState) {

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        /*adapter.addFragment();
        adapter.addFragment();
        adapter.addFragment()*/
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

    }

    private void formatTabs(int position) {
        for (int i = 0; i < TAB_COUNT; i++) {
            if (i == position)
                selectTab(i);
            else
                deselectTab(i);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int state = mViewPager.getCurrentItem();
        outState.putInt(PAGER_STATE, state);
        super.onSaveInstanceState(outState);
    }
}
