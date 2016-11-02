package space.ankan.golocal.model.channels;

import android.support.annotation.NonNull;

/**
 * Created by ankan.
 */

public class Channel implements Comparable<Channel> {
    public String channelId;
    public String name;
    public String lastMessage;
    public long timeStamp;
    public String imageUrl;
    public String userId1;
    public String userId2;
    public boolean isSelected;
    //public ChatMessage[] messages;

    @SuppressWarnings("unused")
    public Channel() {
    }

    public Channel(String channelId, String name, String lastMessage, String imageUrl) {
        this.channelId = channelId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeStamp = System.currentTimeMillis();
        this.imageUrl = imageUrl;
    }

    @SuppressWarnings("unused")
    public String other(String userId) {
        if (userId.equals(userId1)) return userId2;
        return userId1;
    }

    @Override
    public int compareTo(@NonNull Channel that) {
        return Long.compare(that.timeStamp, this.timeStamp);
    }
}
