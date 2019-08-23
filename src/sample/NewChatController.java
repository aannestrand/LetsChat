
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

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class NewChatController {

    @FXML
    private TextField groupNameTf;
    @FXML
    private Button createChatButton;
    @FXML
    private Button addUserButton;
    @FXML
    private ComboBox onlineUsersComboBox;
    @FXML
    private Stage mainStage;


    private ClientMain currentClient;
    private ArrayList<String> groupUsers;
    private boolean requestReceived;

    public void startUp(Stage mainStage) {
        try {
            this.mainStage = mainStage;
            requestReceived = false;
            currentClient.fetchOnlineUsers();
            while (!requestReceived) {System.out.println("Waiting on server");}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void notifyNewChatControllerOfRequest(OnlineUsersRequest request) {
        for (String s : request.onlineUsers) {
            if (!s.equals(currentClient.getUser().getUserName())) {
                onlineUsersComboBox.getItems().add(s);
            }
        }
        requestReceived = true;
    }

    public void setClient(ClientMain client) {
        this.currentClient = client;
    }

    @FXML
    public void addUser(ActionEvent event) {
        if (groupUsers == null) {
             groupUsers = new ArrayList<>();
             groupUsers.add(currentClient.getUser().getUserName());
             System.out.println(currentClient.getUser().getUserName() + " added");
        }

        if (onlineUsersComboBox.getValue() != null) {
            groupUsers.add((String) onlineUsersComboBox.getValue());
            System.out.println(onlineUsersComboBox.getValue()+ " added");
        }
    }

    @FXML
    public void createChat(ActionEvent event) throws IOException {
        if (groupUsers.size() >= 1) {
            ChatRoom room = new ChatRoom(groupUsers, groupNameTf.getText());
            Button chatBtn = new Button();
            chatBtn.setPrefHeight(40);
            chatBtn.setPrefWidth(140);
            chatBtn.setMinHeight(40);
            chatBtn.setMinWidth(140);
            chatBtn.setMaxHeight(40);
            chatBtn.setMaxWidth(140);
            chatBtn.setText(groupNameTf.getText() + " - " + room.getUniqueID());
            chatBtn.setStyle("-fx-background-color: transparent");

            chatBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    currentClient.getMessageController().mouseHoverButtons(event);
                }
            });

            chatBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    currentClient.getMessageController().mouseExitHoverButtons(event);
                }
            });

            chatBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentClient.setCurrentChatRoomSelected(room);
                    for (ChatroomController c : currentClient.getChatroomControllers()) {
                        if (c.getChatRoom().equals(room)) {
                            c.getRoot().setOpacity(1);
                            c.getRoot().toFront();
                        } else {
                            c.getRoot().setOpacity(0);
                        }
                    }
                }
            });

            this.currentClient.newChatRoom(room,chatBtn);
            this.currentClient.sendChatRoom(room);
            mainStage.close();
        }



    }



}
