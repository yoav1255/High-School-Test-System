package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.CheckFirstEntryEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveObjectToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowScheduleTestEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamEntryController {
    @FXML
    private Label msg;

    @FXML
    private TextField text_id;

    @FXML
    private TextField text_testCode;
    private String id;
    private List<String> scheduleTestIds;
    private List<ScheduledTest> scheduledTests;
    boolean isFirstEntry = true;

    public ExamEntryController() {
        EventBus.getDefault().register(this);
        scheduleTestIds = new ArrayList<>();
        Platform.runLater(()->{
            msg.setVisible(false);
        });
    }
    public void cleanup(){
        EventBus.getDefault().unregister(this);
    }

@Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        id = event.getId();
    }

    @FXML
    void initialize(){
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            info.add(id);
            info.add("student");
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        });
    }


@Subscribe
    public synchronized void onShowScheduleTestEvent(ShowScheduleTestEvent event) throws IOException {
        scheduleTestIds.clear();
        List<Object> studentId_scheduleTestId = new ArrayList<>();
        studentId_scheduleTestId.add(id);
        studentId_scheduleTestId.add(text_testCode.getText());
        scheduledTests = event.getScheduledTestList();
        for(ScheduledTest scheduledTest:scheduledTests){
            scheduleTestIds.add(scheduledTest.getId());
        }
        SimpleClient.getClient().sendToServer(new CustomMessage("#checkStudentTest",studentId_scheduleTestId));
}

@Subscribe
    public synchronized void onCheckFirstEntryEvent(CheckFirstEntryEvent event){
        System.out.println("is first changed to : " + event.isFirst());
        isFirstEntry = event.isFirst();
        Platform.runLater(()->{
            if(isFirstEntry) {
                System.out.println("accessing entry with " + isFirstEntry);
                enterTest();
            }
            else{
                System.out.println("Denying entry with "+isFirstEntry);
                Platform.runLater(()->{
                    msg.setVisible(true);
                    msg.setText("Already submitted this test!");
                });
            }
        });
    }

@FXML
    public void EnterTest_btn(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
    }
    public void enterTest(){
        String idInput = text_id.getText();
        String codeInput = text_testCode.getText();
        if(!idInput.equals(id) || (!scheduleTestIds.contains(codeInput))){
            //if the id doesn't match with the user id
            // or the testId doesn't exist
            Platform.runLater(()->{
                msg.setVisible(true);
                msg.setText("You dont have access!");
            });
            //TODO add more validation checks
        }
        else {
            int index = scheduleTestIds.indexOf(codeInput);
            ScheduledTest scheduledTest = scheduledTests.get(index);
            int status = scheduledTest.getStatus();
            if (status == 0) { // test is yet to start
                Platform.runLater(() -> {
                    msg.setVisible(true);
                    msg.setText("the test will be available at : " + scheduledTest.getDate() + " in " + scheduledTest.getTime());
                });
            } else if (status == 2) { // test time has passed
                Platform.runLater(()->{
                    msg.setVisible(true);
                    msg.setText("the test is not available anymore");
                });

            } else { // test is available
                try {
                    cleanup();
                    if(scheduledTest.getIsComputerTest()){ App.switchScreen("studentExecuteExam"); }
                    else{ App.switchScreen("studentExecuteExamLOCAL"); }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getStudent", id));
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getScheduleTestWithInfo", codeInput));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }


    @FXML
    public void goBackButton() throws IOException {
        cleanup();
        App.switchScreen("studentHome");
        Platform.runLater(() -> {
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#studentHome", id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
