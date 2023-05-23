package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Course;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.Subject;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowCourseQuestionsEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowSubjectCoursesEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowTeacherSubjectsEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ShowQuestionsController {
    private static String id="hi";
    public String getId() {return id;}
    public void setId(String id) {
        this.id = id;
    }

    @FXML
    private Button homeBN;
    @FXML
    private ComboBox<String> comboSubject;
    @FXML
    private ComboBox<String> comboCourse;
    private List<Subject> subjects;
    private List<String> courseNames;
    private List<Course> courses;
    private List<Question> questions;
    private List<String> questIDs;
    private List<String> questTexts;
    private List<String> ans1;
    private List<String> ans2;
    private List<String> ans3;
    private List<String> ans4;
    @FXML
    TableView<Question> tableView;
    @FXML
    private TableColumn<Question, Integer> tableQuestID;
    @FXML
    private TableColumn<Question, String> tableText;
    @FXML
    private TableColumn<Question, String> tableAns1;
    @FXML
    private TableColumn<Question, String> tableAns2;
    @FXML
    private TableColumn<Question, String> tableAns3;
    @FXML
    private TableColumn<Question, String> tableAns4;


    @Subscribe
    public void onShowCourseQuestions(ShowCourseQuestionsEvent event){
        try {
            questions.clear();
            questions = event.getQuestions();
            tableQuestID.setCellValueFactory(new PropertyValueFactory<>("id"));
            tableText.setCellValueFactory(new PropertyValueFactory<>("text"));
            tableAns1.setCellValueFactory(new PropertyValueFactory<>("answer0"));
            tableAns2.setCellValueFactory(new PropertyValueFactory<>("answer1"));
            tableAns3.setCellValueFactory(new PropertyValueFactory<>("answer2"));
            tableAns4.setCellValueFactory(new PropertyValueFactory<>("answer3"));

            ObservableList<Question> questions1 = FXCollections.observableArrayList(questions);
            tableView.setItems(questions1);
            tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ShowQuestionsController() {
        EventBus.getDefault().register(this);
        courseNames = new ArrayList<>(); // The selected courses names.
        courses = new ArrayList<>(); // The courses that are in the selected subject.
        subjects = new ArrayList<>(); // The subjects the teacher teach.
        questions = new ArrayList<>(); // The questions that are in the selected course.

        questIDs = new ArrayList<>();
        questTexts = new ArrayList<>();
        ans1 = new ArrayList<>();
        ans2 = new ArrayList<>();
        ans3 = new ArrayList<>();
        ans4 = new ArrayList<>();



    }

    @FXML
    void initialize() {
        Platform.runLater(() -> {
            comboCourse.setDisable(true);

        });

    }

    @Subscribe
    public void onShowTeacherSubjects(ShowTeacherSubjectsEvent event) {
        subjects.clear();
        subjects.addAll(event.getSubjects());
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Subject subject : subjects) {
            items.add(subject.getName());
        }
        comboSubject.setItems(items);
    }

    @FXML
    public void onSelectSubject(ActionEvent event) {
        try {
            String subjectName = comboSubject.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourses", subjectName));
            comboCourse.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onShowSubjectCourses(ShowSubjectCoursesEvent event) {
        courses.clear();
        courses.addAll(event.getCourses());
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Course course : courses) {
            items.add(course.getName());
        }
        comboCourse.setItems(items);
    }

    @FXML
    public void onSelectCourse(ActionEvent event){
        try {
            String courseName = comboCourse.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestions", courseName));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void handleHomeButtonClick(ActionEvent event){
        try {
            App.switchScreen("teacherHome");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        System.out.println("on event "+event.getId());
        setId(event.getId());
        System.out.println("check 2 " +id);
    }


    @FXML
    public void GoToAddQuestion(ActionEvent event) {
    Platform.runLater(()->{
        try {
            System.out.println("btn to add Question form "+ id);
            App.switchScreen("createQuestion");
            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", this.id));
            // TODO : send online teacher's id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    }


}
