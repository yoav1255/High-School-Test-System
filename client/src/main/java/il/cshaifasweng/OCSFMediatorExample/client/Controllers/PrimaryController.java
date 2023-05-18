package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
			App.switchScreen("createTest");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
