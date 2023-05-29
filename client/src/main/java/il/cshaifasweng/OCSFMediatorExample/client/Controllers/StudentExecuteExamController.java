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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StudentExecuteExamController {


    @FXML
    private GridPane StudentsGR;

    @FXML
    private Button homeBN;

    @FXML
    private Label text_Id;
    @FXML
    private Button submitButton;
    @FXML
    private List<ToggleGroup> toggleGroups = new ArrayList<>();
    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private ListView<Question_Answer> questionsListView;

    private String id;
    private ScheduledTest scheduledTest;
    private StudentTest studentTest;
    private List<QuestionScore> questionScoreList;
    private Student student;
    List<Question_Answer> questionAnswers ;



    public StudentExecuteExamController() {
        EventBus.getDefault().register(this);
        System.out.println("on constructor");

    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSelectedStudentEvent(SelectedStudentEvent event){
        student =event.getStudent();
        questionAnswers= new ArrayList<>();
        System.out.println("on selected student event");
        Platform.runLater(() -> {
            text_Id.setText(text_Id.getText() + student.getFirst_name() + " " + student.getLast_name());
        });
        System.out.println("in event: "+student.getFirst_name());
        studentTest = new StudentTest();
        studentTest.setStudent(student);
        student.getStudentTests().add(studentTest);
    }

    @Subscribe
    public void onSelectedTestEvent(SelectedTestEvent event) {
        scheduledTest = event.getSelectedTestEvent();
        questionScoreList = scheduledTest.getExamForm().getQuestionScores();

        for (QuestionScore questionScore : questionScoreList) {
            Question_Answer questionAnswer = new Question_Answer();
            questionAnswer.setStudentTest(studentTest);
            System.out.println("check question score "+questionScore.getId());
            questionAnswer.setQuestionScore(questionScore);
            questionAnswer.setAnswer(-1); // Initialize with no answer selected
            questionScore.getQuestionAnswers().add(questionAnswer);

            questionAnswers.add(questionAnswer);
        }

        ObservableList<Question_Answer> questionAnswerObservableList = FXCollections.observableArrayList(questionAnswers);
        questionsListView.setItems(questionAnswerObservableList);

        questionsListView.setCellFactory(param -> new ListCell<Question_Answer>() {
            @Override
            protected void updateItem(Question_Answer questionAnswer, boolean empty) {
                super.updateItem(questionAnswer, empty);

                if (empty || questionAnswer == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String questionText = questionAnswer.getQuestionScore().getQuestion().getText();
                    String answer0 = questionAnswer.getQuestionScore().getQuestion().getAnswer0();
                    String answer1 = questionAnswer.getQuestionScore().getQuestion().getAnswer1();
                    String answer2 = questionAnswer.getQuestionScore().getQuestion().getAnswer2();
                    String answer3 = questionAnswer.getQuestionScore().getQuestion().getAnswer3();

                    VBox vbox = new VBox();
                    vbox.setSpacing(10);

                    Label questionLabel = new Label("Question:      " + questionText);
                    vbox.getChildren().add(questionLabel);

                    toggleGroup = new ToggleGroup();

                    RadioButton answer1RadioButton = new RadioButton("1.    " + answer0);
                    answer1RadioButton.setToggleGroup(toggleGroup);
                    vbox.getChildren().add(answer1RadioButton);

                    RadioButton answer2RadioButton = new RadioButton("2.     " + answer1);
                    answer2RadioButton.setToggleGroup(toggleGroup);
                    vbox.getChildren().add(answer2RadioButton);

                    RadioButton answer3RadioButton = new RadioButton("3.     " + answer2);
                    answer3RadioButton.setToggleGroup(toggleGroup);
                    vbox.getChildren().add(answer3RadioButton);

                    RadioButton answer4RadioButton = new RadioButton("4.     " + answer3);
                    answer4RadioButton.setToggleGroup(toggleGroup);
                    vbox.getChildren().add(answer4RadioButton);

                    Label scoreLabel = new Label("Points: " + questionAnswer.getQuestionScore().getScore());
                    vbox.getChildren().add(scoreLabel);

                    setGraphic(vbox);
                    toggleGroups.add(toggleGroup);

                    //

                    // Listen for changes in the selected toggle
                    toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                        RadioButton selectedRadioButton = (RadioButton) newValue;
                        if (selectedRadioButton != null) {
                            int answerIndex = Integer.parseInt(selectedRadioButton.getText().split("\\.")[0]) - 1;
                            questionAnswer.setAnswer(answerIndex); // Update the answer index in the Question_Answer object
                            System.out.println("Question: " + questionAnswer.getQuestionScore().getQuestion().getText());
                            System.out.println("Selected Answer: " + selectedRadioButton.getText());
                        } else {
                            System.out.println("No answer selected for question: " + questionAnswer.getQuestionScore().getQuestion().getText());
                        }
                    });

                    //
                }
            }
        });
    }

    @FXML
    public void submitTestBtn(ActionEvent event) throws IOException {
        LocalTime timeStart = scheduledTest.getTime();
        LocalTime currentTime = LocalTime.now();
        Duration timeToComplete = Duration.between(timeStart,currentTime);
        System.out.println("Hour "+ timeToComplete.toHours());
        System.out.println("Minutes "+ timeToComplete.toMinutes());
        System.out.println("Seconds "+ timeToComplete.toSeconds());

        studentTest.setTimeToComplete(timeToComplete.toMillis());
        studentTest.setScheduledTest(scheduledTest);
        studentTest.setQuestionAnswers(questionAnswers);
        int sum =0;
        // student test is ready
        //TODO add timer and add timeToComplete field
        //TODO add scroll bar/pane

        for(Question_Answer questionAnswer:questionAnswers){
            int points = questionAnswer.getQuestionScore().getScore();
            int indexAnsStudent = questionAnswer.getAnswer();
            int indexCorrect = questionAnswer.getQuestionScore().getQuestion().getIndexAnswer();
            if(indexAnsStudent == indexCorrect){
                sum+=points;
            }
        }
        studentTest.setGrade(sum);
        List<Object> student_studentTest_questionAnswers = new ArrayList<>();
        student_studentTest_questionAnswers.add(student);
        student_studentTest_questionAnswers.add(studentTest);
        for(Question_Answer questionAnswer:questionAnswers){
            student_studentTest_questionAnswers.add(questionAnswer);
        }

        SimpleClient.getClient().sendToServer(new CustomMessage("#saveQuestionAnswers",student_studentTest_questionAnswers));
    }

@Subscribe
    public void onShowSuccessEvent(ShowSuccessEvent event) throws IOException {
        System.out.println("good");
        cleanup();
        App.switchScreen("studentHome");
        JOptionPane.showMessageDialog(null, "Exam Submitted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
        });

    }

@Subscribe
    public void onTimerStartEvent(TimerStartEvent event){
        if(event.getScheduledTest().getId().equals(scheduledTest.getId()))
        {
            System.out.println(" on schedule test "+ scheduledTest.getId() + " timer started ");
        }
    }
@Subscribe
    public void onTimerFinishedEvent(TimerFinishedEvent event){
        if(event.getScheduledTest().getId().equals(scheduledTest.getId()))
        {
            System.out.println(" on schedule test "+ scheduledTest.getId() + " timer FINISHED ");
        }
    }
}

