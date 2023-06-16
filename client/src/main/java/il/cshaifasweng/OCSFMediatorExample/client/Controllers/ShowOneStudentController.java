package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveManagerIdEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowOneStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveObjectToNextPageEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowOneStudentController {
    @FXML
    private Button HomeButton;

    @FXML
    private TableView<StudentTest> GradesTable;

    @FXML
    private TableColumn<StudentTest, String> TableCourse;

    @FXML
    private TableColumn<StudentTest, String > TableGrade;

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
    private Student student;
    private boolean isManager;
    private String managerId;
    private String studentId;

    //--------------------- manager is on this page(from all students) ------------------------//

    //--------------------- student is on this page(from student home) ------------------------//

    public List<StudentTest> getStudentTests() {
        return studentTests;
    }

    public void setStudentTests(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }

    public void setStudentID(String ids){studentId = ids;}
    public ShowOneStudentController(){
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize(){
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            if(isManager){
                info.add(managerId);
                info.add("manager");
            }
            else {
                info.add(studentId);
                info.add("student");
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

    @Subscribe
    public void onMoveObjectToNextPageEvent(MoveObjectToNextPageEvent event){
        student = (Student) event.getObject();
        student_id.setText(student_id.getText() + student.getId());
        student_name.setText(student_name.getText() + student.getFirst_name() + " " + student.getLast_name());
    }

    @Subscribe public void onMoveManagerIdEvent(MoveManagerIdEvent event){
        isManager = true;
        managerId = event.getId();
    }

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        isManager = false;
        studentId = event.getId();
    }

    @Subscribe
    @FXML
    public void onShowOneStudentEvent(ShowOneStudentEvent event){
        try{
            setStudentTests(event.getStudentTests());

            TableGrade.setCellValueFactory(cellData -> {
                StudentTest test = cellData.getValue();
                String gradeToShow = "N/A";
                if(test.isChecked())
                    gradeToShow = Integer.toString( test.getGrade());
                return new SimpleStringProperty(gradeToShow);
            });
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
        if(!isManager) {
            try {
                if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                    StudentTest selectedStudentTest = GradesTable.getSelectionModel().getSelectedItem();
                    if (selectedStudentTest != null) {
                        if (selectedStudentTest.isChecked()) {
                            cleanup();
                            App.switchScreen("showStudentTest");
                            Platform.runLater(() -> {
                                try {
                                    EventBus.getDefault().post(new MoveIdToNextPageEvent(studentId));
                                    EventBus.getDefault().post(new MoveObjectToNextPageEvent(student));
                                    SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTestWithInfo", selectedStudentTest));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else{
                            System.out.println("boo woo");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        cleanup();
        if(isManager) {
            App.switchScreen("managerHome");
            Platform.runLater(() -> {
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#managerHome", managerId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            App.switchScreen("studentHome");
            Platform.runLater(() -> {
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#studentHome", studentId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    @FXML
    void handleBackButtonClick(ActionEvent event) throws IOException {
        cleanup();
        if(isManager) {
            App.switchScreen("allStudents");
            Platform.runLater(() -> {
                try {
                    EventBus.getDefault().post(new MoveManagerIdEvent(managerId));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            App.switchScreen("studentHome");
            Platform.runLater(() -> {
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#studentHome", studentId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

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
            if(isManager){
                info.add(managerId);
                info.add("manager");
            }
            else {
                info.add(studentId);
                info.add("student");
            }
            SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        } else {
            alert.close();
        }
    }
}
