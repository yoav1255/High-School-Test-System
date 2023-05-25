package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.QuestionScore;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
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

    private List<ToggleGroup> toggleGroups = new ArrayList<>();
    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private ListView<QuestionScore> questionsListView;
    private String id;
    private ScheduledTest scheduledTest;
    private StudentTest studentTest;
    private List<QuestionScore> questionScoreList;


    public StudentExecuteExamController() {
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) {
        id = event.getId();
        Platform.runLater(() -> {
            text_Id.setText(text_Id.getText() + id);
        });
    }

    @Subscribe
    public void onSelectedTestEvent(SelectedTestEvent event) {
        scheduledTest = event.getSelectedTestEvent();
        questionScoreList = scheduledTest.getExamForm().getQuestionScores();
        ObservableList<QuestionScore> questionScoreObservableList = FXCollections.observableArrayList(questionScoreList);
        questionsListView.setItems(questionScoreObservableList);

        questionsListView.setCellFactory(param -> new ListCell<QuestionScore>() {

            @Override
            protected void updateItem(QuestionScore questionScore, boolean empty) {

                super.updateItem(questionScore, empty);

                if (empty || questionScore == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String questionText = questionScore.getQuestion().getText();
                    String answer0 = questionScore.getQuestion().getAnswer0();
                    String answer1 = questionScore.getQuestion().getAnswer1();
                    String answer2 = questionScore.getQuestion().getAnswer2();
                    String answer3 = questionScore.getQuestion().getAnswer3();
                    int score = questionScore.getScore();

                    //Platform.runLater(()->{
                        VBox vbox = new VBox();
                        vbox.setSpacing(10);

                        Label questionLabel = new Label("Question:      " + questionText);
                        vbox.getChildren().add(questionLabel);

                        toggleGroup = new ToggleGroup();

                        RadioButton answer1RadioButton = new RadioButton("1.    "+answer0);
                        answer1RadioButton.setToggleGroup(toggleGroup);
                        vbox.getChildren().add(answer1RadioButton);

                        RadioButton answer2RadioButton = new RadioButton("2.     "+answer1);
                        answer2RadioButton.setToggleGroup(toggleGroup);
                        vbox.getChildren().add(answer2RadioButton);

                        RadioButton answer3RadioButton = new RadioButton("3.     "+answer2);
                        answer3RadioButton.setToggleGroup(toggleGroup);
                        vbox.getChildren().add(answer3RadioButton);

                        RadioButton answer4RadioButton = new RadioButton("4.     "+answer3);
                        answer4RadioButton.setToggleGroup(toggleGroup);
                        vbox.getChildren().add(answer4RadioButton);

                        Label scoreLabel = new Label("Grade: " + score);
                        vbox.getChildren().add(scoreLabel);

                        setGraphic(vbox);
                        toggleGroups.add(toggleGroup);
                    //});

                }
            }
        });


    }

    @FXML
    public void submitTestBtn(ActionEvent event) {
        ObservableList<QuestionScore> questionScores = questionsListView.getItems();
        int i=0;
        for (QuestionScore questionScore : questionScores) {
            RadioButton selectedRadioButton = (RadioButton) toggleGroups.get(i).getSelectedToggle();
            if (selectedRadioButton != null) {
                String selectedAnswer = selectedRadioButton.getText();
                // Here you can process the selected answer for each question
                // For example, you can store it in a data structure or perform some action based on the answer
                System.out.println("Question: " + questionScore.getQuestion().getText());
                System.out.println("Selected Answer: " + selectedAnswer);
            }
            i++;
        }
    }
}

