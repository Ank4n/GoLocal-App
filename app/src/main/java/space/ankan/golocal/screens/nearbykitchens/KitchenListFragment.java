package space.ankan.golocal.screens.nearbykitchens;

import android.support.v4.app.Fragment;
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
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * A placeholder fragment containing a simple view.
 */
public class KitchenListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private KitchenAdapter adapter;

    public KitchenListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_kitchen_list, container, false);
        setupRecycler();
        syncWithFirebase();
        return mRecyclerView;
    }

    private void setupRecycler() {
        adapter = new KitchenAdapter(getActivity(), new ArrayList<Kitchen>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    private void syncWithFirebase() {
        getFirebaseHelper().getKitchensReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kitchen kitchen = dataSnapshot.getValue(Kitchen.class);
                kitchen.key = dataSnapshot.getKey();
                adapter.add(kitchen);
                log("kitchen key: " + kitchen.key);
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
        });
    }

}
