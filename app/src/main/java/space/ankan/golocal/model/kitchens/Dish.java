package space.ankan.golocal.model.kitchens;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class Dish {
    public String name;
    public String description;
    public String imageUrl;

    public Dish() {
    }

    public Dish(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
