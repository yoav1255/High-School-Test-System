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
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class ShowExamFormsController {
    private String id ;
    private static int instances = 0;
    public ShowExamFormsController(){

        EventBus.getDefault().register(this);
        instances++;
        System.out.println("in exam forms "+instances);
    }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
        instances--;
        System.out.println("in exam forms "+instances);
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void handleGoHomeButtonClick(ActionEvent event) {
        try{
            String teacherId = this.id;
            System.out.println("go home "+teacherId);
            App.switchScreen("teacherHome");
            //SimpleClient.getClient().sendToServer(new CustomMessage("#teacherHome",teacherId));
            cleanup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN )
    @FXML
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        setId(event.getId());
    }

    @FXML
    public void handleAddExamForm(ActionEvent event) {
        Platform.runLater(()->{
            try {
                String teacherId = this.id;
                System.out.println("btn to add exam form "+ teacherId);
                App.switchScreen("createExamForm");
                SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", teacherId));
                // TODO : send online teacher's id);
                cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
@FXML
    public void onSelectSubject(ActionEvent event) {
    }
@FXML
    public void onSelectCourse(ActionEvent event) {
    }
}
