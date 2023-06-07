/**
 * Sample Skeleton for 'scheduledTest.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.*;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;

public class ScheduledTestController {
    private String id;

    private ExamForm examForm;

    private Teacher teacher;
    private ScheduledTest selectedTest;
    @FXML // fx:id="allStudentsBN"
    private Button allStudentsBN; // Value injected by FXMLLoader

    @FXML // fx:id="buttonScheduleTest"
    private Button buttonScheduleTest; // Value injected by FXMLLoader

    @FXML // fx:id="comboBoxExamForm"
    private ComboBox<String> comboBoxExamForm; // Value injected by FXMLLoader

    @FXML // fx:id="dataTimescheduleDate"
    private DatePicker dataTimescheduleDate; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleTime"
    private TextField scheduleTime; // Value injected by FXMLLoader

    @FXML // fx:id="homeBN"
    private Button homeBN; // Value injected by FXMLLoader

    @FXML // fx:id="sendSchedule"
    private Button sendSchedule; // Value injected by FXMLLoader

    @FXML // fx:id="statusLB"
    private Label statusLB; // Value injected by FXMLLoader

    @FXML
    private TextField scheduleCode;
    @FXML
    private RadioButton radioComputerTest;

    @FXML
    private RadioButton radioManualTest;
    private List<ScheduledTest> scheduledTests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //    public ScheduledTest getSelectedTest(){return selectedTest;}
    public void setSelectedTest(ScheduledTest selectedTest) {
        this.selectedTest = selectedTest;
    }

    public ScheduledTestController() {
        EventBus.getDefault().register(this);

    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onTeacherFromIdEvent(TeacherFromIdEvent event) {
        teacher = event.getTeacherFromId();
    }

    @Subscribe
    public void onExamFormEvent(ExamFormEvent event) {
        List<String> examFormList = event.getExamFormEventCode();
        comboBoxExamForm.setItems(FXCollections.observableArrayList(examFormList));
    }

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        setId(event.getId());
        scheduleTime.setText("12:00");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowScheduleTestEvent(ShowScheduleTestEvent event) {
        scheduledTests = event.getScheduledTestList();
    }

    @Subscribe
    public void onSelectedTestEvent(SelectedTestEvent event) throws IOException {
        setSelectedTest(event.getSelectedTestEvent());
        scheduleCode.setText(selectedTest.getId());
        scheduleCode.setEditable(false);
        scheduleCode.setStyle("-fx-background-color: grey;");
        scheduleTime.setText(selectedTest.getTime().toString().substring(0, 5));
        dataTimescheduleDate.setValue(selectedTest.getDate());
        radioComputerTest.setSelected(selectedTest.getIsComputerTest());
        radioManualTest.setSelected(!selectedTest.getIsComputerTest());
        try {
            comboBoxExamForm.setValue(selectedTest.getExamForm().getCode());
        } catch (NullPointerException e) {
            // Handle the exception here (e.g., set a default value)
            comboBoxExamForm.setValue("");
        }
    }


    @FXML
    void comboAction(ActionEvent event) {
        if (comboBoxExamForm.getValue() != null) {
            Platform.runLater(() -> {
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#sendExamFormId", comboBoxExamForm.getValue()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    void handleRadioComputerTest(ActionEvent event) {
        if (radioComputerTest.isSelected()) {
            radioManualTest.setSelected(false);
        }
    }

    @FXML
    void handleRadioManualTest(ActionEvent event) {
        if (radioManualTest.isSelected()) {
            radioComputerTest.setSelected(false);
        }
    }

    @Subscribe
    public void onScheduledTestEvent(ScheduledTestEvent event) {
        this.examForm = (ExamForm) event.getScheduledTestEvent();

    }

    public boolean validateDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy0MM0dd");
        LocalDateTime now = LocalDateTime.now();
        if (dataTimescheduleDate.getValue() != null) {
            String currentDate = dataTimescheduleDate.getValue().toString().replace('-', '0');
            String today = dtf.format(now);
            if (Integer.parseInt(currentDate) >= Integer.parseInt(today)) //TODO change later to >
                return true;
        }
        dataTimescheduleDate.setStyle("-fx-border-color: #cc0000;");
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
        scheduleTime.setStyle("-fx-border-color:#cc0000;");
        return false;
    }


    public boolean validateCode() {
        String patternCode = "[a-zA-Z0-9]{4}";
        if (scheduleCode != null) {
            if (Pattern.matches(patternCode, scheduleCode.getText())) {
                for (ScheduledTest scheduledTest : scheduledTests) {
                    if (scheduledTest.getId().equals(scheduleCode.getText())&&selectedTest==null) {
                        scheduleCode.setStyle("-fx-border-color: #cc0000;");
                        return false;
                    }
                }
                return true;
            }
        }
        scheduleCode.setStyle("-fx-border-color: #cc0000;");
        return false;
    }

    public boolean validateSchesuledForm() {
        boolean valid;
        dataTimescheduleDate.setStyle("-fx-border-color:default;");
        scheduleTime.setStyle("-fx-border-color:default;");
        comboBoxExamForm.setStyle("-fx-border-color:default;");
        scheduleCode.setStyle("-fx-border-color:default;");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("ERROR");
        valid = validateDate() & validateTime() & validateCode();
        if (comboBoxExamForm.getValue() == null) {
            comboBoxExamForm.setStyle("-fx-border-color:#cc0000;");
            valid = false;
        }
        if (!(radioComputerTest.isSelected() || radioManualTest.isSelected())) {
            radioManualTest.setStyle("-fx-border-color:#cc0000;");
            radioComputerTest.setStyle("-fx-border-color:#cc0000;");
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
        int day;
        int month;
        int year;
        String time;
        if (valid) {
            day = dataTimescheduleDate.getValue().getDayOfMonth();
            month = dataTimescheduleDate.getValue().getMonthValue();
            year = dataTimescheduleDate.getValue().getYear();
            time = scheduleTime.getText();
            if (selectedTest != null) {
                selectedTest.setDate(LocalDate.of(year, month, day));
                selectedTest.setTime(LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)), 0));
                selectedTest.setIsComputerTest(radioComputerTest.isSelected());
                selectedTest.setExamForm(examForm);
                selectedTest.setTeacher(teacher);
                ScheduledTest scheduledTest1 = selectedTest;
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#updateScheduleTest", scheduledTest1));
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setHeaderText("Success");
                    success.setContentText("update schedule test Succeed");
                    success.show();
                    App.switchScreen("showScheduleTest");
                    Platform.runLater(() -> {
                        try {
                            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                            SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    cleanup();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ScheduledTest scheduledTest = new ScheduledTest(scheduleCode.getText(), LocalDate.of(year, month, day), LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)), 0), radioComputerTest.isSelected());
                scheduledTest.setExamForm(examForm);
                scheduledTest.setTeacher(teacher);

                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#addScheduleTest", scheduledTest));
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setHeaderText("Success");
                    success.setContentText("added new schedule test Succeed");
                    success.show();
                    App.switchScreen("showScheduleTest");
                    Platform.runLater(() -> {
                        try {
                            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                            SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    cleanup();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("teacherHome");
        Platform.runLater(() -> {
            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));

        });
        cleanup();
    }

    @FXML
    void handleBackBtn(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showScheduleTest");

        Platform.runLater(() -> {
            try {
                EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
