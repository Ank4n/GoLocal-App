package space.ankan.golocal.screens.nearbykitchens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.model.kitchens.Kitchen;

/**
 * A placeholder fragment containing a simple view.
 */
public class KitchenListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private KitchenAdapter adapter;

    public KitchenListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_kitchen_list, container, false);
        setupRecycler();
        return mRecyclerView;
    }

    private void setupRecycler() {
        ArrayList<Kitchen> kitchens = new ArrayList<>();
        kitchens.add(new Kitchen("Arti", "desc", "http://www.ikea.com/ms/media/cho_room/20153/kitchen/20153_cosk30a/20153_cosk30a_01_thumb_PH124155.jpg", 3.5f, true));
        kitchens.add(new Kitchen("Mukesh", "NorthIndian", "http://www.ikea.com/ms/media/cho_room/20161/kitchen/20161_cosk01a/20161_cosk01a_01_thumb_PH128782.jpg", 3.2f, false));

        adapter = new KitchenAdapter(getActivity(), kitchens);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }
}
