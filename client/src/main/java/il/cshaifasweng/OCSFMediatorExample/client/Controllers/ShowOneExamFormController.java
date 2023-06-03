package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.Question_Score;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowExamFormQuestionScoresEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowOneExamFormEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowOneExamFormManagerEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class ShowOneExamFormController {

    @FXML
    private GridPane OneStudentGR;

    @FXML
    private Button backBN;

    @FXML
    private TextArea generalNotes;

    @FXML
    private Button homeBN;

    @FXML
    private Label labelMsg;

    @FXML
    private ListView<Question_Score> questionsListView;

    @FXML
    private Label statusLB;

    @FXML
    private Pane student_details_PN;

    @FXML
    private Label txtCourse;

    @FXML
    private Label txtSubject;

    @FXML
    private Label txtTimeLimit;

    private ExamForm examForm;
    private String teacherId;
    private String managerId;
    private boolean isManager;
    private List<Question_Score> questionScoreList;
    private List<Question> questionList;

    public ShowOneExamFormController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup(){
        EventBus.getDefault().unregister(this);
    }

    public void setFields(){
        txtCourse.setText(txtCourse.getText() + examForm.getCourseName());
        txtSubject.setText(txtSubject.getText()+examForm.getSubjectName());
        txtTimeLimit.setText(txtTimeLimit.getText() + Integer.toString(examForm.getTimeLimit()));
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestionScores",examForm));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
@Subscribe
    public void onShowOneExamFormEvent(ShowOneExamFormEvent event) throws IOException {
        examForm = (ExamForm) event.getSetTeacherAndExam().get(1);
        teacherId = (String) event.getSetTeacherAndExam().get(0);
        isManager = false;
        Platform.runLater(()-> setFields());
    }

    @Subscribe
    public void onShowOneExamFormManagerEvent(ShowOneExamFormManagerEvent event){
        examForm = (ExamForm) event.getSetManagerAndExam().get(1);
        managerId = (String) event.getSetManagerAndExam().get(0);
        isManager = true;

        Platform.runLater(()-> setFields());
    }

@Subscribe
    public void onShowExamFormQuestionScoresEvent(ShowExamFormQuestionScoresEvent event){
    try {

        questionScoreList = event.getQuestionScores();

        ObservableList<Question_Score> questions1 = FXCollections.observableArrayList(questionScoreList);
        questionsListView.setItems(questions1);

        questionsListView.setCellFactory(param -> new ListCell<Question_Score>() {
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
            generalNotes.setText(examForm.getGeneralNotes());
            generalNotes.setDisable(true);
        });

    }catch (Exception e){
        e.printStackTrace();
    }
}

    @FXML
    void handleBackButtonClick(ActionEvent event) {

    }

    @FXML
    public void handleGoHomeButtonClick(ActionEvent event) {
        if (!isManager) {
            try {
                String teacherId = this.teacherId;
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", teacherId));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                App.switchScreen("managerHome");
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#managerHome", managerId));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
