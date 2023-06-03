package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveManagerIdEvent;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdQuestionAddedEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.application.Platform;
import il.cshaifasweng.OCSFMediatorExample.server.Events.extraTimeRequestEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;

import java.io.IOException;

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
            Platform.runLater(()->{
                idLabel.setText("ID: " + id);
            });

        }
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {

    }

@FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) throws IOException {

    }
@FXML
    public void goToQuestions(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showAllQuestions");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveManagerIdEvent(id));
        });
    }
@FXML
    public void goToExamForms(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showExamForms");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveManagerIdEvent(id));
        });
    }
@FXML
    public void goToScheduledTests(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showScheduleTest");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveManagerIdEvent(id));
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
@FXML
    public void goToStatistics(ActionEvent event) {
    }
    @Subscribe
    public void onMoveManagerIdEvent(MoveManagerIdEvent event){
        id = event.getId();
        initializeIfIdNotNull();
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
