package il.cshaifasweng.OCSFMediatorExample.client;


import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import il.cshaifasweng.OCSFMediatorExample.server.*;
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

    public ShowAllStudentsController() {
        EventBus.getDefault().register(this);
    }
//    public void dispose() {
//        EventBus.getDefault().unregister(this);
//    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Subscribe
    @FXML
    public void onShowAllStudentsEvent(ShowAllStudentsEvent event) {
        try {
            System.out.println("in show all students controller");
            setStudentList(event.getStudentList());
            System.out.println(studentList.get(0).getId());
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
        if (event.getClickCount() == 2) { // Check if the user double-clicked the row
            Student selectedStudent = students_table_view.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                // Perform some action based on the selected student, such as showing their details
                //System.out.println("Selected student: " + selectedStudent.getFirstName() + " " + selectedStudent.getLastName());
            }
        }
    }

//    @FXML
//    private void handleStudentSelection() {
//        Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("oneStudentInfo.fxml"));
//        StudentDataController controller = loader.getController();
//        controller.setStudent(selectedStudent);
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
//    }

    }


