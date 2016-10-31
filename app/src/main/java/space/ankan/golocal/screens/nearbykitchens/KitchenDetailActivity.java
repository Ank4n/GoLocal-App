package space.ankan.golocal.screens.nearbykitchens;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import space.ankan.golocal.R;
import space.ankan.golocal.core.LoggedInActivity;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.persistence.DBContract;
import space.ankan.golocal.screens.chat.ChatActivity;
import space.ankan.golocal.utils.DBUtils;


public class KitchenDetailActivity extends LoggedInActivity {


    @BindView(R.id.favourite_button)
    FloatingActionButton mFavouriteButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCtl;

    @BindView(R.id.kitchen_image)
    ImageView mKitchenImage;

    private Kitchen mKitchen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mKitchen = (Kitchen) getIntent().getSerializableExtra("kitchen");
        Kitchen c = DBUtils.queryKitchenById(getContentResolver(), mKitchen.key);
        if (c != null && c.isFavourite) mKitchen.isFavourite = true;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        if (mKitchen != null) {

            mCtl.setTitle(mKitchen.name);
            //getSupportActionBar().setTitle(mKitchen.name);
            formatFAB();

            if (TextUtils.isEmpty(mKitchen.imageUrl)) {
                Picasso.with(this).load(getString(R.string.kitchen_default_image))
                        .into(mKitchenImage);
                mKitchenImage.setImageAlpha(128);
            } else
                Picasso.with(this).load(mKitchen.imageUrl).into(mKitchenImage);


        }

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.kitchen_detail_container, KitchenDetailFragment.newInstance(mKitchen))
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.favourite_button)
    public void saveFavourite(View v) {

        mKitchen.isFavourite = (!mKitchen.isFavourite);
        getFirebaseHelper().updateFavourite(mKitchen);

        if (!mKitchen.isFavourite) {
            DBUtils.deleteKitchen(getContentResolver(), mKitchen.key);
            Toast.makeText(this, mKitchen.name + " deleted from favourites.", Toast.LENGTH_SHORT).show();
        } else {
            DBUtils.insertKitchen(getContentResolver(), mKitchen);
            Toast.makeText(this, mKitchen.name + " saved to favourites.", Toast.LENGTH_SHORT).show();
        }
        formatFAB();
        updateWidgets();
    }

    @OnClick(R.id.start_chat)
    void startChat() {
        if (getCurrentUser().getUid().equals(mKitchen.userId))
            Toast.makeText(this, R.string.chat_with_own_kitchen, Toast.LENGTH_LONG).show();
        else
            ChatActivity.createIntent(this, getSharedPref().getString(mKitchen.userId, null), mKitchen.name, mKitchen.userId);
    }

    private void formatFAB() {
        if (mKitchen.isFavourite)
            mFavouriteButton.setImageResource(R.drawable.ic_favorite_white_filled);
        else
            mFavouriteButton.setImageResource(R.drawable.ic_favorite_border_white);

    }

    public static void createIntent(Context c, Kitchen kitchen) {
        Intent i = new Intent(c, KitchenDetailActivity.class);
        i.putExtra("kitchen", kitchen);
        c.startActivity(i);

    }
}
