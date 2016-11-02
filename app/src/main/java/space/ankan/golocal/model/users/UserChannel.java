package space.ankan.golocal.model.users;

/**
 * Created by ankan.
 */

public class UserChannel {
    public String channelName;
    public String lastMessage;

    @SuppressWarnings("unused")
    public UserChannel() {
    }
    @SuppressWarnings("unused")
    public UserChannel(String channelName, String lastMessage) {
        this.channelName = channelName;
        this.lastMessage = lastMessage;
    }
}
