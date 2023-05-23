package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.CustomMessage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class PrimaryController {
	@FXML
	private Button goToAllStudentsButton;

	@FXML
	private Button createQuestionBN;


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

	@FXML
	void handleShowScheduleTest(ActionEvent event) throws IOException {
		App.switchScreen("showScheduleTest");
		Platform.runLater(() -> {
			try {
				SimpleClient.getClient().sendToServer(new CustomMessage("#showScheduleTest", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

		@FXML
		void handleCreateQuestionButtonClick (ActionEvent event){
			try {
				String teacherId = "1";
				SimpleClient.getClient().sendToServer(new CustomMessage("#getSubjects", teacherId));
				// TODO : send online teacher's id);
				App.switchScreen("createQuestion");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

