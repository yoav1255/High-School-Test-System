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

    @FXML
    void initialize() {
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            if(isManager){
                info.add(managerId);
                info.add("manager");
            }
            else {
                info.add(teacherId);
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


    public void setFields() {
        Platform.runLater(() -> {
            if (!isManager) {
                stat_combobox.setValue("by teacher writer");
                combobox_id.setValue(this.teacherId);
                stat_combobox.setVisible(false);
                combobox_id.setVisible(false);
            } else {
                stat_combobox.setVisible(true);
                combobox_id.setVisible(true);
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
        Platform.runLater(()->{
            statistics_table_view.refresh();
        });
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
    public void onShowAllStudentsNamesEvent(ShowAllStudentsNamesEvent event) {
        Platform.runLater(() -> {
            combobox_id.getItems().clear();
            studentNames = event.getStudentList();
            for (Student student : studentNames)
                combobox_id.getItems().add(student.getFirst_name() + " " + student.getLast_name());
        });
    }

    public void name_stat(ActionEvent actionEvent) throws IOException {
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
                        SimpleClient.getClient().sendToServer(new CustomMessage("#getTeacherWriterStat", teacherId));
                    }

            }
        }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowTeacherStatEvent(ShowTeacherStatEvent event) {
        try {
            Platform.runLater(() -> {
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
            });
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowCourseStatEvent(ShowCourseStatEvent event) {
        try {
            Platform.runLater(() -> {
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowStudentStatEvent(ShowStudentStatEvent event) {
        try {
            Platform.runLater(()-> {
                scheduled_test.textProperty().setValue("Student ID");
            });

            Statistics studentStat = event.getStudentStat();
            statisticList.clear();
            statisticList.add(studentStat);

            Platform.runLater(()-> {
            scheduled_test.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                if(stat != null) {
                    String id = stat.getStudentId();
                    return new SimpleStringProperty(id);
                }
                else {
                    return new SimpleStringProperty(null);
                }
            });
            average.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                if(stat != null) {
                    String avg = Double.toString(stat.getAvgGrade());
                    return new SimpleStringProperty(avg);
                }
                else {
                    return new SimpleStringProperty(null);
                }
            });
            median.setCellValueFactory(cellData->{
                Statistics stat = cellData.getValue();
                if(stat != null) {
                    String med = Integer.toString(stat.getMedian());
                    return new SimpleStringProperty(med);
                }
                else {
                    return new SimpleStringProperty(null);
                }

            });
            });

            ObservableList<Statistics> observableStatisticsList = FXCollections.observableArrayList(statisticList);
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
                        App.switchScreen("showStatisticsDistribute");
                        Platform.runLater(()->{
                            EventBus.getDefault().post(new ShowStatisticsDistributeEvent(selectedStat));
                            if(isManager) {
                                EventBus.getDefault().post(new MoveManagerIdEvent(managerId));
                            }
                            else {
                                EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                            }
                        });
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
                info.add(teacherId);
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


    public void handleBackButtonClick(ActionEvent actionEvent) throws IOException {
        handleGoHomeButtonClick(null);
    }

}


