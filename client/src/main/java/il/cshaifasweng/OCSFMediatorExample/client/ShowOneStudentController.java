package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
            System.out.println("in show One Student controller");
            setStudentTests(event.getStudentTests());
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

    @FXML
    public void handleRowClick(MouseEvent event) {
        try {
            if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                StudentTest selectedStudentTest = GradesTable.getSelectionModel().getSelectedItem();
                if (selectedStudentTest != null) {
                    SimpleClient.getClient().sendToServer(selectedStudentTest);
                    App.switchScreen("showUpdateStudent"); //TODO create an fxml with the same name
                    cleanup();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
