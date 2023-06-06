package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.SelectedStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class StudentHomeController {

    @FXML
    private Button allStudentsBN;

    @FXML
    private Button homeBN;

    @FXML
    private Label idLabel;

    @FXML
    private Label statusLB;

    private String id;

    public StudentHomeController(){
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setId(String id){this.id = id;}
    private Student student;


    @FXML
    @Subscribe
    public void onUserHomeEvent(UserHomeEvent event){
        setId(event.getUserID());
        Platform.runLater(()->{
            initializeIfIdNotNull();
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getStudent",id));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void initializeIfIdNotNull() {
        if (id != null) {
            idLabel.setText("ID: " + id);
        }
    }
    @Subscribe
    public void onSelectedStudentEvent(SelectedStudentEvent event) {
        student = event.getStudent();
    }

    @FXML
    void handleEnterTestClick(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("examEntry");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
        });

    }

@FXML
    public void goToAllTests(ActionEvent event) {
    try {
        App.switchScreen("showOneStudent");

        Platform.runLater(()->{
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTests", student));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
} catch (IOException e) {
        e.printStackTrace();
    }
    }
}
