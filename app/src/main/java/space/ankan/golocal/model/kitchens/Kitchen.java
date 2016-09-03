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
    //public List<Dish> dishes;
    //public List<Review> reviews;
    public String imageUrl;
    public int ratedUserCount;
    public float rating;
    public boolean isFavourite;
    public String address;

    public Kitchen() {
    }

    public Kitchen(String name, String description, String imageUrl, float rating, boolean isFavourite) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavourite = isFavourite;
    }

    public Kitchen(String name, String description, String imageUrl, String address) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.address = address;
    }
}
