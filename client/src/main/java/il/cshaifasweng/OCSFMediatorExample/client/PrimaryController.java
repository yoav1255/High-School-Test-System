package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.server.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.*;

public class PrimaryController {
	@FXML
	private Button goToAllStudentsButton;

    @FXML
    void sendWarning(ActionEvent event) {
    	try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	@FXML
	void handleGoToAllStudentsButtonClick(ActionEvent event){
		try{
			SimpleClient.getClient().sendToServer("#showAllStudents");
			App.switchScreen("allStudents");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	@FXML
	void handleGoHomeButtonClick(ActionEvent event){
		try{
			App.switchScreen("primary");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
