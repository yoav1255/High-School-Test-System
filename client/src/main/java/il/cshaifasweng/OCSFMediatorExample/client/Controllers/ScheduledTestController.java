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

import java.util.regex.*;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;

public class ScheduledTestController {

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

    @FXML
    private TextField scheduleCode;

    public ScheduledTestController() {
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    public void initialize() {
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
            SimpleClient.getClient().sendToServer(new CustomMessage("#sendExamFormId", comboBoxExamForm.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onScheduledTestEvent(ScheduledTestEvent event) {
        this.examForm = event.getScheduledTestEvent();
    }

    public boolean validateDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy0MM0dd");
        LocalDateTime now = LocalDateTime.now();
        if (dataTimescheduleDate.getValue() != null) {
            String currentDate = dataTimescheduleDate.getValue().toString().replace('-', '0');
            String today = dtf.format(now);
            if (Integer.parseInt(currentDate) > Integer.parseInt(today))
                return true;
        }
        dataTimescheduleDate.setStyle("-fx-border-color: red;");
        return false;
    }

    public boolean validateTime() {
        String pattern = "^([01]\\d|2[0-3]):[0-5]\\d$";
        // Check if the time matches the pattern
        if (scheduleTime != null) {
            if (Pattern.matches(pattern, scheduleTime.getText())) {
                if (Integer.parseInt(scheduleTime.getText().split(":")[0]) < 24 && Integer.parseInt(scheduleTime.getText().split(":")[1]) < 60) {
                    return true;
                }
            }

        }
        scheduleTime.setStyle("-fx-border-color:red;");
        return false;
    }

    public boolean validatesubmission() {
        String sumbittionPattern = "\\d+";
        if (textFieldsubmission != null) {
            if (Pattern.matches(sumbittionPattern, textFieldsubmission.getText())) {
                return true;
            }
        }
        textFieldsubmission.setStyle("-fx-border-color:red;");
        return false;
    }

    public boolean validateCode() {
        String patternCode = "[a-zA-Z0-9]{4}";
        if (scheduleCode != null) {
            if (Pattern.matches(patternCode, scheduleCode.getText())) {
                return true;
            }
        }
        scheduleCode.setStyle("-fx-border-color:red;");
        return false;
    }

    public boolean validateSchesuledForm() {
        boolean valid;
        dataTimescheduleDate.setStyle("-fx-border-color:default;");
        scheduleTime.setStyle("-fx-border-color:default;");
        textFieldsubmission.setStyle("-fx-border-color:default;");
        comboBoxExamForm.setStyle("-fx-border-color:default;");
        scheduleCode.setStyle("-fx-border-color:default;");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("ERROR");
        valid = validateDate() & validateTime() & validatesubmission() & validateCode();
        if (comboBoxExamForm.getValue() == null) {
            comboBoxExamForm.setStyle("-fx-border-color:red;");
            valid = false;
        }
        if (!valid) {
            errorAlert.setContentText("input not valid!");
            errorAlert.show();
            return false;
        }
        return true;
    }


    @FXML
    void sendSchedule(ActionEvent event) {
        boolean valid = validateSchesuledForm();
        if (valid) {
            int day = dataTimescheduleDate.getValue().getDayOfMonth();
            int month = dataTimescheduleDate.getValue().getMonth().getValue();
            int year = dataTimescheduleDate.getValue().getYear();
            String time = scheduleTime.getText();
            ScheduledTest scheduledTest = new ScheduledTest(scheduleCode.getText(), new Date(year, month, day), new Time(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)), 0), Integer.parseInt(textFieldsubmission.getText()));
            scheduledTest.setExamForm(examForm);
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#addScheduleTest", scheduledTest));
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setHeaderText("Success");
                success.setContentText("added new schedule test Succeed");
                success.show();
                il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("primary");
                cleanup();
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
