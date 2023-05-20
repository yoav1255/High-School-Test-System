package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowScheduleTestEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

public class ShowScheduleTestController {


    @FXML // fx:id="allStudentsBN"
    private Button allStudentsBN; // Value injected by FXMLLoader

    @FXML // fx:id="date"
    private TableColumn<ScheduledTest, String> date; // Value injected by FXMLLoader

    @FXML // fx:id="examFormId"
    private TableColumn<ScheduledTest, String> examFormId; // Value injected by FXMLLoader

    @FXML // fx:id="homeBN"
    private Button homeBN; // Value injected by FXMLLoader

    @FXML // fx:id="id"
    private TableColumn<ScheduledTest, String> id; // Value injected by FXMLLoader

    @FXML // fx:id="scheduleTestGP"
    private GridPane scheduleTestGP; // Value injected by FXMLLoader

    @FXML // fx:id="statusLB"
    private Label statusLB; // Value injected by FXMLLoader

    @FXML // fx:id="statusLB1"
    private Label statusLB1; // Value injected by FXMLLoader

    @FXML // fx:id="students_table_view"
    private TableView<ScheduledTest> scheduleTest_table_view; // Value injected by FXMLLoader

    @FXML // fx:id="submission"
    private TableColumn<ScheduledTest, String> submission; // Value injected by FXMLLoader

    @FXML // fx:id="teacherId"
    private TableColumn<ScheduledTest, String> teacherId; // Value injected by FXMLLoader

    @FXML // fx:id="time"
    private TableColumn<ScheduledTest, String> time; // Value injected by FXMLLoader

    private List<ScheduledTest> scheduledTests;

    public ShowScheduleTestController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setScheduledTests(List<ScheduledTest> scheduledTests) {
        this.scheduledTests = scheduledTests;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @FXML
    public void ShowScheduleTestEvent(ShowScheduleTestEvent event) {
        try {
            setScheduledTests(event.getScheduledTestList());
            id.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("id"));
            date.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("date"));
            time.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("time"));
            submission.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("submissions"));
            examFormId.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("examForm_id"));
            teacherId.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("teacher_id"));
            ObservableList<ScheduledTest> scheduledTestObservableList = FXCollections.observableList(scheduledTests);
            scheduleTest_table_view.setItems(scheduledTestObservableList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
//        try {
//            if (event.getClickCount() == 2) { // Check if the user double-clicked the row
//                ScheduledTest selectedTest = scheduleTest_table_view.getSelectionModel().getSelectedItem();
//                if (selectedTest != null) {
//                    SimpleClient.getClient().sendToServer(new CustomMessage("#getScheduleTest",selectedTest));
//                    App.switchScreen("ShowOneTest");
//                }
//            }
//        }catch (IOException e){
//            e.printStackTrace();
       // }
    }

    @FXML
    void handleGoScheduleTestButtonClick(ActionEvent event){
        try{
            SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
            App.switchScreen("showScheduleTest");
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



