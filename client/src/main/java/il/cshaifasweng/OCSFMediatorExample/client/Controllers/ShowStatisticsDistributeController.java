package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Distribution;
import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveManagerIdEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowStatisticsDistributeEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ShowStatisticsDistributeController {
    @FXML
    private VBox distributionBox;

    @FXML
    private Button goBack;

    @FXML
    private BarChart<String, Double> distributeBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Statistics selectedStat;
    private String teacherId;
    private String managerId;
    private boolean isManager;


    public ShowStatisticsDistributeController() {
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

    public void setSelectedStat(Statistics selectedStat) {
        this.selectedStat = selectedStat;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowStatisticsDistributeEvent(ShowStatisticsDistributeEvent event) {
        try {
            List<Double> grades = event.getDistribution();
            List<String> statisticRange = new ArrayList<>();
            Statistics stats = event.getStats();
            List<Distribution> distributionList = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                statisticRange.add(stats.getRange(i));
                Distribution distribution = new Distribution(statisticRange.get(i), grades.get(i));
                distributionList.add(distribution);
            }

            XYChart.Series<String, Double> series = new XYChart.Series<>();
            for (Distribution distribution : distributionList) {
                series.getData().add(new XYChart.Data<>(distribution.getRange(), distribution.getPercentage()));
            }

            Platform.runLater(() -> {
                distributeBarChart.getData().clear();
                distributeBarChart.getData().add(series);
                xAxis.setTickLength(1); // Set the tick mark unit to 1
                xAxis.setCategories(FXCollections.observableArrayList(series.getData().stream().map(XYChart.Data::getXValue).collect(Collectors.toList())));

                double barGap = 0.8; // Adjust this value as needed
                double categoryGap = 0.0; // Adjust this value as needed
                distributeBarChart.setCategoryGap(categoryGap);
                distributeBarChart.setBarGap(barGap);
                //xAxis.setGapStartAndEnd(false);
            });
            distributeBarChart.setTitle("Distribution of Grades");

            // Apply custom styles
            distributeBarChart.lookup(".chart-title").setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
            distributeBarChart.lookup(".chart-legend").setStyle("-fx-font-size: 12px;");
            distributeBarChart.lookupAll(".chart-bar").forEach(bar -> {
                bar.setStyle("-fx-bar-fill: #ffab2e;");
                bar.setOnMouseEntered(mouseEvent -> bar.setStyle("-fx-bar-fill: #ff8000;"));
                bar.setOnMouseExited(mouseEvent ->  bar.setStyle("-fx-bar-fill: #ffab2e;"));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleGoBackButtonClick(ActionEvent event) throws IOException {
        App.switchScreen("showStatistics");
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

    public void handleBackButtonClick(ActionEvent actionEvent) throws IOException {
        if (!isManager) {
            cleanup();
            App.switchScreen("showStatistics");
            Platform.runLater(()->{
                EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
            });
        }
        else{
            cleanup();
            App.switchScreen("showStatistics");
            Platform.runLater(()->{
                EventBus.getDefault().post(new MoveManagerIdEvent(managerId));
            });
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

    public void handleGoHomeButtonClick(ActionEvent event) throws IOException {
        cleanup();
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


}