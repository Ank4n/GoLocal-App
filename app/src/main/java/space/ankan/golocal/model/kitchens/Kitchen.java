package space.ankan.golocal.model.kitchens;

import com.firebase.geofire.GeoLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.model.kitchens.Dish;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class Kitchen implements Serializable {
    public String key;
    public String name;
    public String userId;
    public String description;
    public double latitude;
    public double longitude;
    public boolean isSelected; // for internal use only
    //public List<Dish> dishes;
    //public List<Review> reviews;
    public String imageUrl;

    public static final String RATED_USER_COUNT = "ratedUserCount";
    public int ratedUserCount;

    public static final String OVERALL_RATING = "overallRating";
    public double overallRating;
    public static final String IS_FAVOURITE = "isFavourite";
    public boolean isFavourite;
    public String address;

    public Kitchen() {
    }

    public Kitchen(String name, String description, String imageUrl, float rating, boolean isFavourite) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.overallRating = rating;
        this.isFavourite = isFavourite;
    }

    public Kitchen(String name, String description, String imageUrl, String address) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.address = address;
    }

    public Kitchen(String key, String name, String userId, String description, double latitude, double longitude, String imageUrl, int ratedUserCount, double overallRating, int isFavourite, String address) {
        this.key = key;
        this.name = name;
        this.userId = userId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.ratedUserCount = ratedUserCount;
        this.overallRating = overallRating;
        this.isFavourite = isFavourite >= 1;
        this.address = address;
    }
}
