package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowExamFormsController {
    @FXML
    private ComboBox<String> ComboCourse;

    @FXML
    private ComboBox<String> ComboSubject;

    @FXML
    private TableView<ExamForm> ExamForms_Table;

    @FXML
    private GridPane OneStudentGR;

    @FXML
    private TableColumn<ExamForm, String> TableCourse;

    @FXML
    private TableColumn<ExamForm, String> TableTimeLimit;

    @FXML
    private TableColumn<ExamForm, String> TableTeacher;

    @FXML
    private TableColumn<ExamForm, String> TableTestID;

    @FXML
    private Button backBN;
    @FXML
    private Button homeBN;
    @FXML
    private Button btnNewExamForm;

    private Subject sub;
    private String id ;
    private Course cour;
    private String courseName;
    private boolean isManager;
    private String managerId;

    public ShowExamFormsController(){

        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId(){
        return id;
    }

@Subscribe(threadMode = ThreadMode.MAIN )
@FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        isManager =false;

        Platform.runLater(()->{
            setId(event.getId());
            btnNewExamForm.setVisible(true);
            btnNewExamForm.setDisable(false);
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects",id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

@Subscribe
    public void onMoveManagerIdEvent(MoveManagerIdEvent event) {
        isManager =true;
        managerId = event.getId();

        Platform.runLater(()->{
            btnNewExamForm.setVisible(false);
            btnNewExamForm.setDisable(true);
            setId(event.getId());
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getAllSubjects",""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

@Subscribe(threadMode = ThreadMode.MAIN)
@FXML
    public void onShowTeacherSubjects(ShowTeacherSubjectsEvent event){
        List<Subject> subjects = event.getSubjects();
        ObservableList<String> items = FXCollections.observableArrayList();
        Platform.runLater(()->{
            ExamForms_Table.setDisable(true);
        });
    for(Subject subject:subjects){
            items.add(subject.getName());
        }
        ComboSubject.setItems(items);
    }

    @Subscribe
    public void onShowAllSubjectsEvent(ShowAllSubjectsEvent event){
        List<Subject> subjects = event.getSubjects();
        ObservableList<String> items = FXCollections.observableArrayList();
        Platform.runLater(()->{
            ExamForms_Table.setDisable(true);
        });
        for(Subject subject:subjects){
            items.add(subject.getName());
        }
        ComboSubject.setItems(items);
    }

@FXML
    public void onSelectSubject(ActionEvent event) {
        try {
            String subjectName = ComboSubject.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourses", subjectName));
            ComboCourse.setDisable(false);
            ComboCourse.setValue("");
            ExamForms_Table.setDisable(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

@Subscribe(threadMode = ThreadMode.MAIN)
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

@FXML
    public void onSelectCourse(ActionEvent event) {
    try {
        courseName = ComboCourse.getValue();
        SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseFromName",courseName));
        SimpleClient.getClient().sendToServer(new CustomMessage("#getExamFormCode",courseName));
        SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseExamForms",courseName));
        ExamForms_Table.setDisable(false);
    }catch (Exception e){
        e.printStackTrace();
    }
}

@Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowCourseEvent(ShowCourseEvent event){
        this.cour = event.getCourse();
    }
@Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowExamFormsEvent(ShowExamFormsEvent event){
    try {
        //TODO show more things
        List<ExamForm> examForms = event.getExamForms();

        TableTestID.setCellValueFactory(cellData -> {
            ExamForm examForm = cellData.getValue();
            String code = examForm.getExamFormCode();
            return new SimpleStringProperty(code);
        });
        TableTimeLimit.setCellValueFactory(cellData->{
            ExamForm examForm = cellData.getValue();
            String timeLimit = Integer.toString(examForm.getTimeLimit());
            return new SimpleStringProperty(timeLimit);
        });
        TableTeacher.setCellValueFactory(cellData->{
            ExamForm examForm = cellData.getValue();
            String teacherName = examForm.getTeacher().getFirst_name() + " " + examForm.getTeacher().getLast_name();
            return new SimpleStringProperty(teacherName);
        });
        TableCourse.setCellValueFactory(cellData->{
            return new SimpleStringProperty(courseName);
        });
        ObservableList<ExamForm> examForms1 = FXCollections.observableArrayList(examForms);
        ExamForms_Table.setItems(examForms1);
        ExamForms_Table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


    }catch (Exception e){
        e.printStackTrace();
    }
}


    @FXML
    void handleRowClick(MouseEvent event) {
            try {
                if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                    ExamForm selectedExam = ExamForms_Table.getSelectionModel().getSelectedItem();
                    if (selectedExam != null) {
                        cleanup();
                        List<Object> setObjectAndExam = new ArrayList<>();
                        if(!isManager)
                            setObjectAndExam.add(id);
                        else
                            setObjectAndExam.add(managerId);
                        setObjectAndExam.add(selectedExam);

                        App.switchScreen("showOneExamForm");
                        Platform.runLater(() -> {
                            try {
                                if(!isManager)
                                    EventBus.getDefault().post(new ShowOneExamFormEvent(setObjectAndExam));
                                else
                                    EventBus.getDefault().post(new ShowOneExamFormManagerEvent(setObjectAndExam));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

@FXML
    public void handleAddExamForm(ActionEvent event) {
    cleanup();
    if (!isManager) {
        try {
            String teacherId = this.id;
            App.switchScreen("createExamForm2");
            Platform.runLater(() -> {
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", teacherId));
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacher", teacherId));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public void goHome(){
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
    public void handleGoHomeButtonClick(ActionEvent event) {
        goHome();
    }

    @FXML
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

    public void handleBackButtonClick(ActionEvent actionEvent) {
        goHome();
    }
}
