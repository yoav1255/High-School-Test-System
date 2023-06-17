package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdTestOverEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveManagerIdEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class TeacherHomeController {

    @FXML
    private Button allStudentsBN;

    @FXML
    private Button homeBN;

    @FXML
    private Label idLabel;

    @FXML
    private Label helloLabel;

    private String id;
    private Teacher teacher;
    private static int instances = 0;

    public TeacherHomeController(){

    }
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            info.add(id);
            info.add("teacher");
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        });
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
            setId((String) event.getUserID().get(0));
            teacher = (Teacher) event.getUserID().get(1);
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
            if (teacher != null) {
                helloLabel.setText("Hello Teacher " + teacher.getFirst_name() + " " + teacher.getLast_name());
            }
        });


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
    public void goToStatistics(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showStatistics");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
        });
    }

    @FXML
    public void handleExecExamButtonClick(ActionEvent event){
        try {
            cleanup();
            App.switchScreen("teacherExamEntry");
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
    @Subscribe
    public void onMoveIdTestOverEvent (MoveIdTestOverEvent event){
        setId(event.getId());
        initializeIfIdNotNull();
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Test is over!");
            alert.show();
        });
    }

    public void handleLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LOGOUT");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            ArrayList<String> info = new ArrayList<>();
            info.add(id);
            info.add("teacher");
            SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        } else {
            alert.close();
        }
    }

    public void handleBackButtonClick(ActionEvent actionEvent) {
    }
}
