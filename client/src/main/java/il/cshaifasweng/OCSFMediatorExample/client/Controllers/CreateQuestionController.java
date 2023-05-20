package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.QuestionAddedEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowSubjectCoursesEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowTeacherSubjectsEvent;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateQuestionController {
    @FXML
    private ComboBox<String> comboSubject;
    @FXML
    private TextField theQuestion;
    @FXML
    private TextField ans1;
    @FXML
    private TextField ans2;
    @FXML
    private TextField ans3;
    @FXML
    private TextField ans4;
    @FXML
    private ComboBox<String> comboAns;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;
    @FXML
    private ListView<String> courseOptions;
    private List<String> courseNames;
    private List<Course> courses;
    private List<Subject> subjects;

    public CreateQuestionController() {
        courseNames = new ArrayList<>();
        courses = new ArrayList<>();
        subjects = new ArrayList<>();
    }
    @FXML
    void initialize() {
        confirm.setDisable(true);
        courseOptions.setDisable(true);
        SimpleClient.getClient().sendToServer((new CustomMessage("#getSubjects", TeaacherID)));
        courseOptions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        courseOptions.getSelectionModel().selectedItemProperty().addListener(this::selectCourseListener);
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
            confirm.setDisable(false);
            courseOptions.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onShowSubjectCourses(ShowSubjectCoursesEvent event){
        courses.clear();
        courses.addAll(event.getCourses());
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Course course: courses){
            items.add(course.getName());
        }
        courseOptions.setItems(items);
    }

    @Subscribe
    public void onQuestionAddedEvent(QuestionAddedEvent event){
        try{
            App.switchScreen("primary");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void selectCourseListener(ObservableValue<? extends String> ov, String oldVal, String newVal){
        ObservableList<String> selectedItems = courseOptions.getSelectionModel().getSelectedItems();
        courseNames.clear();
        courseNames.addAll(selectedItems);
    }


        @FXML
       public void handleConfirmButtonClick(ActionEvent event){
        if (theQuestion.getText().isEmpty() || ans1.getText().isEmpty() || ans2.getText().isEmpty() || ans3.getText().isEmpty()
                || ans4.getText().isEmpty() || comboAns.getSelectionModel().isEmpty() || courseNames.isEmpty()){

            JOptionPane.showMessageDialog(null, "Error! Fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
            confirm();
        }

        public void confirm(){
            String ans_str = comboAns.getValue();
            int ans_num = Integer.parseInt(ans_str);
            Question myQuestion = new Question(theQuestion.getText(), ans1.getText(), ans2.getText(), ans3.getText(), ans4.getText(), ans_num);
            //myQuestion.setSubject(); //TODO להוסיף אובייקט מקצוע לשאלה
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#addQuestion", myQuestion));
            } catch (IOException e) {
                 e.printStackTrace();
            }
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
    void handleCancelButtonClick(ActionEvent event) {
        try {
            App.switchScreen("primary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}