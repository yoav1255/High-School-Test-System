package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.io.IOException;
import java.util.Set;

public class loginController {

    @FXML
    private Label error_msg;

    @FXML
    private Button loginBN;

    @FXML
    private TextField user_id;

    @FXML
    private PasswordField user_password;

    public loginController(){
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void loginButton(ActionEvent event) throws IOException {
        String id = user_id.getText();
        String pass = user_password.getText();
        Triplet<String, String, String> info = new Triplet<>("login_info", id, pass);
        SimpleClient.getClient().sendToServer(info);
    }

}
