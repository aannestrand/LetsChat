package sample;

import java.io.Serializable;

public class VerifyUserRequest implements Serializable {

    private User user;
    private boolean verified;

    public VerifyUserRequest(User u) {
        this.user = u;
        this.verified = false;
    }

    public User getUser() {
        return user;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
