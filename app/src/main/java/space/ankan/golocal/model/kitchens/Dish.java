package space.ankan.golocal.model.kitchens;

import java.io.Serializable;

/**
 * Created by ankan.
 */

public class Dish implements Serializable {
    public String key;
    public String name;
    public String description;
    public String imageUrl;
    public int price;
    public boolean nonVeg;
    public boolean isSelected; //for internal use only

    public Dish() {
    }

    public Dish(String key, String name, String description, String imageUrl, int price, boolean nonVeg) {
        this.key = key;
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
