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
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
        instances--;
    }

    public void setId(String id){this.id = id;}


    @FXML
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserHomeEvent(UserHomeEvent event){
            setId(event.getUserID());
            initializeIfIdNotNull();
    }

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        setId(event.getId());
        initializeIfIdNotNull();
    }
    private void initializeIfIdNotNull() {
        Platform.runLater(()->{
            if (id != null) {
                idLabel.setText("ID: " + this.id);
            }
        });
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {

    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {
        try {
            App.switchScreen("allStudents");
            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents",""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleShowQuestionsButtonClick(ActionEvent event) {
            try {
                App.switchScreen("showAllQuestions");
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
    public void handleShowScheduledTestsButtonClick(ActionEvent event) throws IOException {
        try {
            cleanup();
            App.switchScreen("showScheduleTest");

            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleShowStatsButtonClick(ActionEvent event) {
    }

    @FXML
    public void handleExecExamButtonClick(ActionEvent event){
        try {
            cleanup();
            App.switchScreen("showTeacherExecExam");
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

}
