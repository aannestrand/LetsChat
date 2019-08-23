
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

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ConnectController {

    @FXML
    private Button connectBtn;
    @FXML
    private TextField ipAddressTf;

    private ClientMain client;
    private Parent root;
    private Stage mainStage;

    public void startUp(ClientMain client, Parent root) {
        this.client = client;
        this.root = root;
    }

    @FXML
    public void setIPAddress() {
       client.setIP(ipAddressTf.getText());
       client.setUpNetworking();
       client.getLoginManager().displayLogin();
    }


}
