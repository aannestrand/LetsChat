package sample;


import java.util.ArrayList;
import java.util.Observable;

public class ChatRoomObserverable extends Observable {

    private ChatRoom chatRoom;
    private ArrayList<ClientObserver> chatUsers = new ArrayList<ClientObserver>();

    public ChatRoomObserverable (ChatRoom room) {
        this.chatRoom = room;
    }

    public void setUpObservers(ArrayList<ClientObserver> observers) {
        this.chatUsers = observers;

        for (ClientObserver c : chatUsers) {
            this.addObserver(c);
        }

    }

    public void messageReceived() {
        this.setChanged();
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }
}
