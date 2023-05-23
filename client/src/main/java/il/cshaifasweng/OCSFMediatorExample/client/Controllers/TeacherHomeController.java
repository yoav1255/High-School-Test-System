package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class TeacherHomeController {

    @FXML
    private Button allStudentsBN;

    @FXML
    private Button homeBN;

    @FXML
    private Label idLabel;

    @FXML
    private Label statusLB;

    private String id;
    private static int instances = 0;

    public TeacherHomeController(){

    }
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        instances++;
        System.out.println("in teacher "+ instances);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
        instances--;
        System.out.println("in teacher "+ instances);
    }

    public void setId(String id){this.id = id;}


    @FXML
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserHomeEvent(UserHomeEvent event){
            setId(event.getUserID());
            System.out.println("on show teacher event id "+ this.id );
            initializeIfIdNotNull();
    }

    private void initializeIfIdNotNull() {
        Platform.runLater(()->{
            if (id != null) {
                System.out.println("in intialize Function id "+this.id);
                idLabel.setText("ID: " + this.id);
            }
        });
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {

    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {

    }

    @FXML
    public void handleShowQuestionsButtonClick(ActionEvent event) {
            try {
                App.switchScreen("showAllQuestions");
                Platform.runLater(()->{
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#SendIdToExamForms",id));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @FXML
    public void handleShowExamFormsButtonClick(ActionEvent event) {
        try {
            cleanup();
            App.switchScreen("showExamForms");
            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleShowScheduledTestsButtonClick(ActionEvent event) {
        Platform.runLater(()->{
            try{
                App.switchScreen("showScheduleTest");
                SimpleClient.getClient().sendToServer(new CustomMessage("#SendIdToExamForms",id));
                cleanup();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void handleShowStatsButtonClick(ActionEvent event) {
    }

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        setId(event.getId());
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", this.id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
