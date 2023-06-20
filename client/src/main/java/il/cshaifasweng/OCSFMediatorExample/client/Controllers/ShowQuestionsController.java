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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShowQuestionsController {
    private String id = new String();
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
    private boolean isShow;
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

    private boolean isManager;
    private String managerId;

    @FXML
    private Button btnNew;
    @FXML
    TableView<Question> tableView;
    @FXML
    private Label labelX;
    @FXML
    private ListView<Question> question_list_view;

    @Subscribe
    public void onShowCourseQuestions(ShowCourseQuestionsEvent event){
        try {
            questions.clear();
            questions = event.getQuestions();

            ObservableList<Question> questions1 = FXCollections.observableArrayList(questions);
            Platform.runLater(()->{
                question_list_view.setItems(questions1);
            });

            question_list_view.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Question question, boolean empty) {
                    super.updateItem(question, empty);
                    if (empty || question == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Platform.runLater(() -> {
                            VBox vbox = new VBox();
                            Label questionText = new Label(question.getText());
                            vbox.getChildren().add(questionText);

                            // Add the answers as separate labels in the VBox

                            Label answerLabel0 = new Label("1.      " + question.getAnswer0());
                            if (question.getIndexAnswer() == 1) {
                                answerLabel0.setStyle("-fx-font-weight: bold; -fx-background-color: derive(greenyellow, 0%, 50%);");                        }
                            vbox.getChildren().add(answerLabel0);

                            Label answerLabel1 = new Label("2.      " + question.getAnswer1());
                            if (question.getIndexAnswer() == 2) {
                                answerLabel1.setStyle("-fx-font-weight: bold; -fx-background-color: derive(greenyellow, 0%, 50%);");                        }
                            vbox.getChildren().add(answerLabel1);

                            Label answerLabel2 = new Label("3.      " + question.getAnswer2());
                            if (question.getIndexAnswer() == 3) {
                                answerLabel2.setStyle("-fx-font-weight: bold; -fx-background-color: derive(greenyellow, 0%, 50%);");                        }
                            vbox.getChildren().add(answerLabel2);

                            Label answerLabel3 = new Label("4.      " + question.getAnswer3());
                            if (question.getIndexAnswer() == 4) {
                                answerLabel3.setStyle("-fx-font-weight: bold; -fx-background-color: derive(greenyellow, 0%, 50%);");                        }
                            vbox.getChildren().add(answerLabel3);

                            setGraphic(vbox);

                        });
                    }
                }
            });


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
        //isShow = false;
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
    @FXML
    void initialize() {
        Platform.runLater(() -> {
            comboCourse.setDisable(true);
            labelX.setVisible(false);
        });
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            if(isManager){
                info.add(managerId);
                info.add("manager");
            }
            else {
                info.add(id);
                info.add("teacher");
            }
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


    @Subscribe(threadMode = ThreadMode.MAIN )
    @FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        isManager = false;
        btnNew.setDisable(false);
        btnNew.setVisible(true);
        Platform.runLater(()->{
            setId(event.getId());
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects",id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Subscribe
    public void onMoveManagerIdEvent(MoveManagerIdEvent event){
        isManager = true;
        managerId = event.getId();
        btnNew.setDisable(true);
        btnNew.setVisible(false);
        Platform.runLater(()->{
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getAllSubjects",""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Subscribe
    public void onShowAllSubjectsEvent(ShowAllSubjectsEvent event){
        subjects.clear();
        subjects.addAll(event.getSubjects());
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Subject subject : subjects) {
            items.add(subject.getName());
        }
        comboSubject.setItems(items);
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
        Platform.runLater(() -> {
            labelX.setVisible(false);
        });

        //if (isShow) {
            List<Question> questionList = new ArrayList<>();
            EventBus.getDefault().post(new ShowCourseQuestionsEvent(questionList));
            isShow = false;
       // }
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
        Platform.runLater(()->{
            comboCourse.setItems(items);
        });
    }
    @FXML
    public void onSelectCourse(ActionEvent event){
       // isShow=true;
        try {
            labelX.setVisible(true);
            String courseName = comboCourse.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestions", courseName));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        cleanup();
        if (!isManager) {
            try {
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", id));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void GoToAddQuestion(ActionEvent event) {
        try {
            cleanup();
            App.switchScreen("createQuestion");
            Platform.runLater(() -> {
                try {
                    String teacherId = this.id;
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleRowClick(MouseEvent event) {
        if (!isManager) {
            try {
                if (event.getClickCount() == 2) { // Check if the user double-clicked the row

                    Question selectedQuestion = question_list_view.getSelectionModel().getSelectedItem();
                    if (selectedQuestion != null) {

                        List<Object> setTeacherAndQuestion = new ArrayList<>();
                        setTeacherAndQuestion.add(id);
                        setTeacherAndQuestion.add(selectedQuestion);
                        cleanup();
                        App.switchScreen("createQuestion");
                        Platform.runLater(() -> {
                            try {
                                Platform.runLater(() -> {
                                    try {
                                        SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestionToUpdate",setTeacherAndQuestion));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onMoveIdQuestionAddedEvent(MoveIdQuestionAddedEvent event){
        Platform.runLater(()->{
            setId(event.getTeacherId());
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects",id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Platform.runLater(()->{
            displayMsg(String.valueOf(event.getQuestId()));
        });
    }

    public void displayMsg(String questId) {
        Platform.runLater(() -> {
            if (!Objects.equals(questId, "0")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION); ////
                alert.setTitle("Question added successfully");
                alert.setHeaderText("Success!");
                alert.setContentText("Question added. Question ID: " + questId);
                alert.show();
          }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR); ////
                alert.setTitle("Question did not add...");
                alert.setContentText("There was a problem and the question was not saved. Please enter it again");
                alert.setHeaderText("Error");
                alert.show();
            }
        });
    }

    public void handleLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); ////
        alert.setTitle("LOGOUT");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            ArrayList<String> info = new ArrayList<>();
            if(isManager){
                info.add(managerId);
                info.add("manager");
            }
            else {
                info.add(id);
                info.add("teacher");
            }
            SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        } else {
            alert.close();
        }
    }


    public void handleBackButtonClick(ActionEvent actionEvent) throws IOException {
        handleGoHomeButtonClick(null);
    }
}
