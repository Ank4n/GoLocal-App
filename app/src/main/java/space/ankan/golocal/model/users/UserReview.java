package space.ankan.golocal.model.users;

/**
 * Created by ankan.
 * Review model, inside Kitchen node which will consist list of all reviews for that kitchen
 */

public class UserReview {
    public String kitchenId;
    public float rating;

    @SuppressWarnings("unused")
    public UserReview() {
    }

    public UserReview(String kitchenId, float rating) {
        this.kitchenId = kitchenId;
        this.rating = rating;

    }
}

