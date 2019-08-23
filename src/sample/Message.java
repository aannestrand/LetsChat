package sample;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private String sender;
    private ChatRoom chatRoom;

    public Message (String message, String sender, ChatRoom chatRoom) {
        this.message = message;
        this.sender = sender;
        this.chatRoom = chatRoom;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void setChatRoom(ChatRoom room) { this.chatRoom = room; }
}
