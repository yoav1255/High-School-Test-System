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
        System.out.println(" in constructor ");
    }
    public void handleGoHomeButtonClick(ActionEvent event) {
    }

    @Subscribe
    @FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        id = event.getId();
        System.out.println("in show exam forms control on event"+ id);
    }

    public void handleAddExamForm(ActionEvent event) throws IOException {
        try {
            System.out.println("handle add exam form "+id);
            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", id));
            // TODO : send online teacher's id);
            App.switchScreen("createExamForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
