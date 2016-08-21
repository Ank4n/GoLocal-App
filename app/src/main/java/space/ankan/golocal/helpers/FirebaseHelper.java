package space.ankan.golocal.helpers;

import android.net.Uri;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.model.users.User;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class FirebaseHelper {

    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private DatabaseReference usersRef;
    private DatabaseReference channelsRef;
    private DatabaseReference kitchensRef;
    private DatabaseReference geoFireRef;
    private GeoFire geoFire;

    private StorageReference profileImagesRef;
    private StorageReference chatImagesRef;

    public FirebaseHelper() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);

        usersRef = database.getReference("users");
        channelsRef = database.getReference("channels");
        kitchensRef = database.getReference("kitchens");
        geoFireRef = database.getReference("geoFire");
        geoFire = new GeoFire(geoFireRef);

        profileImagesRef = storage.getReference("profile");
        chatImagesRef = storage.getReference("chatPhotos");

    }

    public DatabaseReference getUsersReference() {
        return usersRef;
    }

    public DatabaseReference getChannelsReference() {
        return channelsRef;
    }

    public DatabaseReference getKitchensReference() {
        return kitchensRef;
    }

    public StorageReference getDishImagesReference() {
        return profileImagesRef;
    }

    public StorageReference getChatImagesReference() {
        return chatImagesRef;
    }

    public DatabaseReference getUserReference(String user) {
        return database.getReference("users/" + user);
    }

    public DatabaseReference getCurrentUserReference() {
        String user = auth.getCurrentUser().getUid();
        return database.getReference("users/" + user);
    }

    public DatabaseReference getChannelReference(String channel) {
        return database.getReference("channels/" + channel);
    }

    public DatabaseReference getKitchenReference(String kitchen) {
        return database.getReference("kitchens/" + kitchen);
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    public void enrollNewUser() {
        getCurrentUserReference().setValue(new User(null, auth.getCurrentUser().getDisplayName()));
    }

    public String getCurrentUserUid() {
        return auth.getCurrentUser().getUid();
    }

    public void push(Dish dish, String kitchenId) {
        getKitchenReference(kitchenId).child("dishes").push().setValue(dish);

    }

    public String push(Kitchen kitchen) {
        DatabaseReference kitchenRef = getKitchensReference().push();
        kitchenRef.setValue(kitchen);
        Map<String, Object> map = new HashMap<>();
        map.put("kitchen", kitchenRef.getKey());
        getCurrentUserReference().updateChildren(map);
        String key = kitchenRef.getKey();
        geoFire.setLocation(key, new GeoLocation(kitchen.latitude, kitchen.longitude));
        return key;
    }

    public GeoQuery buildQueryForKitchens(GeoLocation geo, double rangeInKms) {
        return geoFire.queryAtLocation(geo, rangeInKms);
    }

    public DatabaseReference getDishesReference(String kitchenId) {
        return getKitchenReference(kitchenId).child("dishes");
    }

    public DatabaseReference getUserChannels() {
        return getCurrentUserReference().child("channels");
    }

    public DatabaseReference getUserChannels(String userId) {
        return getUserReference(userId).child("channels");
    }

    public UploadTask pushImage(Uri imageUri) {
        // Get a reference to the location where we'll store our photos
        // Get a reference to store file at chat_photos/<FILENAME>
        final StorageReference photoRef = chatImagesRef.child(imageUri.getLastPathSegment());
        // Upload file to Firebase Storage
        return photoRef.putFile(imageUri);


    }

    public UploadTask pushProfileImage(Uri imageUri, String oldImageUrl) {
        // Get a reference to the location where we'll store our photos
        // Get a reference to store file at chat_photos/<FILENAME>
        final StorageReference photoRef = profileImagesRef.child(imageUri.getLastPathSegment());
        // Upload file to Firebase Storage
        return photoRef.putFile(imageUri);


    }
}
