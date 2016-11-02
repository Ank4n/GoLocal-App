package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseActivity;
import space.ankan.golocal.utils.RedirectionUtils;

/**
 * Created by ankan.
 * Activity which holds the chat fragment for enabling active conversation
 */

public class ChatActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String channelId = getIntent().getStringExtra(KEY_CHANNEL_ID);
        String channelName = getIntent().getStringExtra(KEY_CHANNEL_NAME);
        String userId = getIntent().getStringExtra(KEY_USER_ID);
        if (!TextUtils.isEmpty(channelName))
            getSupportActionBar().setTitle(channelName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectionUtils.onBackPressed(ChatActivity.this);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, ChatActivityFragment.createInstance(channelId, channelName, userId)).commit();
    }

    public static void createIntent(Context context, String channelId, String channelName, String userId) {
        context.startActivity(getCreateIntent(context, channelId, channelName, userId));
    }

    public static Intent getCreateIntent(Context context, String channelId, String channelName, String userId) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(KEY_CHANNEL_ID, channelId);
        i.putExtra(KEY_CHANNEL_NAME, channelName);
        i.putExtra(KEY_USER_ID, userId);
        return i;
    }

    @Override
    public void onBackPressed() {
        RedirectionUtils.onBackPressed(this);
    }
}
