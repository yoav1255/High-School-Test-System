package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.StudentTest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import il.cshaifasweng.OCSFMediatorExample.server.App;

import java.awt.event.ActionEvent;

public class ShowUpdateStudentController {
    private static ShowUpdateStudentController instance;
    private StudentTest studentTest;
    @FXML
    private TextField oldGrade;
    @FXML
    private TextField newGrade;

    public ShowUpdateStudentController(){
        EventBus.getDefault().register(this);
        System.out.println("check instances");
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


    @Subscribe
    public void onShowUpdateStudentEvent(ShowUpdateStudentEvent event){
        try{
            ShowUpdateStudentController instance = getInstance();
            System.out.println("in show Update Student controller");
            instance.studentTest = event.getStudentTest();
            int grade = instance.studentTest.getGrade();
//TODO make old grade and newgrade
            instance.newGrade = new TextField();
            newGrade = new TextField();
            instance.oldGrade = new TextField();
            oldGrade = new TextField();
            instance.oldGrade.setText(String.valueOf(grade));
            oldGrade.setText(String.valueOf(grade));

            System.out.println("after setting the test");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleUpdateButton(javafx.event.ActionEvent event) {
        try {
            ShowUpdateStudentController instance = getInstance();
            System.out.println("Handling update button1");
            StudentTest st = instance.studentTest;
            try {
                int newG = Integer.parseInt(newGrade.getText());
                if (newG >= 0 && newG <= 100) {
                    App.updateStudentGrade(st, newG);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success");
                    alert.setContentText("Grade successfully updated");
                    alert.showAndWait();

                    SimpleClient.getClient().sendToServer(instance.studentTest.getStudent());
                    il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("showOneStudent");
                    cleanup();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Illegal input");
                    alert.setHeaderText("Update Failed");
                    alert.setContentText("Invalid input, please enter a grade between 0 to 100");
                    alert.showAndWait();
                }
            }catch (NumberFormatException notNum){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Illegal input");
                alert.setHeaderText("Update Failed");
                alert.setContentText("Invalid input, please enter a valid number");
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML public void goBackButton(){
        try {
            SimpleClient.getClient().sendToServer(instance.studentTest.getStudent());
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("showOneStudent");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}



