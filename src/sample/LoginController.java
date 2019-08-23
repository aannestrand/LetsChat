package sample;

import com.sun.security.ntlm.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private TextField tfUserName;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginBtn;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private PasswordField tfPassword;

    private ClientMain client;

    public void endProgram(ActionEvent event) throws IOException {
        client.endNetworking();
    }

    public void initManager(final LoginManager loginManager) {
        client = loginManager.getCurrentClient();

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                User u = new User(tfUserName.getText(), tfPassword.getText());
                tfPassword.clear(); tfUserName.clear();

                boolean validUser = false;
                try {
                    validUser = loginManager.authenicate(u);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (validUser) {
                    try {
                        loginManager.verified(u);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Bad Login");
                }

            }
        });
    }
}
