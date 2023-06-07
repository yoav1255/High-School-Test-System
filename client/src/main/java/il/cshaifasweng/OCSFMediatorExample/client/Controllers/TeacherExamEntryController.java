package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowScheduleTestEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.TeacherExecuteExamEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.TimeLeftEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeacherExamEntryController {
    private ScheduledTest myScheduledTest;
    @FXML
    TextField textEntered;
    @FXML
    Label textDisplay;
    private String id;
    private List<String> scheduleTestIds;
    private List<ScheduledTest> scheduledTests;
    public TeacherExamEntryController() {
        EventBus.getDefault().register(this);
        scheduleTestIds = new ArrayList<>();
        scheduledTests = new ArrayList<>();
        ScheduledTest myScheduledTest;

        Platform.runLater(() -> {
            try{
                SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    void initialize() {
        textDisplay.setVisible(false);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setMyScheduledTest(ScheduledTest MyScheduledTest){
        this.myScheduledTest = MyScheduledTest;
    }
    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) {
        setId(event.getId());
    }
    @Subscribe
    public void onShowScheduleTestEvent(ShowScheduleTestEvent event){ // initialize scheduledTests and  scheduleTestIds Lists
        scheduledTests.clear();
        scheduleTestIds.clear();
        scheduledTests = event.getScheduledTestList();
        for (ScheduledTest test : scheduledTests) {
            String testId = test.getId();
            scheduleTestIds.add(testId);
        }
    }
    public void handleEnterBN(){
        String codeInput = textEntered.getText();   // User's input
        if((!scheduleTestIds.contains(codeInput))){
            // if the testId doesn't exist
            Platform.runLater(()->{
                textDisplay.setText("Exam ID doesn't exist! Try again");
                textDisplay.setVisible(true);
                textEntered.clear();
            });
        }
        else { // valid schedule test id
            for (ScheduledTest test : scheduledTests) {
                if (test.getId().equals(codeInput)) {
                    setMyScheduledTest(test);
                } // MyScheduledTest contains the ScheduledTest we want
            }
            if (myScheduledTest.getStatus() == 1){ // test is in progress
                cleanup();
                try {
                    System.out.println("hiiiiiiiiiiiiiiiiiiiiiii");
                    App.switchScreen("teacherExecuteExam");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                        EventBus.getDefault().post(new TeacherExecuteExamEvent(myScheduledTest));
                        EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                });
            } else if (myScheduledTest.getStatus() == 0) {
                Platform.runLater(() -> {
                    textDisplay.setText("the test will be available in: " + myScheduledTest.getDate() + " at " + myScheduledTest.getTime());
                    textDisplay.setVisible(true);
                });
            }
            else { //myScheduledTest.getStatus() == 2
                textDisplay.setText("the test is not available anymore. the test took place in: " + myScheduledTest.getDate() + " at " + myScheduledTest.getTime());
                textDisplay.setVisible(true);
            }
        }
    }
    @FXML
    public void handleHomeButtonClick(){
        try {
            String teacherId = this.id;
            cleanup();
            App.switchScreen("teacherHome");
            Platform.runLater(() -> {
                EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onTimeLeftEvent(TimeLeftEvent event){
        Platform.runLater(() -> {
            try{
                SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}