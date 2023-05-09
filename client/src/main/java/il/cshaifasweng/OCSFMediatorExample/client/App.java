package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("primary"), 640, 480);
        App.stage = stage;
        stage.setScene(scene);
        stage.show();

        // -------------- //

    }

    public static void setWindowTitle(String title){stage.setTitle(title);}
    public static void setContent(String pageName) throws IOException{
        Parent root = loadFXML(pageName);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void switchScreen(String screenName){
        switch (screenName){
            case "allStudents":
                Platform.runLater(()->{
                    setWindowTitle("All Students");
                    try {
                        setContent("allStudents");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "student":
                Platform.runLater(()->{
                    setWindowTitle("Student");
                    try {
                        setContent("student");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;

        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}
    
    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});
    	
    }

	public static void main(String[] args) {
        launch();
    }

}