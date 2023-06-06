package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import javafx.application.Platform;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        cleanup();
        App.switchScreen("allStudents");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveManagerIdEvent(id));
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents",""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
    public void goToStatistics(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showStatistics");
    }
    @Subscribe
    public void onExtraTimeRequestEvent(extraTimeRequestEvent event){
        Platform.runLater(() -> {
            List<Object> data = new ArrayList<>();
            data = (List<Object>) event.getData();
            String explanation = (String) data.get(0);
            int extraMinutes = (int) data.get(1);
            String teacherName = (String) data.get(2);
            String subCourse = (String) data.get(3);
            ScheduledTest myScheduledTest = (ScheduledTest) data.get(4);

            int input = JOptionPane.showOptionDialog(null, "The teacher " +teacherName + " has requested " + extraMinutes + " extra minutes to an exam in "
                    + subCourse  + "from the reason: " + explanation + ". Select if you want to approve this request.", "Extra time request",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            List<Object> newData = new ArrayList<>();
            newData.add(myScheduledTest);
            if (input == JOptionPane.YES_OPTION) {
                try {
                    int x = myScheduledTest.getTimeLimit();
                    myScheduledTest.setTimeLimit(x+extraMinutes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Platform.runLater(() -> {
                        try{
                            newData.add(1, true);
                        SimpleClient.getClient().sendToServer(new CustomMessage("#extraTimeResponse", newData));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Platform.runLater(() -> {
                        try{
                            newData.add(1, false);
                        SimpleClient.getClient().sendToServer(new CustomMessage("#extraTimeResponse", newData));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


