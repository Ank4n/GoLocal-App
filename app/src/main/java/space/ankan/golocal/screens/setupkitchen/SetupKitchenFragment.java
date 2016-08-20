package space.ankan.golocal.screens.setupkitchen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.screens.onboarding.SplashActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetupKitchenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupKitchenFragment extends BaseFragment {


    @BindView(R.id.upload_kitchen_image)
    View uploadButton;

    @BindView(R.id.edit_kitchen_name)
    EditText editKitchenName;

    @BindView(R.id.edit_kitchen_description)
    EditText editDescription;

    @BindView(R.id.edit_kitchen_address)
    EditText editAddress;

    @BindView(R.id.setup_kitchen_action)
    Button completeActionButton;

    private View rootView;
    private String imageUrl;

    public SetupKitchenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SetupKitchenFragment.
     */
    public static SetupKitchenFragment newInstance() {
        SetupKitchenFragment fragment = new SetupKitchenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setup_kitchen, container, false);
        ButterKnife.bind(this, rootView);
        setupListeners();
        return rootView;
    }

    private void setupListeners() {
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUrl = uploadImage();
            }
        });

        completeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInformation())
                    publishKitchen();
            }
        });
    }


    private boolean validateInformation() {
        //FIXME Implement this
        return true;
    }

    private void publishKitchen() {
        Kitchen kitchen = new Kitchen(editKitchenName.getText().toString(), editDescription.getText().toString(), imageUrl, editAddress.getText().toString());
        kitchen.userId = getCurrentUser().getUid();
        String kitchenId = getFirebaseHelper().push(kitchen);
        saveUserType(kitchenId);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
        startActivity(getActivity().getIntent());
        getActivity().finish();
        showToast("Congratulations. Your kitchen has been published.");

    }

    private String uploadImage() {
        //FIXME Implement this
        showToast("Not implemented yet");
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
