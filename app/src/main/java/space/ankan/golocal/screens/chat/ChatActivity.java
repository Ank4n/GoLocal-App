package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseActivity;

public class ChatActivity extends BaseActivity {

    private String channelId;
    private String channelName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        channelId = getIntent().getStringExtra("channel_id");
        channelName = getIntent().getStringExtra("channel_name");
        userId = getIntent().getStringExtra("user_id");
        if (!TextUtils.isEmpty(channelName))
            getSupportActionBar().setTitle(channelName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, ChatActivityFragment.createInstance(channelId, channelName, userId)).commit();
    }

    public static void createIntent(Context context, String channelId, String channelName, String userId) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra("channel_id", channelId);
        i.putExtra("channel_name", channelName);
        i.putExtra("user_id", userId);
        context.startActivity(i);
    }
}
