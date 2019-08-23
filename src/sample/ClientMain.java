
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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientMain extends Application {

    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;
    private Socket socket;
    private User currentUser;
    public String IP;

    private Stage mainStage;
    private Parent messageRoot;
    private Parent loginRoot;
    private Parent connectRoot;

    private MessageController messageController;
    private LoginController loginController;
    private ConnectController connectController;

    private LoginManager loginManager;

    public ChatRoom currentChatRoomSelected;

    private ArrayList<ChatRoom> rooms = new ArrayList<ChatRoom>();
    private ArrayList<ChatroomController> chatroomControllers = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) throws Exception{

        this.mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("connect.fxml"));
        this.connectRoot = loader.load();
        this.connectController = loader.getController();
        this.connectController.startUp(this,connectRoot);

        loader = new FXMLLoader(getClass().getResource("message.fxml"));
        this.messageRoot = loader.load();
        this.messageController = loader.getController();

        loader = new FXMLLoader(getClass().getResource("login.fxml"));
        this.loginRoot = loader.load();
        this.loginController = loader.getController();

        LoginManager loginManager = new LoginManager(this);
        this.loginManager = loginManager;
        this.loginManager.displayConnect();

        this.mainStage.show();

    }

    class IncomingReader implements Runnable {
        public void run() {
            Object message;

            try {
                while ((message = fromServer.readObject()) != null) {

                    if (message instanceof VerifyUserRequest) {
                        notifyLoginManager((VerifyUserRequest) message);
                    } else if (message instanceof OnlineUsersRequest) {
                        notifyNewChat((OnlineUsersRequest) message);
                    } else {
                        System.out.println("Message Received: " + ((Message)message).getMessage() + " from " + ((Message) message).getSender() + " for " + ((Message) message).getChatRoom().getUniqueID());
                        Object finalMessage = message;
                        Platform.runLater(()-> {
                            try {
                                updateChatRoom(((Message) finalMessage).getChatRoom(), ((Message) finalMessage));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            });
                    }
                }

            } catch (IOException | ClassNotFoundException ex) {

            }

        }

    }

    public void sendMessage(Message m) throws IOException {
        m.setChatRoom(this.currentChatRoomSelected);
        System.out.println(m.getSender() + " is sending message: " + m.getMessage() + " to room - ");
        toServer.writeObject(m);
    }

    public void run() throws Exception {
        launch();
    }

    public void notifyLoginManager(VerifyUserRequest request) {
        this.loginManager.notifyManagerOfRequest(request);
    }
    public void notifyNewChat(OnlineUsersRequest request) {this.messageController.notifyChatController(request);}

    public static void main(String[] args) {
        try {
            ClientMain clientMain = new ClientMain();
            clientMain.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUser(User u) throws IOException {
        this.currentUser = u;
        toServer.writeObject(this.currentUser);
    }

    public User getUser() {
        return currentUser;
    }

    public void setUpNetworking() {

        try {
            socket = new Socket(IP, 9999);
            fromServer = new ObjectInputStream(socket.getInputStream());
            toServer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Networking established.");

            Thread readerThread = new Thread(new IncomingReader());
            readerThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endNetworking() throws IOException {
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("Logged out");
        System.exit(1);
    }

    public void updateChatRoom(ChatRoom chatRoom, Message message) throws IOException {
        // here we will update the textfield of the specific chatroom
        // then we will refresh the pane
        if (rooms.contains(chatRoom)) {
            for (ChatroomController c : chatroomControllers) {
                if (c.getChatRoom().equals(chatRoom)) {
                    c.updateChatRoom(chatRoom, message);
                }
            }
        } else {
            rooms.add(chatRoom);

            Button chatBtn = new Button();
            chatBtn.setPrefHeight(40);
            chatBtn.setPrefWidth(140);
            chatBtn.setMinHeight(40);
            chatBtn.setMinWidth(140);
            chatBtn.setMaxHeight(40);
            chatBtn.setMaxWidth(140);
            chatBtn.setTextAlignment(TextAlignment.CENTER);
            chatBtn.setText(chatRoom.getRoomName() + " - " + chatRoom.getUniqueID());
            chatBtn.setStyle("-fx-background-color: transparent");

            chatBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    messageController.mouseHoverButtons(event);
                }
            });

            chatBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    messageController.mouseExitHoverButtons(event);
                }
            });

            chatBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setCurrentChatRoomSelected(chatRoom);
                    for (ChatroomController c : chatroomControllers) {
                        if (c.getChatRoom().equals(chatRoom)) {
                            c.getRoot().setOpacity(1);
                            c.getRoot().toFront();
                        } else {
                            c.getRoot().setOpacity(0);
                        }
                    }
                }
            });

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    messageController.getChatRoomsVbox().getChildren().add(chatBtn);
                }
            });

            newChatRoom(chatRoom,chatBtn);

            for (ChatroomController c : chatroomControllers) {
                if (c.getChatRoom().equals(chatRoom)) {
                    c.updateChatRoom(chatRoom, message);
                }
            }
        }

    }

    public void setCurrentChatRoomSelected(ChatRoom room) {
        currentChatRoomSelected = room;
    }

    public void newChatRoom(ChatRoom room, Button chatBtn) throws IOException {

        // create new chatroom window, add it to the stackpane and put it to front
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatroom.fxml"));
        Parent root = loader.load();
        ChatroomController controller = loader.getController();
        controller.startUp(this, room);
        controller.setRoot(root);
        chatroomControllers.add(controller);
        messageController.getChatRoomsStackPane().getChildren().add(root);
        System.out.println(messageController.getChatRoomsStackPane().getChildren().toString());
        root.toFront();
        rooms.add(room);

        // add its buttom
        messageController.getChatRoomsVbox().getChildren().add(chatBtn);

    }

    public void fetchRegisteredUsers(User u) throws IOException {
        toServer.writeObject(new VerifyUserRequest(u));
    }

    public void sendChatRoom(ChatRoom room) throws IOException {
        toServer.writeObject(room);
    }

    public void sendLogoutRequest(LogoutRequest req) throws IOException {
        toServer.writeObject(req);
    }

    public void fetchOnlineUsers() throws IOException {
        toServer.writeObject(new OnlineUsersRequest());
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public Parent getMessageRoot() {
        return messageRoot;
    }

    public Parent getLoginRoot() {
        return loginRoot;
    }

    public Parent getConnectRoot() { return connectRoot; }

    public ArrayList<ChatroomController> getChatroomControllers() {
        return chatroomControllers;
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return rooms;
    }

    public LoginManager getLoginManager() { return this.loginManager; }


}


