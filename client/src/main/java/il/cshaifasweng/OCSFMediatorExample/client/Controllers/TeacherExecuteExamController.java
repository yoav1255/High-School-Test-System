package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.TeacherExecuteExamEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class TeacherExecuteExamController {
    private String id;
    private ScheduledTest scheduledTest;
    public ScheduledTest getScheduledTest() {return scheduledTest;}
    public void setScheduledTest(ScheduledTest scheduledTest) {this.scheduledTest = scheduledTest;}
    public String getId() {return id;}
    public void setId(String id) {
        this.id = id;
    }
    public TeacherExecuteExamController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
    @FXML
    void initialize() {
        Platform.runLater(() -> {


        });

    }
    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) {
        setId(event.getId());
    }
    @Subscribe
    public void onTeacherExecuteExamEvent(TeacherExecuteExamEvent event){
        setScheduledTest(event.getScheduledTest());
    }
}
