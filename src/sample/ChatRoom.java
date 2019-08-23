package sample;

        import java.io.Serializable;
        import java.util.ArrayList;

public class ChatRoom implements Serializable {

    private ArrayList<String> chatUserIDs = new ArrayList<>();
    private String roomName;
    private int uniqueID;

    // since we don't want to add anyone else to the list, we only edit in the constructor
    public ChatRoom(ArrayList<String> chatUserIDs, String roomName){
        this.chatUserIDs = chatUserIDs;
        this.roomName = roomName;
        this.uniqueID = (int)(Math.random() * (32991)*(2347));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ChatRoom)) {
            return false;
        }

        ChatRoom room = (ChatRoom)o;

        return ( this.getUniqueID() == room.getUniqueID()  );
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<String> getChatUserIDs() {
        return chatUserIDs;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void addUser(String userName) {
        chatUserIDs.add(userName);
    }

}
