package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowStatisticsController {
    @FXML
    private Button secondaryButton;
    @FXML
    private Button goBack;

    @FXML
    private TableColumn<Statistics, Integer> median;

    @FXML
    private TableColumn<Statistics, String> scheduled_test;

    @FXML
    private TableColumn<Statistics, Double> average;
    @FXML
    private TableView<Statistics> statistics_table_view;

    private List<Statistics> statisticList = new ArrayList<>();

    private String teacherId;
    private String managerId;
    private boolean isManager;

    @FXML
    private ComboBox<String> combobox_id;

    @FXML
    private ComboBox<String> stat_combobox;


    @FXML
    private TableView<Statistics> tableView;

    private List<String> teacherNames;
    private List<String> studentNames;
    private List<String> courseNames;


    public ShowStatisticsController() {
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
    public void onMoveManagerIdEvent(MoveManagerIdEvent event) {
        isManager = true;
        managerId = event.getId();
    }

    public void setStatisticList(List<Statistics> statisticList) {
        this.statisticList = statisticList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @FXML
    public void onShowAllStatisticEvent(ShowAllStatisticEvent event) {
        try {
            List<Statistics> statisticsList = event.getStatisticsList();
            ObservableList<Statistics> observableStatisticsList = FXCollections.observableArrayList(statisticsList);

            scheduled_test.setCellValueFactory(new PropertyValueFactory<Statistics, String>("scheduled_test"));
            average.setCellValueFactory(new PropertyValueFactory<Statistics, Double>("average"));
            median.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("median"));

            statistics_table_view.setItems(observableStatisticsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
            try {
                if (event.getClickCount() == 2) { // Check if the user double-clicked the row
                    Statistics selectedStat = statistics_table_view.getSelectionModel().getSelectedItem();
                    if (selectedStat != null) {
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getStatisticsDistribute", selectedStat));
                        App.switchScreen("showStatisticsDistribute");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    public void selected_stat(ActionEvent actionEvent) throws IOException {
        String selectedParameter = stat_combobox.getValue();
        if(Objects.equals(selectedParameter, "by teacher"))
        {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherName",null));

        } else if (Objects.equals(selectedParameter, "by course")) {

            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseName",null));
        }
        else if (Objects.equals(selectedParameter, "by student"))
        {
            SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentName",null));
        }
    }



    @Subscribe
    public void onShowAllTeachersNamesEvent(ShowAllTeachersNamesEvent event)
    {
        combobox_id.getItems().clear();
        teacherNames = event.getTeacherList();
        for(String teacher:teacherNames)
        combobox_id.getItems().add(teacher);
    }

    @Subscribe
    public void onShowAllCoursesNamesEvent(ShowAllCoursesNamesEvent event)
    {
        combobox_id.getItems().clear();
        courseNames = event.getCourseList();
        for(String course:courseNames)
            combobox_id.getItems().add(course);
    }

    @Subscribe
    public void onShowAllStudentsNamesEvent(ShowAllStudentsNamesEvent event)
    {
        combobox_id.getItems().clear();
        studentNames = event.getStudentList();
        for(String student:studentNames)
            combobox_id.getItems().add(student);
    }

    @Subscribe
    (threadMode = ThreadMode.MAIN)
    public void onShowTeacherStatEvent(ShowTeacherStatEvent event) {
        List<Statistics> teacherStat = event.getTeacherStat();
        statisticList.clear();
        statisticList.addAll(teacherStat);
        statistics_table_view.setItems((ObservableList<Statistics>) statisticList);
    }


    public void handleShowStat(ActionEvent actionEvent) throws IOException {
        String selectedParameter = stat_combobox.getValue();
        String selectedName = combobox_id.getValue();

        if (selectedName != null && selectedParameter != null) {
            switch (selectedParameter) {
                case "by teacher" ->
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherStat", selectedName));
                case "by course" ->
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseStat", selectedName));
                case "by student" ->
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentStat", selectedName));
            }
        } else {
            // Display an error message or show an alert to prompt the user to select values for both comboboxes
            // For example:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Selection");
            alert.setContentText("Please select values for both comboboxes.");
            alert.showAndWait();
        }
    }








}


