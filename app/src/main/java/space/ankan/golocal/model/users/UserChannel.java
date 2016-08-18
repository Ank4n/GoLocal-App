package space.ankan.golocal.model.users;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class UserChannel {
    public String channelName;
    public String lastMessage;

    public UserChannel() {
    }

    public UserChannel(String channelName, String lastMessage) {
        this.channelName = channelName;
        this.lastMessage = lastMessage;
    }
}
