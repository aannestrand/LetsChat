
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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MessageController {

    public static int phraseNumber = 0;
    @FXML
    private Button homeBtn;
    @FXML
    private Button messagesBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private SplitPane messagesSplitPane;
    @FXML
    private BorderPane homeBorderPane;
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox chatRoomsVbox;
    @FXML
    private StackPane chatRoomsStackPane;

    private ClientMain currentClient;
    private NewChatController chatController;

    /**
     * This method will change the scene back to login menu
     */
    public void changeScreenToMessages(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene loginScene = new Scene(loginParent, 600, 700);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    public void mouseHoverButtons(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-color: #57bd72");
    }

    @FXML
    public void mouseExitHoverButtons(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: transparent");
    }

    @FXML
    public void selectedMouseButtons(ActionEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-color: #57bd72");
    }

    @FXML
    public void homeButtonPressed(ActionEvent event) throws IOException {
        // so we want to put messagePane to the top of the stackpane
        messagesSplitPane.setOpacity(0);
        homeBorderPane.setOpacity(1);
        homeBorderPane.toFront();
        welcomeLabel.setText("Hey, "+currentClient.getUser().getUserName() + "...");
    }

    @FXML
    public void messagesButtonPressed(ActionEvent event) throws IOException {
        homeBorderPane.setOpacity(0);
        messagesSplitPane.setOpacity(1);
        messagesSplitPane.toFront();
    }

    @FXML
    public void makeNewChatRoom(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        Parent root = loader.load();
        this.chatController = loader.getController();
        this.chatController.setClient(this.currentClient);

        Stage newChatStage = new Stage();
        newChatStage.setScene(new Scene(root));
        this.chatController.startUp(newChatStage);
        newChatStage.show();
    }

    public void notifyChatController(OnlineUsersRequest request) {
        this.chatController.notifyNewChatControllerOfRequest(request);
    }


    public void initSession(final LoginManager loginManager, ClientMain client) {
        this.currentClient = client;
        welcomeLabel.setText("Hey, "+client.getUser().getUserName() + "...");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (chatRoomsVbox.getChildren().size() > 1) {
                        chatRoomsVbox.getChildren().remove(2,chatRoomsVbox.getChildren().size());
                    }

                    getChatRoomsStackPane().getChildren().clear();
                    currentClient.getChatroomControllers().clear();
                    currentClient.getChatRooms().clear();
                    currentClient.setCurrentChatRoomSelected(null);
                    loginManager.logout();
                    //remove client from all chatrooms
                    LogoutRequest req = new LogoutRequest(currentClient.getUser().getUserName());
                    currentClient.sendLogoutRequest(req);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public VBox getChatRoomsVbox() {
        return chatRoomsVbox;
    }
    public StackPane getChatRoomsStackPane() { return chatRoomsStackPane; }
}
