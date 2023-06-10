package k.s.chattingapp.Model;

public class Message {
    private String message;
    private String timestamp;
    private String senderId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Message(String message, String timestamp, String senderId) {
        this.message = message;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public Message() {
    }
}
