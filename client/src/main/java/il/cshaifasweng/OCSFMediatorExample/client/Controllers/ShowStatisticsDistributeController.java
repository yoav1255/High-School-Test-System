package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Statistics;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveManagerIdEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.ShowStatisticsDistributeEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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


public class ShowStatisticsDistributeController {
    @FXML
    private VBox distributionBox;

    @FXML
    private Button goBack;

    private Statistics selectedStat;
    private String teacherId;
    private String managerId;
    private boolean isManager;
    @FXML
    private TableView<Double> distribute_table_view;
    @FXML
    private TableColumn<Statistics, String> range;

    @FXML
    private TableColumn<Statistics, String > percentage;
    private List<Double> distributeList = new ArrayList<>();
    List<Double> distribution;
    private TableColumn.CellDataFeatures<Statistics, String> cellData;

    public ShowStatisticsDistributeController() {
        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }


    public void setSelectedStat(Statistics selectedStat) {
        this.selectedStat = selectedStat;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowStatisticsDistributeEvent(ShowStatisticsDistributeEvent event) {
        try {
            List<Double> statisticsList = event.getDistribution();
            List<String> statisticRange = new ArrayList<>();
            Statistics stats1 = event.getStats();

            range.setCellValueFactory(cellData -> {
                Statistics stat = cellData.getValue();
                String range2 = stat.getRange();
                return new SimpleStringProperty(range2);
            });

            percentage.setCellValueFactory(cellData -> {
                Statistics stats = cellData.getValue();
                List<Double> percentage2 = stats.getDistribution();
                System.out.println(percentage2);
                return new SimpleStringProperty(percentage2.toString());
            });



            Double distribution = selectedStat.getDistribution().get(1);
            System.out.println(distribution);


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


    public void handleRowClick(MouseEvent mouseEvent) {
    }


    public void handleBackButtonClick(ActionEvent actionEvent) throws IOException {
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
        } else {
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
            info.add(teacherId);
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