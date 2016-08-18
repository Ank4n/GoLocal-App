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
    public int ratedUserCount;
    public int totalRatingValue;

    public Kitchen() {
    }

    public Kitchen(String name, String description, Dish[] dishes) {
        this.name = name;
        this.description = description;
        this.dishes = dishes;
    }
}
