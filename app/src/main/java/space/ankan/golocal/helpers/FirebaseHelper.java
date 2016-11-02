package space.ankan.golocal.helpers;

import android.net.Uri;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import space.ankan.golocal.core.AppConstants;
import space.ankan.golocal.model.kitchens.Dish;
import space.ankan.golocal.model.kitchens.Kitchen;
import space.ankan.golocal.model.notifications.Notification;
import space.ankan.golocal.model.users.User;
import space.ankan.golocal.model.users.UserReview;

/**
 * Created by ankan.
 * Helper class to help with firebase related operations
 */

public class FirebaseHelper implements AppConstants {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private static boolean isPersistent;

    private DatabaseReference usersRef;
    private DatabaseReference channelsRef;
    private DatabaseReference kitchensRef;
    private GeoFire geoFire;

    private StorageReference kitchenImagesRef;
    private StorageReference chatImagesRef;

    public FirebaseHelper() {
        if (!isPersistent) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isPersistent = true;
        }
        FirebaseApp app = FirebaseApp.getInstance();

        if (app != null) {
            database = FirebaseDatabase.getInstance(app);
            auth = FirebaseAuth.getInstance(app);
            storage = FirebaseStorage.getInstance(app);
        }

        usersRef = database.getReference(FIREBASE_DB_USERS);
        channelsRef = database.getReference(FIREBASE_DB_CHANNELS);
        kitchensRef = database.getReference(FIREBASE_DB_KITCHENS);
        DatabaseReference geoFireRef = database.getReference(FIREBASE_DB_GEOFIRE);
        geoFire = new GeoFire(geoFireRef);

        kitchenImagesRef = storage.getReference(FIREBASE_STORAGE_KITCHEN_ROOT);
        chatImagesRef = storage.getReference(FIREBASE_STORAGE_CHAT_IMAGES);


    }

    @SuppressWarnings("unused")
    private DatabaseReference getUsersReference() {
        return usersRef;
    }

    @SuppressWarnings("unused")
    private DatabaseReference getChannelsReference() {
        return channelsRef;
    }

    @SuppressWarnings("unused")
    private DatabaseReference getKitchensReference() {
        return kitchensRef;
    }

    @SuppressWarnings("unused")
    private StorageReference getDishImagesReference() {
        return kitchenImagesRef;
    }

    @SuppressWarnings("unused")
    private StorageReference getChatImagesReference() {
        return chatImagesRef;
    }

    private DatabaseReference getUserReference(String user) {
        return database.getReference(FIREBASE_DB_USERS + FOLDER_SEPARATOR + user);
    }

    public DatabaseReference getCurrentUserReference() {
        if (auth.getCurrentUser() == null) return null;
        String user = auth.getCurrentUser().getUid();
        return database.getReference(FIREBASE_DB_USERS + FOLDER_SEPARATOR + user);
    }

    public DatabaseReference getChannelReference(String channel) {
        return database.getReference(FIREBASE_DB_CHANNELS + FOLDER_SEPARATOR + channel);
    }

    public DatabaseReference getKitchenReference(String kitchen) {
        return database.getReference(FIREBASE_DB_KITCHENS + FOLDER_SEPARATOR + kitchen);
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    public void enrollNewUser() {

        if (auth.getCurrentUser() == null) {
            Log.e(AppConstants.TAG, "Error enrolling user, could not sign in");
            return;
        }

        getCurrentUserReference().setValue(new User(null, auth.getCurrentUser().getDisplayName()));
    }

    @SuppressWarnings("unused")
    private String getCurrentUserUid() {
        if (auth.getCurrentUser() == null) return null;
        return auth.getCurrentUser().getUid();
    }

    public void push(Dish dish, String kitchenId) {
        DatabaseReference dishRef;
        if (dish.key == null) {
            dishRef = getKitchenReference(kitchenId).child(FIREBASE_DB_KITCHENS_DISHES).push();
            dish.key = dishRef.getKey();
        } else
            dishRef = getKitchenReference(kitchenId).child(FIREBASE_DB_KITCHENS_DISHES).child(dish.key);

        dishRef.setValue(dish);

    }

    public String push(Kitchen kitchen) {
        DatabaseReference kitchenRef = getKitchensReference().push();
        kitchenRef.setValue(kitchen);
        Map<String, Object> map = new HashMap<>();
        map.put(User.KITCHEN, kitchenRef.getKey());
        getCurrentUserReference().updateChildren(map);
        String key = kitchenRef.getKey();
        geoFire.setLocation(key, new GeoLocation(kitchen.latitude, kitchen.longitude));
        return key;
    }

    public void updateFavourite(Kitchen kitchen) {
        Map<String, Object> map = new HashMap<>();
        map.put(Kitchen.IS_FAVOURITE, kitchen.isFavourite);
        getKitchenReference(kitchen.key).updateChildren(map);
    }

    public GeoQuery buildQueryForKitchens(GeoLocation geo, double rangeInKms) {
        return geoFire.queryAtLocation(geo, rangeInKms);
    }

    public DatabaseReference getDishesReference(String kitchenId) {
        return getKitchenReference(kitchenId).child(FIREBASE_DB_KITCHENS_DISHES);
    }

    public DatabaseReference getUserChannels() {
        return getCurrentUserReference().child(FIREBASE_DB_USERS_CHANNELS);
    }

    public DatabaseReference getUserChannels(String userId) {
        return getUserReference(userId).child(FIREBASE_DB_USERS_CHANNELS);
    }

    public UploadTask pushImage(Uri imageUri) {
        // Get a reference to the location where we'll store our photos
        // Get a reference to store file at chat_photos/<FILENAME>
        final StorageReference photoRef = chatImagesRef.child(imageUri.getLastPathSegment());
        // Upload file to Firebase Storage
        return photoRef.putFile(imageUri);


    }

    public UploadTask pushProfileImage(Uri imageUri, String userId) {
        // Get a reference to the location where we'll store our photos
        // Get a reference to store file at chat_photos/<FILENAME>
        if (imageUri == null) {
            Log.e(AppConstants.TAG, "Error saving image");
            return null;
        }
        Log.i(AppConstants.TAG, "Image URI " + imageUri);
        final StorageReference photoRef = kitchenImagesRef.child(userId);
        // Upload file to Firebase Storage
        return photoRef.putFile(imageUri);


    }

    // returns 0 if rating does not exist
    public DatabaseReference getUserRatingReferenceByKitchen(String kitchenId) {
        return getCurrentUserReference().child(FIREBASE_DB_USER_REVIEWS).child(kitchenId);
    }

    public DatabaseReference getCurrentUserNotificationsReference() {
        return getCurrentUserReference().child(FIREBASE_DB_USER_NOTIFICATIONS);
    }

    private DatabaseReference getUserNotificationsReference(String userId) {
        return getUserReference(userId).child(FIREBASE_DB_USER_NOTIFICATIONS);
    }

    // returns 0 if rating does not exist
    public double pushRating(String kitchenId, float newRating, float oldRating, int ratedUserCount, double overallRating, boolean alreadyRated) {
        UserReview review = new UserReview(kitchenId, newRating);
        getUserRatingReferenceByKitchen(kitchenId).setValue(review);
        if (!alreadyRated || ratedUserCount == 0) //bad fix
            ratedUserCount++;

        overallRating = (overallRating * ratedUserCount - oldRating + newRating) / ratedUserCount;

        Map<String, Object> map = new HashMap<>();
        map.put(Kitchen.OVERALL_RATING, overallRating);
        map.put(Kitchen.RATED_USER_COUNT, ratedUserCount);

        getKitchenReference(kitchenId).updateChildren(map);
        return overallRating;
    }

    public void subscribe() {
        //TODO Implement FCM and server for notifications
        //String userId = getCurrentUserUid();
        //if (userId == null) return;
        //messaging.subscribeToTopic(userId);
    }

    public void unsubscribe() {
        //Implement FCM and server for notifications
        //String userId = getCurrentUserUid();
        //if (userId == null) return;
        //messaging.unsubscribeFromTopic(userId);
    }

    public void postNotification(Notification notification, String receiverId) {
        DatabaseReference userRef = getUserNotificationsReference(receiverId).push();
        userRef.setValue(notification);
    }
}
