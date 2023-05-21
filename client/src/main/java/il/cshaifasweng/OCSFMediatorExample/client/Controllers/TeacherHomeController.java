package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    public TeacherHomeController(){
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
            idLabel.setText("ID: " + id);
        }
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {

    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {

    }

    public void handleShowQuestionsButtonClick(ActionEvent event) {
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleShowExamFormsButtonClick(ActionEvent event) {

        try {
            System.out.println("in btn click "+id);
            App.switchScreen("showExamForms");
            SimpleClient.getClient().sendToServer(new CustomMessage("#SendIdToExamForms",id));
            //cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleShowScheduledTestsButtonClick(ActionEvent event) {
    }

    public void handleShowStatsButtonClick(ActionEvent event) {
    }
}
