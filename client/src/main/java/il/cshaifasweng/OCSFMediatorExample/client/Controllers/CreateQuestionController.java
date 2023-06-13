package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class CreateQuestionController {
    @FXML
    private ComboBox<String> comboSubject;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
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
    private String id;
    public String getId() {return id;}
    public void setId(String id) {
        this.id = id;
    }
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
            label1.setVisible(false);
            label2.setVisible(false);
            courseOptions.getItems().clear();

            confirmBN.setDisable(true);
            courseOptions.setDisable(true);
            comboAns.getItems().add("1");
            comboAns.getItems().add("2");
            comboAns.getItems().add("3");
            comboAns.getItems().add("4");

            courseOptions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            courseOptions.getSelectionModel().selectedItemProperty().addListener(this::selectCourseListener);

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
    public void onShowTeacherSubjects(ShowTeacherSubjectsEvent event) {
        subjects.clear();
        subjects.addAll(event.getSubjects());
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Subject subject : subjects) {
            items.add(subject.getName());
        }
        Platform.runLater(() -> {
            comboSubject.setItems(items);
        });

    }
    @FXML
    public void onSelectSubject(ActionEvent event) {
        try {
            label1.setVisible(true);
            label2.setVisible(true);
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
        //TODO לתקן את הבחירה המרובה של הקורסים
    }
    @FXML
    public void handleConfirmButtonClick(ActionEvent event) {
        if (theQuestion.getText().isEmpty() || ans1.getText().isEmpty() || ans2.getText().isEmpty() || ans3.getText().isEmpty()
                || ans4.getText().isEmpty() || comboAns.getSelectionModel().isEmpty() || courseNames.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error! Fill all the fields");
            alert.show();
        } else
            confirm();
    }
    @FXML
    void handleCancelButtonClick(ActionEvent event) {
        if (theQuestion.getText().isEmpty() && ans1.getText().isEmpty() && ans2.getText().isEmpty()
                && ans3.getText().isEmpty() && ans4.getText().isEmpty()) {
            try {
                String teacherId = this.id;
                cleanup();
                App.switchScreen("showAllQuestions");
                Platform.runLater(() -> {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Your changes will be lost. Do you wand to proceed?");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String teacherId = this.id;
                    cleanup();
                    App.switchScreen("showAllQuestions");
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {

        if (theQuestion.getText().isEmpty() && ans1.getText().isEmpty() && ans2.getText().isEmpty()
                && ans3.getText().isEmpty() && ans4.getText().isEmpty()) {
            try {
                String teacherId = this.id;
                cleanup();
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Your changes will be lost. Do you wand to proceed?");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String teacherId = this.id;
                    cleanup();
                    App.switchScreen("showAllQuestions");
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        List<Object> objectList = event.getObjectList();
        try {
            String teacherId = this.id;
            String questId = String.valueOf(objectList.get(1));
            if (!(boolean)objectList.get(0)) {
                questId = "0";
            }
            cleanup();
            App.switchScreen("showAllQuestions");
            final String finalQuestId = questId;
            Platform.runLater(() -> {
                EventBus.getDefault().post(new MoveIdQuestionAddedEvent(teacherId, finalQuestId));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        setId(event.getId());
        try {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", this.id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onShowUpdateQuestFormEvent(ShowUpdateQuestFormEvent event){
        List<Object> setTeacherAndQuest = event.getSetTeacherAndQuest();
        Platform.runLater(()->{
            try {
                id = (String)setTeacherAndQuest.get(0);
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Platform.runLater(()->{
        Question updateQuestion = (Question)setTeacherAndQuest.get(1);
        theQuestion.setText(updateQuestion.getText());
        ans1.setText(updateQuestion.getAnswer0());
        ans2.setText(updateQuestion.getAnswer1());
        ans3.setText(updateQuestion.getAnswer2());
        ans4.setText(updateQuestion.getAnswer3());

        Subject selectedSub = updateQuestion.getSubject();

        comboSubject.setValue(selectedSub.getName());
        courseOptions.setVisible(true);
        comboAns.setValue(String.valueOf((updateQuestion.getIndexAnswer())));
        label1.setVisible(true);
        label2.setVisible(true);

        });

        //   comboAns.setItems(updateQuestion.getIndexAnswer());

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

}