package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowScheduleTestEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    public ExamEntryController() {
        EventBus.getDefault().register(this);
        System.out.println("in exam entry controller");
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
        Platform.runLater(()->{
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
@Subscribe
    public void onShowScheduleTestEvent(ShowScheduleTestEvent event){
        List<ScheduledTest> scheduledTests = event.getScheduledTestList();
        for(ScheduledTest scheduledTest:scheduledTests){
            scheduleTestIds.add(scheduledTest.getId());
        }
}

@FXML
    public void EnterTest_btn(ActionEvent event) throws IOException {
        String idInput = text_id.getText();
        String codeInput = text_testCode.getText();
        if(!idInput.equals(id) || (!scheduleTestIds.contains(codeInput))){ //if the id doesn't match with the user id
                                                                           // or the testId doesn't exist
            Platform.runLater(()->{
                msg.setVisible(true);
                msg.setText("You dont have access!");
            });
            //TODO add more validation checks
        }
        else{ // valid id and valid schedule test id
            App.switchScreen("studentExecuteExam");
            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getScheduleTestWithInfo",codeInput));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            //TODO send to server and create new event in new controller
        }



    }
}
