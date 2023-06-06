package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeacherExecuteExamController {
    private String id;
    private ScheduledTest scheduledTest;
    @FXML
    private Label timeLeftText;
    @FXML
    private Label studentsActiveLabel;
    @FXML
    private TextField comments;
    @FXML
    private TextField extraTime;
    @FXML
    private Label errorLabel;
    private String courseName;
    private String subjectName;
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    private String teacherFirstName;
    private String teacherLastName;
    public String getTeacherLastName() {
        return teacherLastName;
    }
    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }
    public String getTeacherFirstName() {
        return teacherFirstName;
    }
    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }
    private Integer timeLeft;
    public ScheduledTest getScheduledTest() {return scheduledTest;}
    public void setScheduledTest(ScheduledTest scheduledTest) {this.scheduledTest = scheduledTest;}
    public String getId() {return id;}
    public void setId(String id) {
        this.id = id;
    }
    public TeacherExecuteExamController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
    @FXML
    void initialize() {
        Platform.runLater(() -> {
            errorLabel.setVisible(false);
        });

    }
    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) {
        setId(event.getId());
        try{
        SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacher", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onTeacherFromIdEvent(TeacherFromIdEvent event){
        setTeacherFirstName(event.getTeacherFromId().getFirst_name());
        setTeacherLastName(event.getTeacherFromId().getLast_name());
    }
    @Subscribe
    public void onTeacherExecuteExamEvent(TeacherExecuteExamEvent event){
        setScheduledTest(event.getScheduledTest());
        setCourseName(scheduledTest.getCourseName());
        setSubjectName(scheduledTest.getSubjectName());
    }
    @Subscribe
    public void onManagerExtraTimeEvent (ManagerExtraTimeEvent event) {
        List<Object> eventObj = event.getData();
        ScheduledTest eventTest = (ScheduledTest) eventObj.get(0);

        if (eventTest.getId().equals(scheduledTest.getId())) {
            if ((Boolean) eventObj.get(1)) {
                Platform.runLater(() -> {
                    int input = JOptionPane.showOptionDialog(null, "Manager approved your request and the time will update shortly ", "Information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                });
            } else {
                Platform.runLater(() -> {
                    int input = JOptionPane.showOptionDialog(null, "Manager did not approve your request", "Information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                });
            }
            errorLabel.setVisible(false);
        }
    }
    @FXML
    public void handleSendClick(ActionEvent event){
        if (comments.getText().isEmpty() || extraTime.getText().isEmpty()){
            errorLabel.setText("fill both fields!");
            errorLabel.setVisible(true);
        }
        else {
            if (!extraTime.getText().matches("-?\\d+")) {
                // The input is not a valid integer
                errorLabel.setText("Extra time field not legal. Enter an Integer!");
                errorLabel.setVisible(true);
            } else {
                // The input is a valid integer
                errorLabel.setText("The request was sent to the manager. you will receive a pop-up message when he will respond.");
                errorLabel.setVisible(true);

                int number = Integer.parseInt(extraTime.getText());
                List<Object> data = new ArrayList<>();
                data.add(comments.getText());
                data.add(number);
                String fullName = teacherFirstName.concat(" ").concat(teacherLastName);
                data.add(fullName);
                String subCourse = subjectName.concat(" ").concat(courseName);
                data.add(subCourse);
                data.add(scheduledTest);
                // data = 0. comments, 1. extraTime, 2. teacher full name, 3. sub + course 4. schedule test.
                try{
                SimpleClient.getClient().sendToServer(new CustomMessage("#extraTimeRequest", data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Subscribe
    public void onTimeLeftEvent(TimeLeftEvent event){// list (0) schedule test (1) time left in minutes
        List<Object> testIdTime = event.getScheduleTestId_timeLeft();
        timeLeft = Integer.parseInt(testIdTime.get(1).toString()) ;
        String eventId = (String) testIdTime.get(0);
        if (eventId.equals(scheduledTest.getId())) {
            Platform.runLater(() -> {
                timeLeftText.setText(Integer.toString(timeLeft));
            });
            updateStudentsStatus(scheduledTest.getId());
        }
    }
    @Subscribe
    public void inTimeFinishedEvent(TimerFinishedEvent event) throws IOException {
        if (scheduledTest.getId().equals(event.getScheduledTest().getId())) {
            try {
                cleanup();
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    EventBus.getDefault().post(new MoveIdTestOverEvent(id));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void updateStudentsStatus(String testId){
        Platform.runLater(() -> {
            try{
                SimpleClient.getClient().sendToServer(new CustomMessage("#getScheduleTestWithInfo", "testId"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Subscribe
    public void onSelectedTestEvent (SelectedTestEvent event){
        setScheduledTest(event.getSelectedTestEvent());
        Platform.runLater(() -> {
            try{
                int activeStudents = scheduledTest.getActiveStudents();
                int sumSubmissions = scheduledTest.getSubmissions();
                int sumStudents = activeStudents + sumSubmissions;
                studentsActiveLabel.setText(String.valueOf(activeStudents) + "/" + sumStudents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    public void handleHomeButtonClick(){
        try {
            String teacherId = this.id;
            cleanup();
            App.switchScreen("teacherHome");
            Platform.runLater(() -> {
                EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
