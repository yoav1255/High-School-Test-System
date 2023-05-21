package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.QuestionAddedEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowSubjectCoursesEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowTeacherSubjectsEvent;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private Button cancelBN;
    @FXML
    private Button confirmBN;
    @FXML
    private ListView<String> courseOptions;
    private List<String> courseNames;
    private List<Course> courses;
    private List<Subject> subjects;

    public CreateQuestionController() {
        EventBus.getDefault().register(this);
        courseNames = new ArrayList<>(); // The selected courses names.
        courses = new ArrayList<>(); // The courses that are in the selected subject.
        subjects = new ArrayList<>(); // The subjects the teacher teach.
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize() {
        Platform.runLater(() -> {
            courseOptions.setVisible(false);
            courseOptions.getItems().clear();
        });

        confirmBN.setDisable(true);
        courseOptions.setDisable(true);
        comboAns.getItems().add("1");
        comboAns.getItems().add("2");
        comboAns.getItems().add("3");
        comboAns.getItems().add("4");

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
            courseOptions.setVisible(true);
            String subjectName = comboSubject.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourses", subjectName));
            confirmBN.setDisable(false);
            courseOptions.setDisable(false);
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
        courseOptions.setItems(items);
    }

    public void selectCourseListener(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        ObservableList<String> selectedItems = courseOptions.getSelectionModel().getSelectedItems();
        courseNames.clear();
        courseNames.addAll(selectedItems);
    }

    @FXML
    public void handleConfirmButtonClick(ActionEvent event) {
        if (theQuestion.getText().isEmpty() || ans1.getText().isEmpty() || ans2.getText().isEmpty() || ans3.getText().isEmpty()
                || ans4.getText().isEmpty() || comboAns.getSelectionModel().isEmpty() || courseNames.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Error! Fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
        } else
            confirm();
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {
        int input = JOptionPane.showConfirmDialog(null, "Your changes will be lost. Do you wand to proceed?", "Select an Option...",
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (input == JOptionPane.YES_OPTION) {
            try {
                App.switchScreen("primary");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleCancelButtonClick(ActionEvent event) {
        int input = JOptionPane.showConfirmDialog(null, "Your changes will be lost. Do you wand to proceed?", "Select an Option...",
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (input == JOptionPane.YES_OPTION) {
            try {
             App.switchScreen("primary");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void confirm() {
        String ans_str = comboAns.getValue();
        int ans_num = Integer.parseInt(ans_str);
        Question myQuestion = new Question(theQuestion.getText(), ans1.getText(), ans2.getText(), ans3.getText(), ans4.getText(), ans_num);

        List<Course> filteredCourses = courses.stream()
                .filter(course -> courseNames.contains(course.getName()))
                .collect(Collectors.toList());

        myQuestion.setCourses(filteredCourses);

        Subject selectedSubject = null;
        String selectedName = comboSubject.getValue();

        for (Subject subject : subjects) {
            if (subject.getName().equals(selectedName)) {
                selectedSubject = subject;
            }
        }
        myQuestion.setSubject(selectedSubject);

        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#addQuestion", myQuestion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onQuestionAddedEvent(QuestionAddedEvent event) {
        System.out.println("createquest - QuestionAddedEvent");
        try {
            App.switchScreen("primary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}