package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.SelectedStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveObjectToNextPageEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class StudentHomeController{

    @FXML
    private Button GradesButton;

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

    public void setId(String id){this.id = id;}
    private Student student;

    public void init_getStudent(){
        initializeIfIdNotNull();
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getStudent",id));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    @Subscribe
    public void onUserHomeEvent(UserHomeEvent event){
        id = event.getUserID();
        Platform.runLater(()->{
            init_getStudent();
        });
    }

    @Subscribe
    public void onMoveIdToNextPage(MoveIdToNextPageEvent event){
        id = event.getId();
        Platform.runLater(()->{
            init_getStudent();
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
        cleanup();
        App.switchScreen("showOneStudent");

        Platform.runLater(()->{
            try {
                EventBus.getDefault().post(new MoveIdToNextPageEvent(student.getId()));
                EventBus.getDefault().post(new MoveObjectToNextPageEvent(student));
                SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTests", student));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
} catch (IOException e) {
        e.printStackTrace();
    }
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
            info.add("student");
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
