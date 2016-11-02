package space.ankan.golocal.model.notifications;

/**
 * Created by ankan.
 */

public class Notification {
    public String senderId; // the user who posted the notification
    public String title;
    public String message;
    public int type;
    public int count;
    public String imageUrl;

    public String channelName;
    public String channelId;

    public Notification() {
    }

    public Notification(String senderId, String title, String message, int type, String channelName, String channelId) {
        this.senderId = senderId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.count = 1;
        this.channelId = channelId;
        this.channelName = channelName;
    }
}
