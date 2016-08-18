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
        kitchens.add(new Kitchen("Arti", "desc", null));
        kitchens.add(new Kitchen("Arti", "desc", null));

        adapter = new KitchenAdapter(getActivity(), kitchens);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }
}
