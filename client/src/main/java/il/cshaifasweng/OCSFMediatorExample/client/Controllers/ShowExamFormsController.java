package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class ShowExamFormsController {
    private String id;
    public ShowExamFormsController(){

        EventBus.getDefault().register(this);
        this.id = "check";
        System.out.println("in constructor");
    }
//    public void cleanup() {
//        EventBus.getDefault().unregister(this);
//    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void handleGoHomeButtonClick(ActionEvent event) {
    }

    @Subscribe
    @FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        setId(event.getId());
    }

    @FXML
    public void handleAddExamForm(ActionEvent event) {
            Platform.runLater(()->{
            try {
                App.switchScreen("createExamForm");
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", this.id));
                // TODO : send online teacher's id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
