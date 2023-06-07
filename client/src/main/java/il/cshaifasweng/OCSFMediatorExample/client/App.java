package il.cshaifasweng.OCSFMediatorExample.client;

//import il.cshaifasweng.OCSFMediatorExample.entities.EventBusManager;
import il.cshaifasweng.OCSFMediatorExample.server.Events.WarningEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.stage.StageStyle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private SimpleClient client;
    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        App.scene = scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        App.stage = stage;
    }
    private static int instances = 0;

    public void cleanup() {
        EventBus.getDefault().unregister(this);
        instances--;
    }
    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        instances++;
        System.out.println("register successfully with client");
        client = SimpleClient.getClient();
        client.openConnection();
//        cleanup();
        scene = new Scene(loadFXML("login"), 574, 423);
        App.stage = stage;
        stage.setScene(scene);
        stage.show();


        // -------------- //

    }

    public static void openPopup(String screenName) throws IOException{

        switch (screenName){
            case ("examEntry"):
                Platform.runLater(()->{
                    try {
                        setPopUpContent("examEntry");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        }
    }
    public static void setPopUpContent(String pageName) throws IOException{
        try {
            Parent popupRoot = loadFXML(pageName);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);

            Scene popupScene = new Scene(popupRoot);
            popupStage.setTitle(pageName);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setWindowTitle(String title){stage.setTitle(title);}
    public static void setContent(String pageName) throws IOException{
        Parent root = loadFXML(pageName);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchScreen(String screenName) throws IOException{
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
            case "showOneStudent":
                Platform.runLater(()->{
                    setWindowTitle("Student");
                    try {
                        setContent("showOneStudent");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showStudentTest":
                Platform.runLater(()->{
                    setWindowTitle("show Student Test");
                    try {
                        setContent("showStudentTest");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showUpdateStudent":
                Platform.runLater(()->{
                    setWindowTitle("Update Grade");
                    try {
                        setContent("showUpdateStudent");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "primary":
                Platform.runLater(()->{
                    setWindowTitle("Main");
                    try {
                        setContent("primary");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showExamForms":
                Platform.runLater(()->{
                    setWindowTitle("Exam Forms");
                    try {
                        setContent("showExamForms");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showOneExamForm":
                Platform.runLater(()->{
                    setWindowTitle("Exam Form");
                    try {
                        setContent("showOneExamForm");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "createExamForm":
                Platform.runLater(()->{
                    setWindowTitle("Create Exam Form");
                    try {
                        setContent("createExamForm");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "createExamForm2":
                Platform.runLater(()->{
                    setWindowTitle("Create Exam Form");
                    try {
                        setContent("createExamForm2");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "login":
                Platform.runLater(()->{
                    setWindowTitle("Login");
                    try {
                        setContent("login");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "studentHome":
                Platform.runLater(()->{
                    setWindowTitle("StudentHome");
                    try {
                        setContent("studentHome");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "teacherHome":
                Platform.runLater(()->{
                    setWindowTitle("TeacherHome");
                    try {
                        setContent("teacherHome");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "managerHome":
                Platform.runLater(()->{
                    setWindowTitle("ManagerHome");
                    try {
                        setContent("managerHome");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "scheduledTest":
                Platform.runLater(()->{
                    setWindowTitle("Schedule Test ");
                    try {
                        setContent("scheduledTest");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showScheduleTest":
                Platform.runLater(()->{
                    setWindowTitle("Schedule Test List ");
                    try {
                        setContent("showScheduleTest");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "createQuestion":
                Platform.runLater(()->{
                    setWindowTitle("Create Question Form");
                    try {
                        setContent("createQuestion");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showAllQuestions":
                Platform.runLater(()->{
                    setWindowTitle("All Questions");
                    try {
                        setContent("showAllQuestions");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "studentExecuteExam":
                Platform.runLater(()->{
                    setWindowTitle("Execute Exam");
                    try {
                        setContent("studentExecuteExam");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "studentExecuteExamLOCAL":
                Platform.runLater(()->{
                    setWindowTitle("Execute Local Exam");
                    try {
                        setContent("studentExecuteExamLOCAL");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "examEntry":
                Platform.runLater(()->{
                    setWindowTitle("Enter Exam");
                    try {
                        setContent("examEntry");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "testGrade":
                Platform.runLater(()->{
                    setWindowTitle("Test Grades");
                    try {
                        setContent("testGrade");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case "showStatistics":
                Platform.runLater(()->{
                    setWindowTitle("Statistics");
                    try {
                        setContent("showStatistics");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
        }
//    return loadFXML(screenName);
    }
    //-------------Menu Functions----------//


    //-------------------------------------//

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
        System.out.println("CLIENT SHUT DOWN");
        cleanup();
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