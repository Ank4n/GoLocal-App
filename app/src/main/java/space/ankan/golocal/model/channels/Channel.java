package space.ankan.golocal.model.channels;

/**
 * Created by ankan.
 * TODO: Add a class comment
 */

public class Channel implements Comparable<Channel> {
    public String channelId;
    public String name;
    public String lastMessage;
    public long timeStamp;
    public String imageUrl;
    public String userId1;
    public String userId2;
    //public ChatMessage[] messages;


    public Channel() {
    }

    public Channel(String channelId, String name, String lastMessage, String imageUrl) {
        this.channelId = channelId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeStamp = System.currentTimeMillis();
        this.imageUrl = imageUrl;
    }

    @Override
    public int compareTo(Channel that) {
        return Long.compare(that.timeStamp, this.timeStamp);
    }
}
