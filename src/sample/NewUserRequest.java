package sample;

public class NewUserRequest {

    User u;

    public NewUserRequest (User u) {
        this.u = u;
    }

    public User getUser() {
        return u;
    }
}
