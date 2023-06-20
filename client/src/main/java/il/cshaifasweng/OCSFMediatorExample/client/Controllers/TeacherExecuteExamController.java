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
import java.util.Optional;

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
        Platform.runLater(() -> {
            setScheduledTest(event.getScheduledTest());
            setCourseName(scheduledTest.getCourseName());
            setSubjectName(scheduledTest.getSubjectName());
            examForm = scheduledTest.getExamForm();
            if(examForm.getGeneralNotes()!=null) teacher_notes.setText(examForm.getGeneralNotes());
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestionScores", examForm));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Subscribe
    public synchronized void onManagerExtraTimeEvent (ManagerExtraTimeEvent event) {
            List<Object> eventObj = event.getData();
            ScheduledTest eventTest = (ScheduledTest) eventObj.get(0);

            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Manager response");

                if (eventTest.getId().equals(scheduledTest.getId())) {
                    if ((Boolean) eventObj.get(1)) {
                            alert.setContentText("Time will update shortly");
                            alert.setHeaderText("Manager approved your request!");
                            alert.show();
                        } else {
                            alert.setHeaderText("Manager did not approve your request");
                            alert.setContentText("The time limit did not change. You can send another request");
                            alert.show();
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
            Platform.runLater(()->{
                errorLabel.setText("fill both fields!");
                errorLabel.setVisible(true);
            });
        }
        else {
            if (!extraTime.getText().matches("-?\\d+")) {
                // The input is not a valid integer
                Platform.runLater(()->{
                    errorLabel.setText("Extra time field not legal. Enter an Integer!");
                    errorLabel.setVisible(true);
                });

            } else {
                // The input is a valid integer
                Platform.runLater(()->{
                    errorLabel.setText("The request was sent to the manager and will soon respond.");
                    errorLabel.setVisible(true);
                });

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
                Platform.runLater(()->{
                    comments.clear();
                    extraTime.clear();
                });

            }
        }
    }
    @Subscribe
    public void onTimeLeftEvent(TimeLeftEvent event){// list (0) schedule test (1) time left in minutes

        List<List<Object>> scheduleTestId_timeLeft_List = event.getScheduleTestId_timeLeft();
        List<Object> scheduleTestId_timeLeft = new ArrayList<>();
        for(int i=0;i<scheduleTestId_timeLeft_List.size();i++){
            List<Object> currObj = scheduleTestId_timeLeft_List.get(i);
            String currId = (String)currObj.get(0);
            if(currId.equals(scheduledTest.getId())){
                scheduleTestId_timeLeft=currObj;
            }
        }

        List<Object> testIdTime = scheduleTestId_timeLeft;
        timeLeft = Integer.parseInt(testIdTime.get(1).toString()) ;
        String eventId = (String) testIdTime.get(0);
        int hours = timeLeft / 60;
        int remainingMinutes = timeLeft % 60;
        System.out.println(remainingMinutes);
        String formattedHours = String.format("%02d", hours);
        String formattedMinutes = String.format("%02d", remainingMinutes);
        if (eventId.equals(scheduledTest.getId())) {
            Platform.runLater(() -> {
                timeLeftText.setText((formattedHours + ":" + formattedMinutes));
            });
            updateStudentsStatus(scheduledTest.getId());
        }
    }
    @Subscribe
    public synchronized void inTimeFinishedEvent(TimerFinishedEvent event) throws IOException {
        if (scheduledTest.getId().equals(event.getScheduledTest().getId())) {
            try {
                cleanup();
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", id));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void updateStudentsStatus(String testId){
            try{
                SimpleClient.getClient().sendToServer(new CustomMessage("#getScheduleTestWithInfo", testId));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    @Subscribe
    public void onSelectedTestEvent (SelectedTestEvent event) throws IOException {
        setScheduledTest(event.getSelectedTestEvent());
            try{
                int activeStudents = scheduledTest.getActiveStudents();
                int sumSubmissions = scheduledTest.getSubmissions();
                int sumStudents = activeStudents + sumSubmissions;
                Platform.runLater(() -> {
                    studentsActiveLabel.setText(activeStudents + "/" + sumStudents);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Subscribe
    public void onShowExamFormQuestionScoresEvent(ShowExamFormQuestionScoresEvent event){
        try {

            questionScoreList = event.getQuestionScores();
            examForm.setQuestionScores(questionScoreList);
            ObservableList<Question_Score> questions1 = FXCollections.observableArrayList(questionScoreList);
            Platform.runLater(()-> Questions_List_View.setItems(questions1));

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
                        if (question.getIndexAnswer() == 1) {
                            answerLabel0.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel0);

                        Label answerLabel1 = new Label("2.      "+question.getAnswer1());
                        if (question.getIndexAnswer() == 2) {
                            answerLabel1.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel1);

                        Label answerLabel2 = new Label("3.      "+question.getAnswer2());
                        if (question.getIndexAnswer() == 3) {
                            answerLabel2.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel2);

                        Label answerLabel3 = new Label("4.      "+question.getAnswer3());
                        if (question.getIndexAnswer() == 4) {
                            answerLabel3.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel3);

                        Label score = new Label("( " + Integer.toString(questionScore.getScore()) + " points )");
                        vbox.getChildren().add(score);

                        if (questionScore.getTeacher_note()!=null) {
                            Label t_notes = new Label("Note for the teacher : " + questionScore.getTeacher_note());
                            vbox.getChildren().add(t_notes);
                        }
                        if (questionScore.getStudent_note()!=null) {
                            Label s_notes = new Label("Note for the Student : " + questionScore.getStudent_note());
                            vbox.getChildren().add(s_notes);
                        }

                        Platform.runLater(()-> setGraphic(vbox));

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
    public void handleGoHomeButtonClick(){
        try {
            String teacherId = this.id;
            cleanup();
            App.switchScreen("teacherHome");
            Platform.runLater(() -> {
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", id));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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

    public void handleBackButtonClick(ActionEvent actionEvent) {
        handleGoHomeButtonClick();
    }
}
