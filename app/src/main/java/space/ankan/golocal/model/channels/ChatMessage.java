package space.ankan.golocal.model.channels;

/**
 * Created by ankan.
 *
 */

public class ChatMessage {
    public String name;
    public String message;
    public long timeStamp;

    @SuppressWarnings("unused")
    public ChatMessage() {
    }

    public ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
        timeStamp = System.currentTimeMillis();
    }
}
