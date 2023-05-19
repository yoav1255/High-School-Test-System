package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateTestController {

    @FXML
    private Label labelMsg;
    @FXML
    private ComboBox<String> ComboSubject;
    @FXML
    private ComboBox<String> ComboCourse;
    @FXML
    private TableView<Question> Table_Questions;

    @FXML
    private TableColumn<Question, Integer> id;
    @FXML
    private TableColumn<Question, String> text;
    @FXML
    private TableColumn<Question, String> ans0;
    @FXML
    private TableColumn<Question, String > ans1;

    @FXML
    private TableColumn<Question, String > ans2;

    @FXML
    private TableColumn<Question, String> ans3;
    @FXML
    private TableColumn<Question, Integer> ansIndex;
    @FXML
    private TableView<QuestionScore> Table_Chosen;
    @FXML
    private TableColumn<QuestionScore, String> qText;
    @FXML
    private TableColumn<QuestionScore, Integer> score;
    @FXML
    private TableColumn<QuestionScore, Integer> qId;
    @FXML
    private TextField scoreTextField;
    @FXML
    private Button addButton;
    @FXML
    private TextField timeLimit;
    private List<QuestionScore> questionScoreList;
    private String ExamCode;
    private Subject sub;
    private Course cour;


    public CreateTestController(){ EventBus.getDefault().register(this); }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize(){
        ComboCourse.setDisable(true);
        Table_Questions.setDisable(true);
        questionScoreList = new ArrayList<>();
        System.out.println("intialized");
    }
    @Subscribe
    public void onShowTeacherSubjects(ShowTeacherSubjectsEvent event){
        List<Subject> subjects = event.getSubjects();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Subject subject:subjects){
            items.add(subject.getName());
        }
        ComboSubject.setItems(items);
    }

    public void onSelectSubject(ActionEvent event) {
        try {
            String subjectName = ComboSubject.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourses", subjectName));
            ComboCourse.setDisable(false);
            Table_Questions.setDisable(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onShowSubjectCourses(ShowSubjectCoursesEvent event){
        List<Course> courses = event.getCourses();
        if(!courses.isEmpty())
            sub = courses.get(0).getSubject();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Course course:courses){
            items.add(course.getName());
        }
        ComboCourse.setItems(items);
    }
    public void onSelectCourse(ActionEvent event) {
        try {
            String courseName = ComboCourse.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseFromName",courseName));
            SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestions", courseName));
            SimpleClient.getClient().sendToServer(new CustomMessage("#getExamFormCode",courseName));
            Table_Questions.setDisable(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onShowCourseEvent(ShowCourseEvent event){
        this.cour = event.getCourse();
    }
    @Subscribe
    public void onShowCourseQuestions(ShowCourseQuestionsEvent event){
        try {
            List<Question> questions = event.getQuestions();

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            text.setCellValueFactory(new PropertyValueFactory<>("text"));
            ans0.setCellValueFactory(new PropertyValueFactory<>("answer0"));
            ans1.setCellValueFactory(new PropertyValueFactory<>("answer1"));
            ans2.setCellValueFactory(new PropertyValueFactory<>("answer2"));
            ans3.setCellValueFactory(new PropertyValueFactory<>("answer3"));
            ansIndex.setCellValueFactory(new PropertyValueFactory<>("indexAnswer"));

            ObservableList<Question> questions1 = FXCollections.observableArrayList(questions);
            Table_Questions.setItems(questions1);
            Table_Questions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addScore(ActionEvent event) {
        try {
            Platform.runLater(()->{
                Question selectedQuestion = Table_Questions.getSelectionModel().getSelectedItem();
                if (selectedQuestion != null) {
                    String scoreText = scoreTextField.getText();
                    if (!scoreText.isEmpty()) {
                        int score = Integer.parseInt(scoreText);
                        QuestionScore qs = new QuestionScore(score);
                        qs.setQuestion(selectedQuestion);
                        questionScoreList.add(qs);
                        scoreTextField.clear();
                    }
                }
                qId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuestion().getId()));
                qText.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getQuestion().getText()));
                score.setCellValueFactory(new PropertyValueFactory<>("score"));

                ObservableList<QuestionScore> questionScores = FXCollections.observableArrayList(questionScoreList);
                Table_Chosen.setItems(questionScores);
                Table_Chosen.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void submitForm(ActionEvent event) {
        try {
            int timeLim = Integer.parseInt(timeLimit.getText());
            ExamCode = Integer.toString(cour.getCode()) + Integer.toString(sub.getCode()) + "45";
            System.out.println(ExamCode);
            ExamForm examForm = new ExamForm(ExamCode, timeLim);
            examForm.setQuestionScores(questionScoreList);
            examForm.setSubject(sub);
            examForm.setCourse(cour);
            SimpleClient.getClient().sendToServer(new CustomMessage("#addExamForm", examForm));
            labelMsg.setVisible(true);
            labelMsg.setText("SUCCESS");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    @Subscribe
//    public void onSuccessEvent(ShowSuccessEvent event){
//        String msg = event.getMsg();
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success");
//        alert.setHeaderText(null);
//        alert.setContentText(msg);
//        alert.showAndWait();
//    }


    @FXML
    void handleBackButtonClick(ActionEvent event) {
        //TODO func
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {
        try{
            App.switchScreen("primary");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {
        try{
            SimpleClient.getClient().sendToServer("#showAllStudents");
            App.switchScreen("allStudents");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
