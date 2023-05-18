package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowOneStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class ShowOneStudentController {

    @FXML
    private TableView<StudentTest> GradesTable;

    @FXML
    private TableColumn<StudentTest, String> TableCourse;

    @FXML
    private TableColumn<StudentTest, Integer> TableGrade;

    @FXML
    private TableColumn<StudentTest, String> TableSubject;

    @FXML
    private TableColumn<StudentTest, String> TableTeacher;

    @FXML
    private TableColumn<StudentTest, String> TableTestID;
    @FXML
    private Label student_id;
    @FXML
    private Label student_name;
    @FXML
    private Label statusLB;
    private List<StudentTest> studentTests;

    public List<StudentTest> getStudentTests() {
        return studentTests;
    }

    public void setStudentTests(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }
    public ShowOneStudentController(){
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    @FXML
    public void onShowOneStudentEvent(ShowOneStudentEvent event){
        try{
            setStudentTests(event.getStudentTests());
            if(studentTests!=null){
                Student student = studentTests.get(0).getStudent();
                Platform.runLater(()->{
                    statusLB.setText(statusLB.getText() + student.getId());
                    student_id.setText(student_id.getText() + student.getId());
                    student_name.setText(student_name.getText() + student.getFirst_name() + " " + student.getLast_name());
                });

            }
            TableGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
            TableCourse.setCellValueFactory(cellData -> {
                StudentTest test = cellData.getValue();
                String courseName = test.getCourseName();
                return new SimpleStringProperty(courseName);
            });
            TableSubject.setCellValueFactory(cellData -> {
                StudentTest test = cellData.getValue();
                String subjectName = test.getSubjectName();
                return new SimpleStringProperty(subjectName);
            });
            TableTeacher.setCellValueFactory(cellData -> {
                StudentTest test = cellData.getValue();
                String teacherName = test.getTeacherName();
                return new SimpleStringProperty(teacherName);
            });
            TableTestID.setCellValueFactory(cellData -> {
                StudentTest test = cellData.getValue();
                String examFormCode = test.getExamFormCode();
                return new SimpleStringProperty(examFormCode);
            });
            ObservableList<StudentTest> allStudentTests = FXCollections.observableList(studentTests);
            GradesTable.setItems(allStudentTests);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML void initialize(){
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
        try {
            if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                StudentTest selectedStudentTest = GradesTable.getSelectionModel().getSelectedItem();
                if (selectedStudentTest != null) {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTest",selectedStudentTest));
                    App.switchScreen("showUpdateStudent"); //TODO create an fxml with the same name
                    cleanup();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event){
        try{
            SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents",""));
            App.switchScreen("allStudents");
            cleanup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void handleGoHomeButtonClick(ActionEvent event){
        try{
            App.switchScreen("primary");
            cleanup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void handleBackButtonClick(ActionEvent event) {
        try{
            SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents",""));
            App.switchScreen("allStudents");
            cleanup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
