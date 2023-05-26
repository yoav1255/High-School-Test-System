package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.SelectedStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.SelectedTestEvent;
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

import java.io.IOException;
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
        student = event.getStudent();
        System.out.println("in event: "+student.getFirst_name());
        studentTest = new StudentTest();
        studentTest.setStudent(student);
    }

    @Subscribe
    public void onSelectedTestEvent(SelectedTestEvent event) {
        scheduledTest = event.getSelectedTestEvent();
        questionScoreList = scheduledTest.getExamForm().getQuestionScores();

        for (QuestionScore questionScore : questionScoreList) {
            Question_Answer questionAnswer = new Question_Answer();
            questionAnswer.setStudentTest(studentTest);
            System.out.println("check question score "+questionScore.getId());
            questionAnswer.setQuestion(questionScore);
            questionAnswer.setAnswer(-1); // Initialize with no answer selected

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
                    String questionText = questionAnswer.getQuestion().getQuestion().getText();
                    String answer0 = questionAnswer.getQuestion().getQuestion().getAnswer0();
                    String answer1 = questionAnswer.getQuestion().getQuestion().getAnswer1();
                    String answer2 = questionAnswer.getQuestion().getQuestion().getAnswer2();
                    String answer3 = questionAnswer.getQuestion().getQuestion().getAnswer3();

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

                    Label scoreLabel = new Label("Points: " + questionAnswer.getQuestion().getScore());
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
                            System.out.println("Question: " + questionAnswer.getQuestion().getQuestion().getText());
                            System.out.println("Selected Answer: " + selectedRadioButton.getText());
                        } else {
                            System.out.println("No answer selected for question: " + questionAnswer.getQuestion().getQuestion().getText());
                        }
                    });

                    //
                }
            }
        });
    }

    @FXML
    public void submitTestBtn(ActionEvent event) {
        studentTest.setScheduledTest(scheduledTest);
        studentTest.setQuestionAnswers(questionAnswers);
        // student test is ready
        //TODO add the grade for the test
        //TODO add timer and add timeToComplete field
    }
}

