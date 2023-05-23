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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private TableColumn<ExamForm, Course> TableCourse;

    @FXML
    private TableColumn<ExamForm, String> TableQuestionsNum;

    @FXML
    private TableColumn<ExamForm, String> TableTeacher;

    @FXML
    private TableColumn<ExamForm, String> TableTestID;

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

    private Subject sub;
    private String id ;
    private static int instances = 0;
    private Course cour;

    public ShowExamFormsController(){

        EventBus.getDefault().register(this);
        instances++;
        System.out.println("in exam forms "+instances);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
        instances--;
        System.out.println("in exam forms "+instances);
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
        System.out.println("before platform");
        Platform.runLater(()->{
            System.out.println("in platform");
            setId(event.getId());
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects",id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
@Subscribe(threadMode = ThreadMode.MAIN)
@FXML
    public void onShowTeacherSubjects(ShowTeacherSubjectsEvent event){
        System.out.println("on show subjects event in show examForms");
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
        String courseName = ComboCourse.getValue();
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
        List<ExamForm> examForms = event.getExamForms();

        TableTestID.setCellValueFactory(cellData -> {
            ExamForm examForm = cellData.getValue();
            String code = examForm.getExamFormCode();
            return new SimpleStringProperty(code);
        });

//        TableTeacher.setCellValueFactory(cellData -> {
//            String teacherName = "no teacher yet";
//            ExamForm examForm = cellData.getValue();
//            Teacher teacher = examForm.getTeacher()!=null?examForm.getTeacher():;
//            if(teacher!=null) {
//                teacherName = teacher.getFirst_name() + " " + teacher.getLast_name(); // TODO change the entities: teacher and exam form
//            }
//            return new SimpleStringProperty(teacherName);
//        });
//        TableQuestionsNum.setCellValueFactory(cellData -> {
//            ExamForm examForm = cellData.getValue();
//            String num =  Integer.toString(examForm.getQuestionScores().size()); // TODO change the entities: teacher and exam form
//            return new SimpleStringProperty(num);
//        });

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
                    List<Object> setTeacherAndExam = new ArrayList<>();
                    setTeacherAndExam.add(id);
                    setTeacherAndExam.add(selectedExam);
                    App.switchScreen("createExamForm");
                    Platform.runLater(()->{
                        try {
                            EventBus.getDefault().post(new ShowUpdateExamFormEvent(setTeacherAndExam));
                            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



@FXML
    public void handleAddExamForm(ActionEvent event) {
        try {
            String teacherId = this.id;
            System.out.println("btn to add exam form "+ teacherId);
            App.switchScreen("createExamForm");
            Platform.runLater(()->{
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", teacherId));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
@FXML
    public void handleGoHomeButtonClick(ActionEvent event) {
        try{
            String teacherId = this.id;
            System.out.println("go home "+teacherId);
            App.switchScreen("teacherHome");
            Platform.runLater(()->{
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome",teacherId));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
