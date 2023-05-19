package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowCourseQuestionsEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowSubjectCoursesEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowTeacherSubjectsEvent;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTestController {

    @FXML
    private ComboBox<String> ComboSubject;
    @FXML
    private ComboBox<String> ComboCourse;
    @FXML
    private GridPane OneStudentGR;

    @FXML
    private TableView<Question> Table_Questions;

    @FXML
    private Button allStudentsBN;
    @FXML
    private TableColumn<Question, Boolean> checkQuestion;
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
    private Button backBN;

    @FXML
    private Pane grade_upgrade_info;

    @FXML
    private Button homeBN;

    @FXML
    private Label statusLB;

    @FXML
    private Pane student_details_PN;

    @FXML
    private TextField timeLimit;
    private List<Question> questionsLeft;
    private List<Question> questionsChosen;


    public CreateTestController(){ EventBus.getDefault().register(this); }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize(){
        ComboCourse.setDisable(true);
        Table_Questions.setDisable(true);
        questionsChosen = new ArrayList<>();
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
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Course course:courses){
            items.add(course.getName());
        }
        ComboCourse.setItems(items);
    }
    public void onSelectCourse(ActionEvent event) {
        try {
            String courseName = ComboCourse.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestions", courseName));
            Table_Questions.setDisable(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onShowCourseQuestions(ShowCourseQuestionsEvent event){
        List<Question> questions = event.getQuestions();
        this.questionsLeft = questions;
//        TableColumn<Question, Boolean> checkBoxColumn = new TableColumn<>("Select");
//        checkBoxColumn.setCellValueFactory(cellData-> new SimpleBooleanProperty(false).asObject());
//        checkBoxColumn.setCellFactory(column-> new CheckBoxTableCell<>());
//        Table_Questions.getColumns().add(0,checkBoxColumn);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        text.setCellValueFactory(new PropertyValueFactory<>("text"));
        ans0.setCellValueFactory(new PropertyValueFactory<>("answer0"));
        ans1.setCellValueFactory(new PropertyValueFactory<>("answer1"));
        ans2.setCellValueFactory(new PropertyValueFactory<>("answer2"));
        ans3.setCellValueFactory(new PropertyValueFactory<>("answer3"));
        ansIndex.setCellValueFactory(new PropertyValueFactory<>("indexAnswer"));
        ObservableList<Question> questions1 = FXCollections.observableArrayList(questions);
        Table_Questions.setItems(questions1);
    }






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


    public void handleRowClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2){
            Question selectedQuestion = Table_Questions.getSelectionModel().getSelectedItem();

        }
    }
}
