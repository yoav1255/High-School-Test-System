package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.Events.ShowUpdateStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import il.cshaifasweng.OCSFMediatorExample.server.App;

import java.io.IOException;


public class ShowUpdateStudentController {
    private static ShowUpdateStudentController instance;
    private StudentTest studentTest;
    @FXML
    private Label oldGrade;
    @FXML
    private TextField newGrade;

    @FXML
    private Label statusLB;

    @FXML
    private Label test_course;

    @FXML
    private Label test_id;

    @FXML
    private Label update_status;

    public ShowUpdateStudentController(){
        EventBus.getDefault().register(this);
    }
    public static ShowUpdateStudentController getInstance() {
        if (instance == null) {
            instance = new ShowUpdateStudentController();
        }
        return instance;
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public StudentTest getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(StudentTest studentTest) {
        this.studentTest = studentTest;
    }


    @FXML void initialize(){
        statusLB.setText(statusLB.getText() + instance.studentTest.getExamFormCode());
        test_id.setText(String.valueOf(instance.studentTest.getExamFormCode()));
        test_course.setText(String.valueOf(instance.studentTest.getCourseName()));
        oldGrade.setText(String.valueOf(instance.studentTest.getGrade()));
    }
    @Subscribe
    public void onShowUpdateStudentEvent(ShowUpdateStudentEvent event){
        try{
            ShowUpdateStudentController instance = getInstance();
            instance.studentTest = event.getStudentTest();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleUpdateButton(javafx.event.ActionEvent event) {
        try {
            ShowUpdateStudentController instance = getInstance();
            StudentTest st = instance.studentTest;
            try {
                int newG = Integer.parseInt(newGrade.getText());
                if (newG >= 0 && newG <= 100) {
                    App.updateStudentGrade(st, newG);

                    /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success");
                    alert.setContentText("Grade successfully updated");
                    alert.showAndWait();*/

                    SimpleClient.getClient().sendToServer(instance.studentTest.getStudent());
                    il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("showOneStudent");
                    cleanup();
                } else {
                    /*
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Illegal input");
                    alert.setHeaderText("Update Failed");
                    alert.setContentText("Invalid input, please enter a grade between 0 to 100");
                    alert.showAndWait();*/
                    update_status.setText("Invalid input, please enter a grade between 0 to 100");
                    newGrade.clear();
                }
            }catch (NumberFormatException notNum){
                /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Illegal input");
                alert.setHeaderText("Update Failed");
                alert.setContentText("Invalid input, please enter a valid number");
                alert.showAndWait();*/
                update_status.setText("Invalid input, please enter a valid number");
                newGrade.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML public void goBackButton(){
        try {
            SimpleClient.getClient().sendToServer(instance.studentTest.getStudent());
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("showOneStudent");
            cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event){
        try{
            SimpleClient.getClient().sendToServer("#showAllStudents");
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("allStudents");
            cleanup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void handleGoHomeButtonClick(ActionEvent event){
        try{
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("primary");
            cleanup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}



