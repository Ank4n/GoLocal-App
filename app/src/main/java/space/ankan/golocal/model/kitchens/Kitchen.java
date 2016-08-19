package space.ankan.golocal.model.kitchens;

import space.ankan.golocal.model.kitchens.Dish;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class Kitchen {
    public String name;
    public String description;
    public Dish[] dishes;
    public String imageUrl;
    public int ratedUserCount;
    public float rating;
    public boolean isFavourite;

    public Kitchen() {
    }

    public Kitchen(String name, String description, String imageUrl, float rating, boolean isFavourite) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavourite = isFavourite;
    }
}
