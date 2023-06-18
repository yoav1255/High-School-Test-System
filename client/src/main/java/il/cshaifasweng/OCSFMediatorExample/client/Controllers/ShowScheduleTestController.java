package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowScheduleTestController {
    private String idTeacher;

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
    @FXML
    private CheckBox onlyMyTestCheckBox;
    @FXML
    private Button btnNewTest;
    @FXML // fx:id="students_table_view"
    private TableView<ScheduledTest> scheduleTest_table_view; // Value injected by FXMLLoader

    @FXML // fx:id="submission"
    private TableColumn<ScheduledTest, String> checked; // Value injected by FXMLLoader

    @FXML // fx:id="teacherId"
    private TableColumn<ScheduledTest, String> teacherId; // Value injected by FXMLLoader

    @FXML // fx:id="time"
    private TableColumn<ScheduledTest, String> time; // Value injected by FXMLLoader
    @FXML // fx:id="time"
    private TableColumn<ScheduledTest, String> InComputer; // Value injected by FXMLLoader

    @FXML
    private Button currentTestBtn;

    @FXML
    private Button showAllTest;
    @FXML
    private Button testPerformed;
    @FXML
    private Button testHasntPerformed;

    private List<ScheduledTest> scheduledTests;
    private boolean edit = false;
    private boolean showGrades = false;
    private boolean extraTime=false;

    private boolean onlyMyTest=false;
    private boolean testsNotYetPerformed = false;
    private boolean testsPerformed = false;
    private boolean allTests = true;
    private boolean currentTests = false;

    private boolean isManager;
    private String managerId;

    private String presentThis="ShowAllTests";


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

    @FXML
    void initialize(){
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            if(isManager){
                info.add(managerId);
                info.add("manager");
            }
            else {
                info.add(idTeacher);
                info.add("teacher");
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
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) {
        this.idTeacher = event.getId();
        isManager = false;
        Platform.runLater(()->{
            btnNewTest.setDisable(false);
            btnNewTest.setVisible(true);
            onlyMyTestCheckBox.setVisible(true);

        });
    }

    @Subscribe
    public void onMoveManagerIdEvent(MoveManagerIdEvent event) {
        isManager = true;
        managerId = event.getId();
        Platform.runLater(()->{
            btnNewTest.setDisable(true);
            btnNewTest.setVisible(false);
            onlyMyTestCheckBox.setVisible(false);
        });

    }

    public void setScheduledTests(List<ScheduledTest> scheduledTests) {
        this.scheduledTests = scheduledTests;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowScheduleTestEvent(ShowScheduleTestEvent event) {
        setScheduledTests(event.getScheduledTestList());
        PresentsTable();
    }
    public void PresentsTable(){
        Platform.runLater(()->{
            try {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                date.setCellValueFactory(new PropertyValueFactory<>("date"));
                time.setCellValueFactory(cellData -> {
                    String formattedTime = cellData.getValue().getTime().toString();
                    formattedTime = formattedTime.substring(0, 5);
                    return new SimpleStringProperty(formattedTime);
                });
                checked.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCheckedSubmissions() + "/" + param.getValue().getSubmissions()));
                examFormId.setCellValueFactory(param -> {
                    try {
                        return new SimpleStringProperty(param.getValue().getExamForm().getCode());
                    } catch (NullPointerException e) {
                        // Handle the exception here (e.g., set a default value)
                        return new SimpleStringProperty("N/A");
                    }
                });
                teacherId.setCellValueFactory(param -> {
                    try {
                        return new SimpleStringProperty(param.getValue().getTeacher().getId());
                    } catch (NullPointerException e) {
                        // Handle the exception here (e.g., set a default value)
                        return new SimpleStringProperty("N/A");
                    }
                });
                //InComputer.setCellValueFactory(new PropertyValueFactory<>("isComputerTest"));
                InComputer.setCellValueFactory(cellData -> {
                    boolean isComputerTest = cellData.getValue().getIsComputerTest();
                    return new SimpleStringProperty(isComputerTest ? "Online" : "Local");
                });
                ShowScheduleTest(presentThis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void ShowScheduleTest(String show) {
        ObservableList<ScheduledTest> scheduledTestObservableList = FXCollections.observableArrayList();

        if (show.equals("ShowAllTests")) {
            if (!onlyMyTest)
                scheduledTestObservableList = FXCollections.observableList(scheduledTests);
            else {
                for (ScheduledTest scheduledTest : scheduledTests) {
                    if (this.idTeacher.equals(scheduledTest.getTeacher().getId()))
                        scheduledTestObservableList.add(scheduledTest);
                }
            }
            this.edit = false;
            this.showGrades = false;
        } else {
             scheduledTestObservableList = FXCollections.observableArrayList();

            for (ScheduledTest scheduledTest : scheduledTests) {
                if (show.equals("ShowTestHasntPerformed")) {
                    if (scheduledTest.getStatus()==0) {
                        if (!onlyMyTest)
                            scheduledTestObservableList.add(scheduledTest);
                        else {
                            if (this.idTeacher.equals(scheduledTest.getTeacher().getId()))
                                scheduledTestObservableList.add(scheduledTest);
                        }
                    }
                    this.showGrades = false;
                    this.edit = true;
                    this.extraTime=false;
                } else if (show.equals("ShowTestPerformed")) {
                    if (scheduledTest.getStatus()!=0) {
                        if (!onlyMyTest)
                            scheduledTestObservableList.add(scheduledTest);
                        else {
                            if (this.idTeacher.equals(scheduledTest.getTeacher().getId()))
                                scheduledTestObservableList.add(scheduledTest);
                        }
                    }
                    this.showGrades = true;
                    this.edit = false;
                    this.extraTime=false;
                }
                else if (show.equals("ShowCurrentTests")) {
                    if (scheduledTest.getStatus()==1) {
                        if (!onlyMyTest)
                            scheduledTestObservableList.add(scheduledTest);
                        else {
                            if (this.idTeacher.equals(scheduledTest.getTeacher().getId()))
                                scheduledTestObservableList.add(scheduledTest);
                        }
                    }
                    this.extraTime=true;
                    this.showGrades = false;
                    this.edit = false;
                }

            }
        }
        scheduleTest_table_view.setItems(scheduledTestObservableList);

    }
    @FXML
    void showCurrentTest(ActionEvent event) throws IOException {
        Platform.runLater(()->{
            currentTestBtn.setStyle("-fx-background-color:  #ffab2e; -fx-text-fill: black;");;
            showAllTest.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            testPerformed.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            testHasntPerformed.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
        });
        this.presentThis="ShowCurrentTests";
        testsPerformed = false;
        testsNotYetPerformed = false;
        allTests = false;
        currentTests = true;
        SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
    }
    @FXML
    void showTestHasntPerformed(ActionEvent event) {
        Platform.runLater(()->{
            testHasntPerformed.setStyle("-fx-background-color:  #ffab2e; -fx-text-fill: black;");
            testPerformed.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            showAllTest.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
           currentTestBtn.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");

        });

        this.presentThis="ShowTestHasntPerformed";
        testsPerformed = false;
        testsNotYetPerformed = true;
        allTests = false;
        currentTests = false;
        PresentsTable();
    }

    @FXML
    void showTestPerformed(ActionEvent event) {
        Platform.runLater(()->{
            testPerformed.setStyle("-fx-background-color:  #ffab2e; -fx-text-fill: black;");
            showAllTest.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            testHasntPerformed.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            currentTestBtn.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
        });

        this.presentThis="ShowTestPerformed";
        testsPerformed = true;
        testsNotYetPerformed = false;
        allTests = false;
        currentTests = false;
        PresentsTable();
    }

    @FXML
    void handleShowAllTest(ActionEvent event) {
        Platform.runLater(()->{
            showAllTest.setStyle("-fx-background-color:  #ffab2e; -fx-text-fill: black;");
            testPerformed.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            testHasntPerformed.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
            currentTestBtn.setStyle("-fx-background-color:  white; -fx-text-fill: black; -fx-border-color: orange;");
        });

        this.presentThis="ShowAllTests";
        testsPerformed = false;
        testsNotYetPerformed = false;
        allTests = true;
        currentTests = false;
        PresentsTable();
    }
    @FXML
    void handleOnlyMyTest(ActionEvent event) throws IOException {
        if (onlyMyTestCheckBox.isSelected()) {
            onlyMyTest = true;
        }
        else {
            onlyMyTest = false;
        }
        if(currentTests) SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
        else PresentsTable();
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
        if (!isManager) {
            try {
                if (event.getClickCount() == 2 && scheduleTest_table_view.getSelectionModel().getSelectedItem() != null) { // Check if the user double-clicked the row
                    ScheduledTest selectedTest = scheduleTest_table_view.getSelectionModel().getSelectedItem();
                    if (this.idTeacher != null && this.idTeacher.equals(selectedTest.getTeacher().getId()) && selectedTest.getStatus() == 0) {
                        App.switchScreen("scheduledTest");
                        Platform.runLater(() -> {
                            try {
                                EventBus.getDefault().post(new ShowScheduleTestEvent(scheduledTests));
                                SimpleClient.getClient().sendToServer(new CustomMessage("#fillComboBox", idTeacher));
                                SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacher", idTeacher));
                                EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
                                EventBus.getDefault().post(new SelectedTestEvent(selectedTest));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });

                    } else if (this.idTeacher != null && this.idTeacher.equals(selectedTest.getTeacher().getId()) ) {
                        App.switchScreen("testGrade");
                        Platform.runLater(() -> {
                            try {
                                EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
                                SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentTestsFromSchedule", selectedTest));
                                EventBus.getDefault().post(new SelectedTestEvent(selectedTest));
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
    }


    @FXML
    void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        cleanup();
        if (!isManager) {
            try {
                App.switchScreen("teacherHome");
                Platform.runLater(() -> {
                    try {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome", idTeacher));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void goToScheduleNewTest(ActionEvent event) throws IOException {
        System.out.println("pressed");
        App.switchScreen("scheduledTest");
        Platform.runLater(() -> {
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#scheduledTest", ""));
                EventBus.getDefault().post(new ShowScheduleTestEvent(scheduledTests));
                SimpleClient.getClient().sendToServer(new CustomMessage("#fillComboBox", idTeacher));
                SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacher", idTeacher));
                EventBus.getDefault().post(new MoveIdToNextPageEvent(idTeacher));
            } catch (Exception e) {

            }
        });
    }

    @Subscribe
    public synchronized void onTimeLeftEvent(TimeLeftEvent event) throws IOException {
        SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest",""));
    }

    @FXML
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
                info.add(idTeacher);
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

    @FXML
    public void handleBackButtonClick(ActionEvent actionEvent) throws IOException {
        handleGoHomeButtonClick(null);
    }

}



