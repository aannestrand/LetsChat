package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable{

    private String userName;
    private String password;
    private ArrayList<User> friendsList;
    private String email;
    //private HashMap<Chat,ArrayList<Message>> messageHistory;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addFriend(User friend) {
        friendsList.add(friend);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() { return password; }


}
