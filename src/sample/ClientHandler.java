package sample;

import org.bson.Document;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientHandler implements Runnable, Serializable {

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket clientSocket;
    private String userName;
    private ArrayList<ClientObserver> handlerObservers = new ArrayList<>();

    public ClientHandler(Socket clientSocket, ObjectOutputStream toClient) throws IOException {
        input = new ObjectInputStream(clientSocket.getInputStream());
        this.clientSocket = clientSocket;
        this.output = toClient;
        ServerMain.rooms.get(0).addObserver(new ClientObserver(this,"",null));
        output.writeObject(new Message("Welcome to broadcast!","Server", ServerMain.rooms.get(0).getChatRoom()));
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void run() {

        while (true) {

            synchronized (this) {

                Date dateClientConnected = new Date();
                System.out.println(dateClientConnected);

                try {

                    Object message = input.readObject();

                    // if this is the first message, which tells the server who is connecting
                    if (message instanceof User) {
                        this.userName = ((User)message).getUserName();
                        ServerMain.onlineClients.put(((User) message).getUserName(), this);
                        System.out.println(((User) message).getUserName() + " successfully added to server.");
                    }

                    // if the client is requesting to make a new chatroom
                    else if (message instanceof ChatRoom) {
                        ArrayList<String> users = ((ChatRoom) message).getChatUserIDs();
                        ArrayList<ClientObserver> observers = new ArrayList<ClientObserver>();
                        ChatRoomObserverable obsRoom = new ChatRoomObserverable((ChatRoom)message);

                        for (String s1 : users) {
                            ClientObserver obs = new ClientObserver(ServerMain.onlineClients.get(s1),s1,obsRoom);
                            observers.add(obs);
                            if (obs.userName.equals(this.userName)) {
                                handlerObservers.add(obs);
                            }
                        }

                        obsRoom.setUpObservers(observers);
                        ServerMain.rooms.add(obsRoom);
                        System.out.println("New Chat Room Made: " + ((ChatRoom)message).getUniqueID());
                    }

                    // if it's an online users request
                    else if (message instanceof OnlineUsersRequest) {
                        ArrayList<String> onlineUsers = new ArrayList<>();
                        for (String s : ServerMain.onlineClients.keySet()) {
                            onlineUsers.add(s);
                        }

                        ((OnlineUsersRequest) message).setOnlineUsers(onlineUsers);
                        output.writeObject(message);
                    }

                    //if it's a user verification request
                    else if (message instanceof VerifyUserRequest) {

                        Document found = (Document) ServerMain.userCollection.find(new Document("userID",((VerifyUserRequest)message).getUser().getUserName())).first();
                        if (found != null) {
                            System.out.println("User found");

                            if (found.get("password").equals(((VerifyUserRequest) message).getUser().getPassword())) {
                                System.out.println("password correct");
                                ((VerifyUserRequest) message).setVerified(true);
                            }
                        }

                        output.writeObject(message);

                    }

                    // finally, if the client is sending a message...
                    else if (message instanceof Message) {

                        // grab the chatroom the message is intended for
                        for (ChatRoomObserverable r : ServerMain.rooms) {
                            if (r.getChatRoom().getUniqueID() == ((Message) message).getChatRoom().getUniqueID()) {
                                r.messageReceived();
                                r.notifyObservers(message);
                                System.out.println("Notifying observers of: " + ((Message) message).getMessage());
                            }
                        }
                    }

                    // new user request
                    else if (message instanceof NewUserRequest) {

                        Document document = new Document("userID",((NewUserRequest) message).getUser().getUserName());
                        document.append("password",((NewUserRequest) message).getUser().getPassword());

                        ServerMain.userCollection.insertOne(document);

                    }

                    else if (message instanceof LogoutRequest) {
                        for (ClientObserver c : handlerObservers) {
                            c.obsRoom.deleteObserver(c);
                        }

                    }

                } catch (IOException | ClassNotFoundException e) {
                    return;
                }
            }
        }
    }
}

