package sample;

import java.io.Serializable;

public class LogoutRequest implements Serializable {

    public String userName;

    public LogoutRequest(String userName) {
        this.userName = userName;
    }
}
