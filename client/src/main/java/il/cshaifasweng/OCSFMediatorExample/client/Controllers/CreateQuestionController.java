package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateQuestionController implements Initializable {
    @FXML
    private Label subjectLabel;
    @FXML
    private ChoiceBox<String> subjectChoiceBox;

    private String[] subects = {"Math","English","Sport"};

    @FXML
    private Label coursesLabel;
    @FXML
    private ListView coursesList;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    @FXML
    void cancel(ActionEvent event) {
        try{
            App.switchScreen("primary");
            //cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void confirm(ActionEvent event) {
        try{
            App.switchScreen("buildQuestion");
            //cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        subjectChoiceBox.getItems().addAll(subects);

    }
}
