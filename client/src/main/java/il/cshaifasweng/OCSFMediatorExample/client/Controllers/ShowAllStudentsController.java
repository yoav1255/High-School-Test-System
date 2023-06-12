package il.cshaifasweng.OCSFMediatorExample.client.Controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import il.cshaifasweng.OCSFMediatorExample.client.App;
//import il.cshaifasweng.OCSFMediatorExample.entities.EventBusManager;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveManagerIdEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowAllStudentsEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveObjectToNextPageEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ShowAllStudentsController {

    @FXML
    private Button secondaryButton;
    @FXML
    private Button goBack;

    @FXML
    private TableColumn<Student, String> email;

    @FXML
    private TableColumn<Student, String> first_name;
    @FXML
    private TableColumn<Student, String> id;

    @FXML
    private TableColumn<Student, String> last_name;
    @FXML
    private TableView<Student> students_table_view;

    private List<Student> studentList;
    private String teacherId;
    private String managerId;
    private boolean isManager;



    public ShowAllStudentsController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        teacherId = event.getId();
        isManager = false;
    }
    @Subscribe
    public void onMoveManagerIdEvent(MoveManagerIdEvent event)  {
        isManager = true;
        managerId = event.getId();
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @FXML
    public void onShowAllStudentsEvent(ShowAllStudentsEvent event) {
        try {
            setStudentList(event.getStudentList());
            id.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
            first_name.setCellValueFactory(new PropertyValueFactory<Student,String>("first_name"));
            last_name.setCellValueFactory(new PropertyValueFactory<Student,String>("last_name"));
            email.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
            ObservableList<Student> students = FXCollections.observableList(studentList);
            students_table_view.setItems(students);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
        if (isManager) { // only the manager can access the students grades from all students
            try {
                if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                    Student selectedStudent = students_table_view.getSelectionModel().getSelectedItem();
                    App.switchScreen("showOneStudent");

                    if (selectedStudent != null) {
                        Platform.runLater(()->{
                            try {
                                EventBus.getDefault().post(new MoveManagerIdEvent(managerId));
                                EventBus.getDefault().post(new MoveObjectToNextPageEvent(selectedStudent));
                                SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTests", selectedStudent));
                                // posts ShowOneStudentEvent
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event){
        if (!isManager) {
            try {
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", teacherId));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                cleanup();
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
                cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                info.add(teacherId);
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


