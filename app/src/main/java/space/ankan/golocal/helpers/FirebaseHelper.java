package space.ankan.golocal.helpers;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import space.ankan.golocal.model.kitchens.Dish;
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

    private StorageReference dishImagesRef;
    private StorageReference chatImagesRef;

    public FirebaseHelper() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);

        usersRef = database.getReference("users");
        channelsRef = database.getReference("channels");
        kitchensRef = database.getReference("storage");

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
        getCurrentUserReference().setValue(new User(null, auth.getCurrentUser().getDisplayName(), null));
    }

    public String getCurrentUserUid() {
        return auth.getCurrentUser().getUid();
    }

    public void push(Dish dish, String kitchenId) {
        getKitchenReference(kitchenId).child("dishes").push().setValue(dish);

    }
}
