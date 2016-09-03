package space.ankan.golocal.model;

/**
 * Created by ankan.
 * Review model, inside kitchen node which will consist list of all reviews for that kitchen
 */

public class Review {
    public String id;
    public String userName;
    public String userId;
    public String kitchenId;
    public int rating;
    public String reviewText;

    public Review() {
    }

    public Review(String id, String userName, String userId, String kitchenId, int rating, String reviewText) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.kitchenId = kitchenId;
        this.rating = rating;
        this.reviewText = reviewText;
    }
}

