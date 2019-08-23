
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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {
    public static HashMap<String, User> registeredUsers = new HashMap<>();
    public static HashMap<String, ClientHandler> onlineClients = new HashMap<>();
    public static List<ChatRoomObserverable> rooms = new ArrayList<ChatRoomObserverable>();

    public static void main(String[] args) {
        try {
            new ServerMain().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws Exception {
        addRegisteredUser();
        setUpNetwork();
    }

    public void addRegisteredUser() {
        registeredUsers.put("andrew1", new User("andrew1", "andrew1"));
        registeredUsers.put("andrew2", new User("andrew2", "andrew2"));
        registeredUsers.put("andrew3", new User("andrew3", "andrew3"));
        registeredUsers.put("andrew4", new User("andrew4", "andrew4"));

    }

    public void setUpNetwork() throws IOException {
        ServerSocket servSock = new ServerSocket(9999);
        System.out.println(InetAddress.getLocalHost());

        ChatRoomObserverable broadcast = new ChatRoomObserverable(new ChatRoom(null,"broadcast"));
        rooms.add(broadcast);

        while(true) {
            Socket clientSocket = servSock.accept();
            System.out.println("Client connected.");
            ObjectOutputStream toClient = new ObjectOutputStream(clientSocket.getOutputStream());


            Runnable handler = new ClientHandler(clientSocket, toClient);
            Thread t = new Thread(handler);
            t.start();
        }

    }


}
