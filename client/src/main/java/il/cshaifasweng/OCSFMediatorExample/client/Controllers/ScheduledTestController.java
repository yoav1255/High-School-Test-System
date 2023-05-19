/**
 * Sample Skeleton for 'scheduledTest.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ExamFormEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ScheduledTestEvent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.regex.*;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ScheduledTestController {

    private static ScheduledTestController instance;

    public static ExamForm examForm;
    @FXML // fx:id="allStudentsBN"
    private Button allStudentsBN; // Value injected by FXMLLoader

    @FXML // fx:id="buttonScheduleTest"
    private Button buttonScheduleTest; // Value injected by FXMLLoader

    @FXML // fx:id="comboBoxExamForm"
    private ComboBox<String> comboBoxExamForm; // Value injected by FXMLLoader

    @FXML // fx:id="dataTimescheduleDate"
    private DatePicker dataTimescheduleDate; // Value injected by FXMLLoader

    @FXML // fx:id="labelTeacher"
    private Label labelTeacher; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleTime"
    private TextField scheduleTime; // Value injected by FXMLLoader

    @FXML // fx:id="homeBN"
    private Button homeBN; // Value injected by FXMLLoader

    @FXML // fx:id="sendSchedule"
    private Button sendSchedule; // Value injected by FXMLLoader

    @FXML // fx:id="statusLB"
    private Label statusLB; // Value injected by FXMLLoader

    @FXML
    private TextField textFieldsubmission;

    public ScheduledTestController() {
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public static ScheduledTestController getInstance() {
        if (instance == null) {
            instance = new ScheduledTestController();
        }
        return instance;
    }


    @FXML
    public void initialize() throws IOException {
        labelTeacher.setText("1");
        scheduleTime.setText("12:00");
        textFieldsubmission.setText("");
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#fillComboBox", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onExamFormEvent(ExamFormEvent event) {
        List<String> examFormList = event.getExamFormEventCode();
        comboBoxExamForm.setItems(FXCollections.observableArrayList(examFormList));
    }

    @FXML
    void comboAction(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#sendExamFormId",comboBoxExamForm.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onScheduledTestEvent(ScheduledTestEvent event) {
        System.out.println("ScheduledTestEvent");
        ExamForm scheduledTest = event.getScheduledTestEvent();
        this.examForm = scheduledTest;
    }

public boolean validateSchesuledForm(DatePicker date,String time,int submission){
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    errorAlert.setHeaderText("Input not valid");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy0MM0dd");
    LocalDateTime now = LocalDateTime.now();
    String currentDate = date.getValue().toString().replace('-', '0');
    String today=dtf.format(now);
    System.out.println(currentDate+""+today);
    if(Integer.parseInt(currentDate)<Integer.parseInt(today)){
        errorAlert.setContentText("date not valid");
        errorAlert.show();
        return false;
    }
//    String pattern = "^([01]\\d|2[0-3]):[0-5]\\d$";
////    // Check if the time matches the pattern
////        if (Pattern.matches(pattern, time)||Integer.parseInt(time.split(":")[0])>=23) {
////            // Check if the time falls within the 24/7 range
////            errorAlert.setContentText("time not valid, keep on format 'HH:mm'");
////            errorAlert.show();
////            return false;
////        }
////            if (submission<0||submission>100){
////                errorAlert.setContentText("number of submission isnt valid");
////                errorAlert.show();
////                return false;
//            }
    return true;
}


    @FXML
    void sendSchedule(ActionEvent event) {
        int day = dataTimescheduleDate.getValue().getDayOfMonth();
        int month = dataTimescheduleDate.getValue().getMonth().getValue();
        int year = dataTimescheduleDate.getValue().getYear();
        String time=scheduleTime.getText();
        boolean valid=validateSchesuledForm(dataTimescheduleDate,time,Integer.parseInt(textFieldsubmission.getText()));
        System.out.println(valid);
        if(valid){
        ScheduledTest scheduledTest=new ScheduledTest(0,new Date(year,month,day), new Time(Integer.parseInt(time.substring(0,2)),Integer.parseInt(time.substring(3,5)),0),Integer.parseInt(textFieldsubmission.getText()));
        scheduledTest.setExamForm(examForm);
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#addScheduleTest",scheduledTest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }


    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {
        try {
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("primary");
            cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer("#showAllStudents");
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("allStudents");
            cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
