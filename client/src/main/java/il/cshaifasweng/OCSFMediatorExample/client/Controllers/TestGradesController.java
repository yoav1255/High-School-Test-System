/**
 * Sample Skeleton for 'testGrade.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestGradesController {

    @FXML // fx:id="allStudentsBN"
    private Button allStudentsBN; // Value injected by FXMLLoader

    @FXML // fx:id="gender"
    private TableColumn<StudentTest, String> gender; // Value injected by FXMLLoader

    @FXML // fx:id="grade"
    private TableColumn<StudentTest, String> grade; // Value injected by FXMLLoader

    @FXML // fx:id="homeBN"
    private Button homeBN; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleTestGP"
    private GridPane scheduleTestGP; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleTest_table_view"
    private TableView<StudentTest> studentTestTableView; // Value injected by FXMLLoader

    @FXML // fx:id="statusLB"
    private Label statusLB; // Value injected by FXMLLoader

    @FXML // fx:id="statusLB1"
    private Label statusLB1; // Value injected by FXMLLoader

    @FXML // fx:id="studentId"
    private TableColumn<StudentTest, String> studentId; // Value injected by FXMLLoader

    @FXML // fx:id="studentName"
    private TableColumn<StudentTest, String> studentName; // Value injected by FXMLLoader

    @FXML // fx:id="timeTook"
    private TableColumn<StudentTest, String> timeTook; // Value injected by FXMLLoader

    @FXML
    private Label ontime_text;

    int onTime;
    int afterTime;
    private String id;
    private List<StudentTest> studentTests;

    private ScheduledTest selectedTest;

    public TestGradesController() {
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize() {
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            info.add(id);
            info.add("teacher");
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

    public void setId(String id) {
        this.id = id;
    }

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        setId(event.getId());
    }

    @Subscribe
    public void onSelectedTestEvent(SelectedTestEvent event) throws IOException {
        this.selectedTest = event.getSelectedTestEvent();
    }

    @Subscribe
    public void onShowStudentFromScheduleEvent(ShowStudentFromScheduleEvent event) throws IOException {
        this.studentTests = event.getStudentTests();
        onTime=0;
        afterTime=0;
        for(StudentTest st : studentTests){
            if(st.isOnTime()) onTime++;
            else afterTime++;
        }
        Platform.runLater(()->{
            ontime_text.setText("On time: " + onTime + " / "+studentTests.size());
        });

        studentId.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStudent().getId()));
        studentName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStudent().getFirst_name() + param.getValue().getStudent().getLast_name()));
        timeTook.setCellValueFactory(new PropertyValueFactory<>("timeToComplete"));
        gender.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStudent().getGender()));
        if (selectedTest.getIsComputerTest())
            grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        else
            grade.setCellValueFactory(param -> {
                return new SimpleStringProperty("Local");
            });
        ObservableList<StudentTest> studentTestObservableList = FXCollections.observableList(studentTests);
        studentTestTableView.setRowFactory(tv -> {
            TableRow<StudentTest> row = new TableRow<StudentTest>() {
                @Override
                protected void updateItem(StudentTest studentTest, boolean empty) {
                    super.updateItem(studentTest, empty);
                    if (studentTest != null && studentTest.isChecked()&&selectedTest.getIsComputerTest()) {
                        setStyle("-fx-background-color: #2ECC71 ;");
                    } else if (studentTest != null && !studentTest.isChecked()&&selectedTest.getIsComputerTest()) {
                        setStyle("-fx-background-color: #E74C3C ;");
                    }
                }
            };
            return row;
        });

        studentTestTableView.setItems(studentTestObservableList);
    }

    @FXML
    void handleBackButtonClick(ActionEvent event) throws IOException {
        cleanup();
        App.switchScreen("showScheduleTest");

        Platform.runLater(() -> {
            try {
                EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("teacherHome");
        Platform.runLater(() -> {
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        cleanup();
    }


    @FXML
    void handleRowClick(MouseEvent event) {
        try {
            if (event.getClickCount() == 2 && selectedTest.getIsComputerTest()) { // Check if the user double-clicked the row
                StudentTest selectedStudentTest = studentTestTableView.getSelectionModel().getSelectedItem();

                if (selectedStudentTest != null) {
                    App.switchScreen("showUpdateStudent");
                    Platform.runLater(() -> {
                        try {
                            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                            SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTestWithInfo", selectedStudentTest));
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

    @Subscribe
    public void onShowSuccessEvent(ShowSuccessEvent event) {
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
            info.add(id);
            info.add("teacher");
            SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        } else {
            alert.close();
        }
    }

}
