package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;

public class PrimaryController {
	@FXML
	private Button goToAllStudentsButton;

//	public PrimaryController(){ EventBus.getDefault().register(this); }
//	public void cleanup() {
//		EventBus.getDefault().unregister(this);
//	}

    @FXML
    void sendWarning(ActionEvent event) {
    	try {
			SimpleClient.getClient().sendToServer(new CustomMessage("#warning",""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	@FXML
	void handleGoToAllStudentsButtonClick(ActionEvent event){
		try{
			SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents",""));
			App.switchScreen("allStudents");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@FXML
	void handleGoHomeButtonClick(ActionEvent event){
		try{
			App.switchScreen("primary");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@FXML
	void handleCreateTestButtonClick(ActionEvent event){
		try{
			String teacherId = "1";
			CustomMessage askForTeacher = new CustomMessage("#getTeacher",teacherId); // TODO : send online teacher's id
			SimpleClient.getClient().sendToServer(askForTeacher);
			App.switchScreen("createExamForm");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
