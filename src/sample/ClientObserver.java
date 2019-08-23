package sample;

        import com.sun.security.ntlm.Client;

        import java.io.IOException;
        import java.io.Serializable;
        import java.util.Observable;
        import java.util.Observer;

public class ClientObserver implements Observer, Serializable {

    private ClientHandler client;
    public String userName;
    public ChatRoomObserverable obsRoom;

    @Override
    public void update(Observable o, Object arg) {
        try {
            client.getOutput().writeObject(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientObserver (ClientHandler client, String userName, ChatRoomObserverable obsRoom) {
        this.client = client;
        this.userName = userName;
        this.obsRoom = obsRoom;
    }
}
