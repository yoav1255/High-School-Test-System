package il.cshaifasweng.OCSFMediatorExample.client;


import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.server.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import il.cshaifasweng.OCSFMediatorExample.server.*;
public class ShowAllStudents {


    @FXML
    private Button secondaryButton;
    @FXML
    private TableColumn<Student, String> email;

    @FXML
    private TableColumn<Student, String> first_name;

    @FXML
    private Button goBack;

    @FXML
    private TableColumn<Student, String> id;

    @FXML
    private TableColumn<Student, String> last_name;
    @FXML
    private TableView<Student> students_table_view;


    @FXML
    void initialize() throws Exception {
        try {
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            first_name.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            last_name.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            List<Student> students = App.getAllStudents();
            ObservableList<Student> studentList = FXCollections.observableList(students);
            students_table_view.setItems(studentList);
        }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
