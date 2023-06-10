package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ExtraTime;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public void onTimeLeftEvent(TimeLeftEvent event){
        System.out.println("onTimeLeftEvent");
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getExtraTimeRequests",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onExtraTimeRequestEvent(extraTimeRequestEvent event){
        System.out.println("onExtraTimeRequestEvent");
        List<ExtraTime> extraTimeRequestEventList = event.getData();
        Platform.runLater(() -> {
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#clearExtraTimeRequests",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (!extraTimeRequestEventList.isEmpty()){
            for (ExtraTime extraTime : extraTimeRequestEventList) {
                //if (isScheduledTestActive(extraTime.getScheduledTest())) {
                    handleExtraTimeRequest(extraTime);
              //  }
            }
        }
    }

    public boolean isScheduledTestActive(ScheduledTest scheduledTest) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Get the start date and time of the scheduled test
        LocalDate testStartDate = scheduledTest.getDate();
        LocalTime testStartTime = scheduledTest.getTime();

        // Calculate the end date and time of the scheduled test
        LocalDateTime testStartDateTime = LocalDateTime.of(testStartDate, testStartTime);
        LocalDateTime testEndDateTime = testStartDateTime.plusMinutes(scheduledTest.getTimeLimit());

        // Check if the current date and time is within the scheduled test's time range
        return (currentDate.isEqual(testStartDate) && currentTime.isAfter(testStartTime)) ||
                (currentDate.isAfter(testStartDate) && currentDate.isBefore(testEndDateTime.toLocalDate())) ||
                (currentDate.isEqual(testEndDateTime.toLocalDate()) && currentTime.isBefore(testEndDateTime.toLocalTime()));
    }


    public void handleExtraTimeRequest(ExtraTime extraTime){
        Platform.runLater(() -> {
            String explanation = extraTime.getExplanation();
            int extraMinutes = extraTime.getExtraTime();
            String teacherName = extraTime.getTeacherName();
            String subCourse = extraTime.getSubCourse();
            ScheduledTest myScheduledTest = extraTime.getScheduledTest();

            int input = JOptionPane.showOptionDialog(null, "The teacher " +teacherName + " has requested " + extraMinutes + " extra minutes to an exam in "
                    + subCourse  + " from the reason: " + '"'+ explanation + '"' + ". Select if you want to approve this request.", "Extra time request",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            List<Object> data = new ArrayList<>();
            data.add(myScheduledTest);
            if (input == JOptionPane.YES_OPTION) {
                try {
                    int x = myScheduledTest.getTimeLimit();
                    myScheduledTest.setTimeLimit(x+extraMinutes);
                    Platform.runLater(()->{
                        try {
                            SimpleClient.getClient().sendToServer(new CustomMessage("#updateScheduleTest", myScheduledTest));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Platform.runLater(() -> {
                        try{
                            data.add(1, true);
                        SimpleClient.getClient().sendToServer(new CustomMessage("#extraTimeResponse", data));
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
                            data.add(1, false);
                        SimpleClient.getClient().sendToServer(new CustomMessage("#extraTimeResponse", data));
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


