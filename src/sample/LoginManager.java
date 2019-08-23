
/* CHAT ROOM <MyClass.java> *
EE422C Project 7 submission by *
Replace <...> with your actual data. *
Andrew Annestrand
ata758
 Slip days used: 1 * Summer 2019
 */
/*   Describe here known bugs or issues in this file. If your issue spans multiple
   files, or you are not sure about details, add comments to the README.txt file.
*/


package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginManager {

    private ClientMain currentClient;
    private Scene messagesScene;
    private Scene loginScene;
    private Scene connectScene;
    private boolean requestReceived;
    private boolean requestVerification;

    public LoginManager(ClientMain client) {
        this.currentClient = client;
        this.connectScene = new Scene(currentClient.getConnectRoot());
        this.messagesScene = new Scene(currentClient.getLoginRoot(), 600,700);
        this.loginScene = new Scene(currentClient.getMessageRoot(),850,720);
    }

    //displays connect screen
    public void displayConnect() {
        currentClient.getMainStage().setScene(connectScene);
        currentClient.getMainStage().setResizable(false);
    }

    // displays the login screen
    public void displayLogin() {
        currentClient.getMainStage().setScene(messagesScene);
        currentClient.getMainStage().setResizable(false);
        currentClient.getLoginController().initManager(this);

    }

    // displays the main screen
    public void displayMain(User u) {
        currentClient.getMainStage().setScene(loginScene);
        currentClient.getMainStage().setResizable(false);
        currentClient.getMessageController().initSession(this, currentClient);
    }

    // this method gets user data, and searches a database
    // to verify credentials
    public boolean authenicate(User u) throws IOException {
        requestReceived = false;
        currentClient.fetchRegisteredUsers(u);

        int wait = 0;
        while (!requestReceived) {System.out.println("Weird");}
        return requestVerification;
    }

    // this is called by the client to notify the login manager that
    // a feedback request was received from the server
    public void notifyManagerOfRequest(VerifyUserRequest request) {
        requestVerification = request.isVerified();
        requestReceived = true;
    }

    // if the credentials are verified, switch to main screen
    public void verified(User u) throws IOException {
        this.currentClient.setUser(u);
        displayMain(u);
    }

    // go back to login page
    public void logout() throws IOException {
//        this.currentClient.endNetworking();
        displayLogin();
    }

    public ClientMain getCurrentClient () {
        return currentClient;
    }
}
