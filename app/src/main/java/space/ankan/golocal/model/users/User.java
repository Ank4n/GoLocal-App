package space.ankan.golocal.model.users;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class User {

    public static final String KITCHEN = "kitchen";
    public String kitchen;

    public static final String USERNAME = "username";
    public String userName;
    //public UserChannel[] userChannels;
    //kitchenids

    public User(String kitchen, String userName) {
        this.kitchen = kitchen;
        this.userName = userName;
        //  this.userChannels = userChannels;
    }

    public User() {
    }
}
