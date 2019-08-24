package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ChatroomController {

    @FXML
    private TextField inputTextField;
    @FXML
    private TextArea historyTextArea;
    @FXML
    private Parent root;
    @FXML
    private Label groupNameLabel;

    private ClientMain currentClient;
    private ChatRoom chatRoom;

    public void startUp(ClientMain currentClient, ChatRoom room) {
        this.currentClient = currentClient;
        this.chatRoom = room;
        groupNameLabel.setText(room.getRoomName());
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
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
    public void sentMessage(ActionEvent event) throws IOException {

        if (!inputTextField.getText().isEmpty() && currentClient.currentChatRoomSelected != null) {
            Message message = new Message(inputTextField.getText(), currentClient.getUser().getUserName(), null);
            inputTextField.clear();
            currentClient.sendMessage(message);
        }

    }

    @FXML
    public void updateChatRoom(ChatRoom chatRoom, Message message) {
        // use chatRoom for specific TA
        groupNameLabel.setText(chatRoom.getRoomName());
        historyTextArea.appendText(message.getSender() + ": " + message.getMessage()+"\n");
    }

    @FXML
    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }
}
