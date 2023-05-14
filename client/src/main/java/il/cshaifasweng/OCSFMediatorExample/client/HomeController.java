package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import il.cshaifasweng.OCSFMediatorExample.server.Student;

public class HomeController {

    @FXML
    private Button allStudentsBN;

    @FXML
    private Button homeBN;

    @FXML
    private Label statusLB;

    public void handieClicks(ActionEvent actionEvent) {
    }

    /*@FXML
    void handieClicks(ActionEvent event) {

    }*/

}
