package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import il.cshaifasweng.OCSFMediatorExample.server.Events.UserHomeEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class ShowExamFormsController {
    private String id;
    public ShowExamFormsController(){

        EventBus.getDefault().register(this);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    public void handleGoHomeButtonClick(ActionEvent event) {
    }

    @Subscribe
    @FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        id = event.getId();
        System.out.println("in show exam forms control on event "+ id);
    }

    public void handleAddExamForm(ActionEvent event) {
        try {
            System.out.println("handle add exam form "+id);
            App.switchScreen("createExamForm");
            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", id));
            // TODO : send online teacher's id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
