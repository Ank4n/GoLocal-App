package space.ankan.golocal.screens.setupkitchen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.screens.MainActivity;
import space.ankan.golocal.utils.CommonUtils;

import static android.app.Activity.RESULT_OK;
import static space.ankan.golocal.core.AppConstants.RC_PHOTO_PICKER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetupKitchenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetupKitchenFragment extends BaseFragment {


    @BindView(R.id.upload_kitchen_image)
    View uploadButton;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.kitchen_image)
    ImageView kitchenImage;

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
    private Uri selectedImageUri;

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
                CommonUtils.imagePickerForResult(SetupKitchenFragment.this);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();

            if (selectedImageUri == null) {
                Toast.makeText(getActivity(), "Could not upload image", Toast.LENGTH_SHORT).show();
                return;
            }
            Picasso.with(getActivity()).load(selectedImageUri).into(kitchenImage);

            getFirebaseHelper().pushProfileImage(selectedImageUri, getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            });

        }
    }


    private boolean validateInformation() {
        if (TextUtils.isEmpty(editKitchenName.getText()) || TextUtils.isEmpty(editDescription.getText()) || TextUtils.isEmpty(editAddress.getText())) {
            Toast.makeText(getActivity(), "Please fill all the boxes", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void publishKitchen() {
        Location location = ((MainActivity) getActivity()).getLocation();
        if (location == null) {
            Toast.makeText(getActivity(), R.string.location_error_message, Toast.LENGTH_LONG);
            return;
        }
        Kitchen kitchen = new Kitchen(editKitchenName.getText().toString(), editDescription.getText().toString(), imageUrl, editAddress.getText().toString());
        kitchen.userId = getCurrentUser().getUid();
        kitchen.latitude = location.getLatitude();
        kitchen.longitude = location.getLongitude();
        String kitchenId = getFirebaseHelper().push(kitchen);
        saveUserType(kitchenId);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
        startActivity(getActivity().getIntent());
        getActivity().finish();
        Toast.makeText(getActivity(), R.string.kitchen_published_message, Toast.LENGTH_LONG);
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
