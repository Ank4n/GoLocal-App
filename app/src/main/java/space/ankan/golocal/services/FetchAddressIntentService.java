package space.ankan.golocal.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import space.ankan.golocal.core.AppConstants;

import static com.google.android.gms.wearable.DataMap.TAG;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.RESULT_DATA_KEY, message);
        if (mReceiver != null)
            mReceiver.send(resultCode, bundle);
    }

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                AppConstants.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(AppConstants.RECEIVER);
        Log.wtf(TAG, "Receiver" + mReceiver);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException | IllegalArgumentException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Could not fetch address";
            Log.e(TAG, errorMessage, ioException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No Address found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(AppConstants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "found address");
            deliverResultToReceiver(AppConstants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

}



