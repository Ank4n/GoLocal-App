package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.channels.Channel;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ChannelsFragment extends BaseFragment implements ChildEventListener {

    private View mRootView;
    private ChannelsAdapter adapter;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChannelsFragment() {
    }

    @SuppressWarnings("unused")
    public static ChannelsFragment newInstance(int columnCount) {
        ChannelsFragment fragment = new ChannelsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channels_list, container, false);
        setupRecycler();
        syncWithFirebase();
        return mRootView;
    }

    private void setupRecycler() {
        Context context = mRootView.getContext();
        mRecyclerView = (RecyclerView) mRootView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ChannelsAdapter(new ArrayList<Channel>(), getActivity());
        mRecyclerView.setAdapter(adapter);

    }

    private void syncWithFirebase() {
        getFirebaseHelper().getUserChannels().addChildEventListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        adapter.add(dataSnapshot.getValue(Channel.class));
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        adapter.modify(dataSnapshot.getValue(Channel.class), dataSnapshot.getKey());
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
