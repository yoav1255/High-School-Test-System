package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.server.Events.loginEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.ArrayList;

public class loginController {

    @FXML
    private Label error_msg;

    //@FXML
    //private TextArea error_msgg;

    @FXML
    private Button loginBN;

    @FXML
    private TextField user_id;

    @FXML
    private PasswordField user_password;

    public loginController(){
        EventBus.getDefault().register(this);
    }
//    public void cleanup() {
//        EventBus.getDefault().unregister(this);
//    }

    private String user_type;
    private void setUserType(String user_type){this.user_type = user_type;}


    @FXML void initialize(){
        error_msg.setVisible(false);
    }
    @Subscribe
    @FXML
    public void onloginEvent(loginEvent event) throws IOException {
        //error_msg.setText("ARRRIVE");
        setUserType(event.getUserType());
        switch (user_type) {
            case ("wrong"):
                //System.out.println(user_type);
                error_msg.setVisible(true);
                //error_msg.setStyle("-fx-text-fill: red; -fx-text-alignment: center;");

                break;
            case ("student"):
                SimpleClient.getClient().sendToServer(new CustomMessage("#studentHome", user_id.getText()));
                App.switchScreen("studentHome");
//                cleanup();
                break;
            case ("teacher"):
                SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", user_id.getText()));
                App.switchScreen("teacherHome");
                break;
            case ("manager"):
                SimpleClient.getClient().sendToServer(new CustomMessage("#managerHome", user_id.getText()));
                App.switchScreen("managerHome");
                break;
        }
    }




    @FXML
    void loginButton(ActionEvent event) throws IOException {
        String id = user_id.getText();
        String pass = user_password.getText();
        ArrayList<String> loginData = new ArrayList<>();
        loginData.add(0,id);
        loginData.add(1,pass);
        try{
            SimpleClient.getClient().sendToServer(new CustomMessage("#login", loginData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
