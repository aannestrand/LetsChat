package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class OnlineUsersRequest implements Serializable {

    public ArrayList<String> onlineUsers;

    public void setOnlineUsers (ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}
