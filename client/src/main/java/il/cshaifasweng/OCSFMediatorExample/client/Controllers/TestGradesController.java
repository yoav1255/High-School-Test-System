/**
 * Sample Skeleton for 'testGrade.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowOneStudentEvent;
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

import java.io.IOException;
import java.util.List;

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
    private TableView<StudentTest> scheduleTest_table_view; // Value injected by FXMLLoader

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
    private String id;
    private List<StudentTest> studentTests;

    public TestGradesController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setId(String id){this.id = id;}

    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        setId(event.getId());
    }
    @Subscribe
    public void onShowOneStudentEvent(ShowOneStudentEvent event)throws IOException{
        this.studentTests=event.getStudentTests();
        System.out.println(studentTests);
        studentId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentTest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<StudentTest, String> param) {
                return new SimpleStringProperty(param.getValue().getStudent().getId());
            }
        });        studentName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentTest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<StudentTest, String> param) {
                    return new SimpleStringProperty(param.getValue().getStudent().getFirst_name()+param.getValue().getStudent().getLast_name());
            }
        });
      timeTook.setCellValueFactory(new PropertyValueFactory<StudentTest, String>("timeToComplete"));
        gender.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentTest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<StudentTest, String> param) {
                    return new SimpleStringProperty(param.getValue().getStudent().getGender());
            }
        });
        grade.setCellValueFactory(new PropertyValueFactory<StudentTest, String>("grade"));
        ObservableList<StudentTest> studentTestObservableList = FXCollections.observableList(studentTests);
        scheduleTest_table_view.setItems(studentTestObservableList);


    }
    @FXML
    void goToScheduleNewTest(ActionEvent event) {
        try {
            App.switchScreen("showScheduleTest");

            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        cleanup();
    }

    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen("teacherHome");
        Platform.runLater(()->{
            EventBus.getDefault().post(new MoveIdToNextPageEvent(id));

        });
        cleanup();
    }

    @FXML
    void handleGoToAllStudentsButtonClick(ActionEvent event) {

    }

    @FXML
    void handleRowClick(MouseEvent event) {

    }

}
