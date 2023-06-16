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
import javafx.scene.paint.Color;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
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
    @FXML
    private Label codeError;
    @FXML
    private Label dateError;
    @FXML
    private Label examFormError;
    @FXML
    private Label timeError;
    @FXML
    private Label radioBtnError;
    @FXML
    private Button deleteScheduleTest;

    private List<ScheduledTest> scheduledTests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSelectedTest(ScheduledTest selectedTest) {
        this.selectedTest = selectedTest;
    }

    public ScheduledTestController() {
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
        deleteScheduleTest.setVisible(true);
        buttonScheduleTest.setText("update Schedule Test");
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
        String errorTxt = "the field is empty";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy0MM0dd");
        LocalDateTime now = LocalDateTime.now();
        if (dataTimescheduleDate.getValue() != null) {
            String currentDate = dataTimescheduleDate.getValue().toString().replace('-', '0');
            String today = dtf.format(now);
            if (Integer.parseInt(currentDate) > Integer.parseInt(today)) //TODO change later to >
                return true;
            else if (Integer.parseInt(currentDate) == Integer.parseInt(today)) {
                return validateTime(true);
            } else errorTxt = "The day has passed";
        }
        dataTimescheduleDate.setStyle("-fx-border-color: #cc0000;");
        setErrorLabel(dateError, errorTxt);
        return false;
    }

    public boolean validateTime(boolean today) {
        String errorTxt = "time format is not hh:mm";
        String pattern = "^([01]\\d|2[0-3]):[0-5]\\d$";
        // Check if the time matches the pattern
        if (scheduleTime != null && scheduleTime.getText().length() == 5) {
            if (Pattern.matches(pattern, scheduleTime.getText())) {
                System.out.println("inui");
                    if (!today)
                        return true;
                    else {
                        int timeNumberNow = Integer.parseInt(LocalTime.now().toString().substring(0, 5).replace(":", "0"));
                        int timeSchedule = Integer.parseInt(scheduleTime.getText().substring(0, 5).replace(":", "0"));
                        if (timeSchedule > timeNumberNow)
                            return true;
                        errorTxt = "time passed";
                    }
                } else {
                    System.out.println("lolo");
                    errorTxt = "time is not valid";}
        }
        scheduleTime.setStyle("-fx-border-color: #cc0000;");
        setErrorLabel(timeError, errorTxt);
        return false;
    }

    public boolean validateCode() {
        String errorTxt = "the field is empty";
        String patternCode = "[a-zA-Z0-9]{4}";
        if (scheduleCode != null) {
            if (Pattern.matches(patternCode, scheduleCode.getText())) {
                for (ScheduledTest scheduledTest : scheduledTests) {
                    if (scheduledTest.getId().equals(scheduleCode.getText()) && selectedTest == null) {
                        scheduleCode.setStyle("-fx-border-color: #cc0000;");
                        setErrorLabel(codeError, "code is already exist");
                        return false;
                    }
                }
                return true;
            }
            errorTxt = "need exactly 4 characters with only letters and numbers";
        }
        scheduleCode.setStyle("-fx-border-color: #cc0000;");
        setErrorLabel(codeError, errorTxt);
        return false;
    }

    public boolean validateSchesuledForm() {
        boolean valid;
        dataTimescheduleDate.setStyle("-fx-border-color:default;");
        scheduleTime.setStyle("-fx-border-color:default;");
        comboBoxExamForm.setStyle("-fx-border-color:default;");
        scheduleCode.setStyle("-fx-border-color:default;");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        valid = validateDate() & validateTime(false) & validateCode();
        if (comboBoxExamForm.getValue() == null) {
            comboBoxExamForm.setStyle("-fx-border-color:#cc0000;");
            setErrorLabel(examFormError, "select examForm");
            valid = false;
        }
        if (!(radioComputerTest.isSelected() || radioManualTest.isSelected())) {
            setErrorLabel(radioBtnError, "has not selected");
            valid = false;
        }
        if (!valid) {
            errorAlert.setTitle("input not valid!");
            errorAlert.setContentText("Please fill all the fields correctly!");
            errorAlert.setHeaderText("Error!");
            errorAlert.show();
            return false;
        }
        return true;
    }

    void setErrorLabel(Label label, String errorTxt) {
        label.setVisible(true);
        label.setText(errorTxt);
        label.setTextFill(Color.RED);
    }

    void resetVisibleLabel() {
        codeError.setVisible(false);
        dateError.setVisible(false);
        timeError.setVisible(false);
        examFormError.setVisible(false);
        radioBtnError.setVisible(false);
    }

    @Subscribe
    public void onUpdateScheduleTestEvent(UpdateScheduleTestEvent event) {
        Platform.runLater(() -> {
            if (event.isCheck()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success!");
                alert.setContentText("Scheduled test saved successfully!");
                alert.setTitle("Information");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setHeaderText("Error");
                alert.setContentText("There was a problem and the test did not save. please enter it again");
                alert.show();
            }
            try {
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
        });
    }

    @FXML
    void sendSchedule(ActionEvent event) {
        resetVisibleLabel();
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
                selectedTest.setTimeLimit(examForm.getTimeLimit());
                selectedTest.setTeacher(teacher);
                ScheduledTest scheduledTest1 = selectedTest;
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#updateScheduleTest", scheduledTest1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ScheduledTest scheduledTest = new ScheduledTest(scheduleCode.getText(), LocalDate.of(year, month, day), LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)), 0), radioComputerTest.isSelected());
                scheduledTest.setExamForm(examForm);
                scheduledTest.setTimeLimit(examForm.getTimeLimit());
                scheduledTest.setTeacher(teacher);
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#addScheduleTest", scheduledTest));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void deleteScheduleTest(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this Schedule Test?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            SimpleClient.getClient().sendToServer(new CustomMessage("#deleteRow", selectedTest));
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

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Your changes will be lost. Do you wand to proceed?");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("teacherHome");
            Platform.runLater(() -> {
                EventBus.getDefault().post(new MoveIdToNextPageEvent(id));

            });
            cleanup();
        }
    }

    @FXML
    void handleBackButtonClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Your changes will be lost. Do you wand to proceed?");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
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

}
