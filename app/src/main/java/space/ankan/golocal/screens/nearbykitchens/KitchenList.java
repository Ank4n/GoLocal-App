package space.ankan.golocal.screens.nearbykitchens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.ankan.golocal.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class KitchenList extends Fragment {

    public KitchenList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kitchen_list, container, false);
    }
}
