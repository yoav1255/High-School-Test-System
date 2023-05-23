package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.SelectedTestEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowScheduleTestEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import java.util.List;

public class ShowScheduleTestController {
    private  String idTeacher;

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


    public String getId() {
        return this.idTeacher;
    }

    public void setId(String id) {
        this.idTeacher = id;
    }


    public ShowScheduleTestController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        System.out.println("Ok"+event.getId());
        this.idTeacher=event.getId();
        System.out.println(idTeacher);
    }
    public void setScheduledTests(List<ScheduledTest> scheduledTests) {
        this.scheduledTests = scheduledTests;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowScheduleTestEvent(ShowScheduleTestEvent event) {
        try {
            setScheduledTests(event.getScheduledTestList());
            id.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("id"));
            date.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("date") );
            time.setCellValueFactory(cellData -> {
                String formattedTime = cellData.getValue().getTime().toString();
                formattedTime = formattedTime.substring(0, 5);
                return new SimpleStringProperty(formattedTime);
            });
            submission.setCellValueFactory(new PropertyValueFactory<ScheduledTest,String>("submissions"));
            examFormId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTest, String>, ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTest, String> param) {
                    try {

                        return new SimpleStringProperty(param.getValue().getExamForm().getCode());
                    } catch (NullPointerException e) {
                        // Handle the exception here (e.g., set a default value)
                        return new SimpleStringProperty("N/A");
                    }
                }
            });
            teacherId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTest, String>, ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTest, String> param) {
                    try {
                        return new SimpleStringProperty(param.getValue().getTeacher().getId());
                    } catch (NullPointerException e) {
                    // Handle the exception here (e.g., set a default value)
                    return new SimpleStringProperty("N/A");
                }
                }
            });
            ObservableList<ScheduledTest> scheduledTestObservableList = FXCollections.observableList(scheduledTests);
            scheduleTest_table_view.setItems(scheduledTestObservableList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
        try {
            if (event.getClickCount() == 2&&scheduleTest_table_view.getSelectionModel().getSelectedItem() != null) { // Check if the user double-clicked the row
                ScheduledTest selectedTest = scheduleTest_table_view.getSelectionModel().getSelectedItem();
                System.out.println(idTeacher);
                if (idTeacher.equals(selectedTest.getTeacher().getId())){
//                        ScheduledTestController.updateSelectedRow(selectedTest);
                          App.switchScreen("scheduledTest");
                    Platform.runLater(()->{
                        try {
                            SimpleClient.getClient().sendToServer(new CustomMessage("#fillComboBox", idTeacher));
                            SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacher", idTeacher));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
                        EventBus.getDefault().post(new SelectedTestEvent(selectedTest));
                    });

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) throws IOException {
            App.switchScreen("allStudents");
            Platform.runLater(()->{
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents",""));
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
            App.switchScreen("primary");
            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

    @FXML
    public void goToScheduleNewTest(ActionEvent event) throws IOException {
        App.switchScreen("scheduledTest");
        Platform.runLater(()->{
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#scheduledTest", ""));
                SimpleClient.getClient().sendToServer(new CustomMessage("#fillComboBox", idTeacher));
                SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacher", idTeacher));
                EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
            }catch (Exception e){

            }
        });
    }
}



