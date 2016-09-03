package space.ankan.golocal.screens.nearbykitchens;

import android.content.Context;
import android.content.Intent;
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
import space.ankan.golocal.screens.chat.ChatActivity;


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

            if (TextUtils.isEmpty(mKitchen.imageUrl))
                Picasso.with(this).load("http://www.ikea.com/ms/media/cho_room/20153/kitchen/20153_cosk07a/20153_cosk07a_01_PH124156.jpg")
                        .into(mKitchenImage);

            else
                Picasso.with(this).load(mKitchen.imageUrl).into(mKitchenImage);


        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putSerializable("kitchen", mKitchen);

            KitchenDetailFragment fragment = new KitchenDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.kitchen_detail_container, fragment)
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

        if (mKitchen.isFavourite) {
            //TODO Implement this
            Toast.makeText(this, mKitchen.name + " deleted from favourites.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, mKitchen.name + " saved to favourites.", Toast.LENGTH_SHORT).show();
        }
        mKitchen.isFavourite = (!mKitchen.isFavourite);
        formatFAB();

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
