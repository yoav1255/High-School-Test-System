package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.DisconnectClientEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class StartController {
    private static SimpleClient client = null;

    @FXML
    private TextField host_text;

    @FXML
    private Label msg;

    @FXML
    private TextField port_text;

    public StartController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup(){
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void goToLogin(ActionEvent event) {
        try {
            System.out.println("register successfully with client");
            client = SimpleClient.getClient(host_text.getText(),Integer.parseInt(port_text.getText()));
            client = SimpleClient.getClient();
            client.openConnection();
            System.out.println(client.getHost()+ " is the host " + client.getPort() + " is the port ");
            App.switchScreen("login");
            cleanup();
        }catch (Exception e){
            Platform.runLater(()->{
                msg.setVisible(true);
                msg.setText("Connection Failed");
            });
        }
    }

    @Subscribe
    public void onDisconnectClientEvent(DisconnectClientEvent event) throws IOException {
        System.out.println("on disconnect ");
        if(event.getClient()==client) {
            client.closeConnection();
            System.out.println("disconnect this client");
        }
    }
}
