package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.*;

public class ShowStatisticsController {
    @FXML
    private Button secondaryButton;
    @FXML
    private Button goBack;

    @FXML
    private TableColumn<Statistics, String> median;

    @FXML
    private TableColumn<Statistics, String> scheduled_test;

    @FXML
    private TableColumn<Statistics, String > average;
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

    private List<Teacher> teacherNames;
    private List<Student> studentNames;
    private List<Course> courseNames;


    public ShowStatisticsController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void setFields() {
        Platform.runLater(() -> {
            if (!isManager) {
                stat_combobox.setValue("by teacher writer");
                stat_combobox.setDisable(true);
            } else {
                stat_combobox.setDisable(false);
            }
        });
    }


    @Subscribe
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event) throws IOException {
        teacherId = event.getId();
        isManager = false;
        setFields();

    }
    @Subscribe
    public void onMoveManagerIdEvent(MoveManagerIdEvent event) {
        isManager = true;
        managerId = event.getId();
    }

    public void setStatisticList(List<Statistics> statisticList) {
        this.statisticList = statisticList;
    }

    public void selected_stat(ActionEvent actionEvent) throws IOException {
        if(isManager){

            String selectedParameter = stat_combobox.getValue();
            if (Objects.equals(selectedParameter, "by teacher")) {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherName", null));

            } else if (Objects.equals(selectedParameter, "by course")) {

                SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseName", null));
            } else if (Objects.equals(selectedParameter, "by student")) {
                SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentName", null));
            }}
        else{
            SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherName", null));

        }
        }

    @Subscribe
    public void onShowAllTeachersNamesEvent(ShowAllTeachersNamesEvent event)
    {
        Platform.runLater(()->{
            combobox_id.getItems().clear();
            teacherNames = event.getTeacherList();
            for(Teacher teacher:teacherNames)
                combobox_id.getItems().add(teacher.getFirst_name() + " " +  teacher.getLast_name());
        });

    }

    @Subscribe
    public void onShowAllCoursesNamesEvent(ShowAllCoursesNamesEvent event)
    {
        Platform.runLater(()->{
        combobox_id.getItems().clear();
        courseNames = event.getCourseList();
        for(Course course:courseNames)
            combobox_id.getItems().add(course.getName());
    });
        }

    @Subscribe
    public void onShowAllStudentsNamesEvent(ShowAllStudentsNamesEvent event)
    {
        combobox_id.getItems().clear();
        studentNames = event.getStudentList();
        for(Student student:studentNames)
            combobox_id.getItems().add(student.getFirst_name() + " " + student.getLast_name());
    }

    public void handleShowStat(ActionEvent actionEvent) throws IOException {
        String selectedParameter = stat_combobox.getValue();
        String selectedName = combobox_id.getValue();
        int index = combobox_id.getSelectionModel().getSelectedIndex();
        String TeacherId;
        int CourseId;
        String StudentId;
        Platform.runLater(()->{
            statistics_table_view.refresh();
        });
            if (selectedName != null && selectedParameter != null) {
                if(isManager) {
                    switch (selectedParameter) {
                        case "by teacher" -> {
                            TeacherId = teacherNames.get(index).getId();
                            SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherStat", TeacherId));
                        }
                        case "by course" -> {
                            CourseId = courseNames.get(index).getCode();
                            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseStat", CourseId));
                        }
                        case "by student" -> {
                            StudentId = studentNames.get(index).getId();
                            SimpleClient.getClient().sendToServer(new CustomMessage("#getStudentStat", StudentId));
                        }
                    }
                }
                else {
                        TeacherId = teacherNames.get(index).getId();
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherWriterStat", TeacherId));
                    }
            } else {
                // Display an error message or show an alert to prompt the user to select values for both comboboxes
                // For example:
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Missing Selection");
                    alert.setContentText("Please select values for both comboboxes.");
                    alert.showAndWait();
                });

            }
        }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowTeacherStatEvent(ShowTeacherStatEvent event) {
        try {
        List<Statistics> teacherStat = event.getTeacherStat();
        statisticList.clear();
        statisticList.addAll(teacherStat);

        scheduled_test.setCellValueFactory(cellData->{
            Statistics stat = cellData.getValue();
            String id = stat.getScheduleTestId();
            return new SimpleStringProperty(id);
        });
        average.setCellValueFactory(cellData->{
            Statistics stat = cellData.getValue();
            String avg = Double.toString(stat.getAvgGrade());
            return new SimpleStringProperty(avg);
        });
        median.setCellValueFactory(cellData->{
            Statistics stat = cellData.getValue();
            String med = Integer.toString(stat.getMedian());
            return new SimpleStringProperty(med);
        });

        ObservableList<Statistics> observableStatisticsList = FXCollections.observableArrayList(statisticList);
        statistics_table_view.setItems(observableStatisticsList);
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowCourseStatEvent(ShowCourseStatEvent event) {
        try {
            List<Statistics> courseStat = event.getCourseStat();
            statisticList.clear();
            statisticList.addAll(courseStat);

            scheduled_test.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                String id = stat.getScheduleTestId();
                return new SimpleStringProperty(id);
            });
            average.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                String avg = Double.toString(stat.getAvgGrade());
                return new SimpleStringProperty(avg);
            });
            median.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                String med = Integer.toString(stat.getMedian());
                return new SimpleStringProperty(med);
            });

            ObservableList<Statistics> observableStatisticsList = FXCollections.observableArrayList(statisticList);
            statistics_table_view.setItems(observableStatisticsList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowStudentStatEvent(ShowStudentStatEvent event) {
        try {
            Statistics studentStat = event.getStudentStat();
            statisticList.clear();
            statisticList.add(studentStat);
            int index = combobox_id.getSelectionModel().getSelectedIndex();
            String StudentId = studentNames.get(index).getId();

            Platform.runLater(()-> {
                        scheduled_test.textProperty().setValue("Student ID");
                    });
            if(studentStat != null){
            scheduled_test.setCellValueFactory(cellData->{
                return new SimpleStringProperty(StudentId);
            });
            average.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                String avg = Double.toString(stat.getAvgGrade());
                return new SimpleStringProperty(avg);
            });
            median.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                String med = Integer.toString(stat.getMedian());
                return new SimpleStringProperty(med);
            });

            ObservableList<Statistics> observableStatisticsList = FXCollections.observableArrayList(statisticList);
            Platform.runLater(()->{
                statistics_table_view.setItems(observableStatisticsList);
            });}
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
                        //SimpleClient.getClient().sendToServer(new CustomMessage("#getStatisticsDistribute", selectedStat));
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


}


