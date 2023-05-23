package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import il.cshaifasweng.OCSFMediatorExample.server.Events.MoveIdToNextPageEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ShowQuestionsController {
    private static String id="hi";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShowQuestionsController() {
        //this.id = "check";
        EventBus.getDefault().register(this);
    }
    @Subscribe
    @FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        System.out.println("on event "+event.getId());
        setId(event.getId());
        System.out.println("check 2 " +id);
    }

@FXML
    public void GoToAddQuestion(ActionEvent event) {
    Platform.runLater(()->{
        try {
            System.out.println("btn to add Question form "+ id);
            App.switchScreen("createQuestion");
            SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", this.id));
            // TODO : send online teacher's id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    }
}
