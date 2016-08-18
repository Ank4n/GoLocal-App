package space.ankan.golocal.screens.mykitchen;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.ankan.golocal.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyKitchenActivityFragment extends Fragment {

    public MyKitchenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_kitchen, container, false);
    }
}
