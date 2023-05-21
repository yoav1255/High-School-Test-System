package il.cshaifasweng.OCSFMediatorExample.client.Controllers;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class PrimaryController {
	@FXML
	private Button goToAllStudentsButton;


	@FXML
	void handleGoToAllStudentsButtonClick(ActionEvent event) {
		try {
			App.switchScreen("allStudents");
			SimpleClient.getClient().sendToServer(new CustomMessage("#showAllStudents", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void handleGoHomeButtonClick(ActionEvent event) {
		try {
			App.switchScreen("primary");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
