package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class CreateTestController {

    @FXML
    private ComboBox<?> ComboSubject;
    @FXML
    private ComboBox<?> ComboCourse;
    @FXML
    private GridPane OneStudentGR;

    @FXML
    private ListView<?> TableQuestions;

    @FXML
    private Button allStudentsBN;

    @FXML
    private Button backBN;

    @FXML
    private Pane grade_upgrade_info;

    @FXML
    private Button homeBN;

    @FXML
    private Label statusLB;

    @FXML
    private Pane student_details_PN;

    @FXML
    private TextField timeLimit;


//    public CreateTestController(){ EventBus.getDefault().register(this); }
//    public void cleanup() {
//        EventBus.getDefault().unregister(this);
//    }



    @FXML
    void initialize(){
        ComboCourse.setDisable(true);
        TableQuestions.setDisable(true);
        //SimpleClient.getClient().sendToServer();
    }

    public void onSelectSubject(ActionEvent event) {

    }
    @FXML
    void handleBackButtonClick(ActionEvent event) {
        //TODO func
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {
        try{
            App.switchScreen("primary");
            //cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {
        try{
            SimpleClient.getClient().sendToServer("#showAllStudents");
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("allStudents");
            //cleanup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
