package space.ankan.golocal.model.users;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class User {
    public String kitchen;
    public String userName;
    public UserChannel[] userChannels;

    public User(String kitchen, String userName, UserChannel[] userChannels) {
        this.kitchen = kitchen;
        this.userName = userName;
        this.userChannels = userChannels;
    }

    public  User() {
    }
}
