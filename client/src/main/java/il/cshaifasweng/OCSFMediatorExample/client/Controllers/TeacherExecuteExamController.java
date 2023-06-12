package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
    @FXML
    private ListView<Question_Score> Questions_List_View;
    @FXML
    private TextArea teacher_notes;

    private List<Question_Score> questionScoreList;
    private List<Question> questionList;
    private ExamForm examForm;

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
            studentsActiveLabel.setText("0/0");
            timeLeftText.setText("time will update shortly");
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
    public void onTeacherExecuteExamEvent(TeacherExecuteExamEvent event) throws IOException {
        setScheduledTest(event.getScheduledTest());
        setCourseName(scheduledTest.getCourseName());
        setSubjectName(scheduledTest.getSubjectName());
        examForm = scheduledTest.getExamForm();
        SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestionScores",examForm));
    }
    @Subscribe
    public void onManagerExtraTimeEvent (ManagerExtraTimeEvent event) {
        Platform.runLater(() -> {
            List<Object> eventObj = event.getData();
            ScheduledTest eventTest = (ScheduledTest) eventObj.get(0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Manager response");
            alert.setHeaderText(null);

            if (eventTest.getId().equals(scheduledTest.getId())) {
                if ((Boolean) eventObj.get(1)) {
                    Platform.runLater(() -> {
                       alert.setContentText("Manager approved your request and the time will update shortly");
                       alert.show();
                    });
                } else {
                    Platform.runLater(() -> {
                        alert.setContentText("Manager did not approve your request");
                        alert.show();
                    });
                }
                errorLabel.setVisible(false);
                comments.clear();
                extraTime.clear();
            }
        });

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
                errorLabel.setText("The request was sent to the manager and will soon respond.");
                errorLabel.setVisible(true);

                int number = Integer.parseInt(extraTime.getText());
                //List<Object> data = new ArrayList<>();
                //data.add(comments.getText());
                //data.add(number);
                String fullName = teacherFirstName.concat(" ").concat(teacherLastName);
                //data.add(fullName);
                String subCourse = subjectName.concat(" ").concat(courseName);
                //data.add(subCourse);
                //data.add(scheduledTest);
                ExtraTime extraTimeRequest = new ExtraTime(fullName, number, subCourse, (comments.getText()), scheduledTest);
                // data = 0. comments, 1. extraTime, 2. teacher full name, 3. sub + course 4. schedule test.
                try{
                    SimpleClient.getClient().sendToServer(new CustomMessage("#addExtraTimeRequest", extraTimeRequest));
                    //SimpleClient.getClient().sendToServer(new CustomMessage("#extraTimeRequest", data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                comments.clear();
                extraTime.clear();
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
                updateStudentsStatus(scheduledTest.getId());
            });
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
                SimpleClient.getClient().sendToServer(new CustomMessage("#getScheduleTestWithInfo", testId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Subscribe
    public void onSelectedTestEvent (SelectedTestEvent event) throws IOException {
        setScheduledTest(event.getSelectedTestEvent());
        Platform.runLater(() -> {
            try{
                int activeStudents = scheduledTest.getActiveStudents();
                int sumSubmissions = scheduledTest.getSubmissions();
                int sumStudents = activeStudents + sumSubmissions;
                studentsActiveLabel.setText(activeStudents + "/" + sumStudents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Subscribe
    public void onShowExamFormQuestionScoresEvent(ShowExamFormQuestionScoresEvent event){
        try {

            questionScoreList = event.getQuestionScores();
            examForm.setQuestionScores(questionScoreList);
            ObservableList<Question_Score> questions1 = FXCollections.observableArrayList(questionScoreList);
            Questions_List_View.setItems(questions1);

            Questions_List_View.setCellFactory(param -> new ListCell<Question_Score>() {
                @Override
                protected void updateItem(Question_Score questionScore, boolean empty) {
                    super.updateItem(questionScore, empty);
                    if (empty || questionScore == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Question question = questionScore.getQuestion();
                        VBox vbox = new VBox();
                        Label questionText = new Label(question.getText());
                        vbox.getChildren().add(questionText);

                        // Add the answers as separate labels in the VBox

                        Label answerLabel0 = new Label("1.      "+question.getAnswer0());
                        if (question.getIndexAnswer() == 0) {
                            answerLabel0.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel0);

                        Label answerLabel1 = new Label("2.      "+question.getAnswer1());
                        if (question.getIndexAnswer() == 1) {
                            answerLabel1.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel1);

                        Label answerLabel2 = new Label("3.      "+question.getAnswer2());
                        if (question.getIndexAnswer() == 2) {
                            answerLabel2.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel2);

                        Label answerLabel3 = new Label("4.      "+question.getAnswer3());
                        if (question.getIndexAnswer() == 3) {
                            answerLabel3.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel3);

                        Label score = new Label("( " + Integer.toString(questionScore.getScore()) + " points )");
                        vbox.getChildren().add(score);

                        setGraphic(vbox);

                    }
                }
            });
            Platform.runLater(()->{
                teacher_notes.setText(examForm.getGeneralNotes());
                teacher_notes.setDisable(true);
            });

        }catch (Exception e){
            e.printStackTrace();
        }
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
