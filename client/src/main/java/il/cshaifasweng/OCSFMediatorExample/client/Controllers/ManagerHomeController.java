package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdQuestionAddedEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.extraTimeRequestEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;

public class ManagerHomeController {

    @FXML
    private Button allStudentsBN;

    @FXML
    private Button homeBN;

    @FXML
    private Label idLabel;

    @FXML
    private Label statusLB;

    private String id;

    public ManagerHomeController(){
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setId(String id){this.id = id;}


    @FXML
    @Subscribe
    public void onUserHomeEvent(UserHomeEvent event){
        setId(event.getUserID());
        initializeIfIdNotNull();
    }

    private void initializeIfIdNotNull() {
        if (id != null) {
            idLabel.setText("ID: " + id);
        }
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {

    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {

    }
    @Subscribe
    public void onExtraTimeRequestEvent(extraTimeRequestEvent event){
        Platform.runLater(() -> {
            int input = JOptionPane.showOptionDialog(null, "Extra time request: " + event.getMsg(), "Extra time request",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (input == JOptionPane.YES_OPTION) {
                try {
                    ScheduledTest myScheduledTest = event.getScheduledTest();
                    int x = myScheduledTest.get
                    myScheduledTest.setTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


/*    @Subscribe
    public void onExtraTimeRequestEvent(extraTimeRequestEvent event){
        try {
            cleanup();
            App.switchScreen("ManagerExtraTime");
            Platform.runLater(() -> {
                EventBus.getDefault().post(new extraTimeRequestEvent(event.getExtraMinutes(), event.getMsg(), event.getScheduledTest()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
