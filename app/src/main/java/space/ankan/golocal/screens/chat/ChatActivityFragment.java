package space.ankan.golocal.screens.chat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.channels.Channel;
import space.ankan.golocal.model.channels.ChatMessage;
import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.screens.mykitchen.DishAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatActivityFragment extends BaseFragment implements ChildEventListener {

    private ChatAdapter adapter;
    private View mRootView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.send_message)
    ImageView sendButton;

    @BindView(R.id.entered_text)
    EditText enteredText;

    private String channelId;
    private String channelName;
    private String userId;

    public ChatActivityFragment() {
    }

    public static Fragment createInstance(String channelId, String channelName, String userId) {
        Fragment fragment = new ChatActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel_id", channelId);
        bundle.putString("channel_name", channelName);
        bundle.putString("user_id", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, mRootView);
        channelId = getArguments().getString("channel_id");
        channelName = getArguments().getString("channel_name");
        userId = getArguments().getString("user_id");
        setupRecycler();
        syncWithFirebase();
        setupListeners();
        log("channelid:" + channelId);
        return mRootView;
    }

    private void setupRecycler() {
        adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>(), getFirebaseHelper().getFirebaseAuth().getCurrentUser().getDisplayName());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        //llm.setReverseLayout(true);
        //llm.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enteredText.getText().toString().length() > 0)
                    sendChat(enteredText.getText().toString());
                enteredText.setText("");
            }
        });


    }

    private void sendChat(String message) {
        ChatMessage chatMessage = new ChatMessage(getFirebaseHelper().getFirebaseAuth().getCurrentUser().getDisplayName(), message);
        if (TextUtils.isEmpty(channelId)) {
            DatabaseReference newUserChannelRef = getFirebaseHelper().getUserChannels().push();
            Channel newChannel = new Channel(newUserChannelRef.getKey(), channelName, message, null);
            newChannel.userId1 = getCurrentUser().getUid();
            newChannel.userId2 = userId;
            newUserChannelRef.setValue(newChannel);

            getFirebaseHelper().getChannelReference(newUserChannelRef.getKey()).push().setValue(chatMessage);
            channelId = newUserChannelRef.getKey();
            newChannel.name = getCurrentUser().getDisplayName();
            getFirebaseHelper().getUserChannels(userId).child(newUserChannelRef.getKey()).setValue(newChannel);
            syncWithFirebase();
            getSharedPref().edit().putString(userId, channelId).commit();

        } else {
            getFirebaseHelper().getChannelReference(channelId).push().setValue(chatMessage);
            Map<String, Object> map = new HashMap<>();
            map.put("lastMessage", message);
            map.put("timeStamp", System.currentTimeMillis());
            getFirebaseHelper().getUserChannels().child(channelId).updateChildren(map);
            getFirebaseHelper().getUserChannels(userId).child(channelId).updateChildren(map);
        }

    }

    private void syncWithFirebase() {
        log("Syncing with firebase");
        if (!TextUtils.isEmpty(channelId)) {
            log("adding listener");
            getFirebaseHelper().getChannelReference(channelId).addChildEventListener(this);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
        adapter.add(message);
        mRecyclerView.smoothScrollToPosition(adapter.getCount() - 1);
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
}
