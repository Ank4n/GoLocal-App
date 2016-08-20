package space.ankan.golocal.helpers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private StorageReference dishImagesRef;
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

        dishImagesRef = storage.getReference("dishes");
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
        return dishImagesRef;
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

}
