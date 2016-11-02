package space.ankan.golocal.screens.setupkitchen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.AppConstants;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.screens.MainActivity;
import space.ankan.golocal.utils.CommonUtils;

import static android.app.Activity.RESULT_OK;
import static space.ankan.golocal.core.AppConstants.PERMISSION_REQUEST_CODE;
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
        return new SetupKitchenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setup_kitchen, container, false);
        ButterKnife.bind(this, rootView);
        setupListeners();
        return rootView;
    }

    private void setupListeners() {
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.hasPermission(SetupKitchenFragment.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(AppConstants.TAG, "request code: " + requestCode + " | " + permissions[0] + "was " + grantResults[0]);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(AppConstants.TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            CommonUtils.imagePickerForResult(SetupKitchenFragment.this);
        } else {
            Toast.makeText(getActivity(), "You need to allow write permissions to upload image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            if (selectedImageUri == null) {
                Toast.makeText(getActivity(), R.string.upload_image_error, Toast.LENGTH_LONG).show();
                return;
            }
            Picasso.with(getActivity()).load(selectedImageUri).into(kitchenImage);

            UploadTask task = getFirebaseHelper().pushProfileImage(CommonUtils.compressImage(getActivity(), selectedImageUri), getCurrentUser().getUid());
            if (task != null)
                task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        if (downloadUri != null)
                            imageUrl = taskSnapshot.getDownloadUrl().toString();
                    }
                });

        }
    }


    private boolean validateInformation() {
        if (TextUtils.isEmpty(editKitchenName.getText()) || TextUtils.isEmpty(editDescription.getText()) || TextUtils.isEmpty(editAddress.getText())) {
            Toast.makeText(getActivity(), R.string.error_fields_not_filled, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void publishKitchen() {
        Location location = ((MainActivity) getActivity()).getLocation();
        if (location == null) {
            Toast.makeText(getActivity(), R.string.location_error_message, Toast.LENGTH_LONG).show();
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
        Toast.makeText(getActivity(), R.string.kitchen_published_message, Toast.LENGTH_LONG).show();
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
