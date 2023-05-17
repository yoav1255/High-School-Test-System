package il.cshaifasweng.OCSFMediatorExample.client.Controllers;


import java.io.IOException;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.Events.ShowAllStudentsEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    public ShowAllStudentsController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Subscribe
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
        try {
            if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                Student selectedStudent = students_table_view.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTests",selectedStudent));
                    App.switchScreen("showOneStudent");
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
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void handleGoHomeButtonClick(ActionEvent event){
        try{
            App.switchScreen("primary");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    }


