package space.ankan.golocal.model.kitchens;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class Dish {
    public String name;
    public String description;
    public String imageUrl;
    public int price;
    public boolean nonVeg;

    public Dish() {
    }

    public Dish(String name, String description, String imageUrl, int price, boolean nonVeg) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.nonVeg = nonVeg;
    }

    public Dish(String name, int price, boolean nonVeg) {
        this.name = name;
        this.price = price;
        this.nonVeg = nonVeg;
    }
}
