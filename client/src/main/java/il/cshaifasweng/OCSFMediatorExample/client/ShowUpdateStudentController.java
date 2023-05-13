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
            newGrade = new TextField("0");
            oldGrade = new TextField("0");
            oldGrade.setText("0");
            instance.newGrade = new TextField("0");
            System.out.println("in show Update Student controller");
            instance.studentTest = event.getStudentTest();
            instance.oldGrade = new TextField(String.valueOf(instance.studentTest.getGrade()));
            instance.oldGrade.setText("0");
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
            System.out.println(st);
            System.out.println(st.getGrade()+5);
            int newG = Integer.parseInt(newGrade.getText());
            System.out.println(st.getGrade()-23);
            App.updateStudentGrade(st,newG);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Update success");
            alert.setContentText("Grade successfully updated");
            alert.showAndWait();

            SimpleClient.getClient().sendToServer("#showAllStudents");//TODO go back to the student
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("allStudents");
            cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}



