package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.record.HCenterRecord;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class StudentExecuteExamLOCALController implements Serializable {


    @FXML
    private GridPane StudentsGR;
    @FXML
    private Button homeBN;
    @FXML
    private Label text_Id;
    @FXML
    private Label timeLeftText;
    @FXML
    private Button submitButton;
    @FXML
    private List<ToggleGroup> toggleGroups = new ArrayList<>();
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private ListView<Question_Answer> questionsListView;
    @FXML
    private TextArea inputFileTextBox;
    @FXML
    private Label errorLabel;

    private String id;
    private ScheduledTest scheduledTest;
    private StudentTest studentTest;
    private List<Question_Score> questionScoreList;
    private Student student;
    private List<Question_Answer> questionAnswers ;
    private long timeLeft;
//    private List<TextField> q_notes;
    private TestFile final_file;



    public StudentExecuteExamLOCALController() {
        EventBus.getDefault().register(this);
    }

    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize(){
        errorLabel.setVisible(false);
        App.getStage().setOnCloseRequest(event -> {
            ArrayList<String> info = new ArrayList<>();
            info.add(id);
            info.add("student");
            try {
                SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        });
    }

    @Subscribe
    public void onSelectedStudentEvent(SelectedStudentEvent event){
        student =event.getStudent();
        id = student.getId();
        Platform.runLater(() -> {
            text_Id.setText(text_Id.getText() + student.getFirst_name() + " " + student.getLast_name());
        });
        studentTest = new StudentTest();
        studentTest.setStudent(student);
        student.getStudentTests().add(studentTest);
    }

    @Subscribe
    public void onSelectedTestEvent(SelectedTestEvent event) throws IOException {
        scheduledTest = event.getSelectedTestEvent();

        SimpleClient.getClient().sendToServer(new CustomMessage("#updateSubmissions_Active_Start",scheduledTest.getId()));

        questionScoreList = scheduledTest.getExamForm().getQuestionScores();

    }

    @FXML
    public void submitTestBtn(ActionEvent event) throws IOException {
        //TODO validation checks
        //endTest();
        if(final_file == null){
            errorLabel.setVisible(true);
            return;
        }
        System.out.println(final_file.getFileName() + " " + final_file.getStudentID());
        System.out.println("submit local test file to server");

        SimpleClient.getClient().sendToServer(new CustomMessage("#updateSubmissions_Active_Finish",scheduledTest.getId()));

        studentTest.setTimeToComplete(scheduledTest.getTimeLimit()-timeLeft);
        studentTest.setScheduledTest(scheduledTest);


        SimpleClient.getClient().sendToServer(new CustomMessage("#updateStudentTest",studentTest));
        SimpleClient.getClient().sendToServer(new CustomMessage("#endLocalTest", final_file));

    }


    @Subscribe
    public void onShowSuccessEvent(ShowSuccessEvent event) throws IOException {
        cleanup();
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Exam Submitted Successfully");
            alert.showAndWait();
            try {
                App.switchScreen("studentHome");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()->{
                EventBus.getDefault().post(new MoveIdToNextPageEvent(student.getId()));
            });
        });
    }

    @Subscribe
    public void onTimerStartEvent(TimerStartEvent event){ // not necessary
        if(event.getScheduledTest().getId().equals(scheduledTest.getId()))
        {
            System.out.println(" on schedule test "+ scheduledTest.getId() + " timer started ");
        }
    }

    @Subscribe
    public void onTimeLeftEvent(TimeLeftEvent event){
        List<Object> scheduleTestId_timeLeft = event.getScheduleTestId_timeLeft();
        timeLeft = (long)scheduleTestId_timeLeft.get(1);
        String scheduleTestId = (String) scheduleTestId_timeLeft.get(0);

        if(scheduleTestId.equals(scheduledTest.getId())) {
            Platform.runLater(() -> {
                timeLeftText.setText(Long.toString(timeLeft));
            });
        }
    }
    @Subscribe
    public void onTimerFinishedEvent(TimerFinishedEvent event) throws IOException {
        if(event.getScheduledTest().getId().equals(scheduledTest.getId())) {
            System.out.println(" on schedule test " + scheduledTest.getId() + " timer FINISHED ");
            studentTest.setOnTime(false);

            SimpleClient.getClient().sendToServer(new CustomMessage("#updateSubmissions_Active_Finish", scheduledTest.getId()));

            studentTest.setTimeToComplete(scheduledTest.getTimeLimit() - timeLeft);
            studentTest.setScheduledTest(scheduledTest);
//
            SimpleClient.getClient().sendToServer(new CustomMessage("#updateStudentTest", studentTest));
            SimpleClient.getClient().sendToServer(new CustomMessage("#endLocalTest", null));

        }
    }
    @Subscribe
    public void onManagerExtraTimeEvent(ManagerExtraTimeEvent event) {
        Platform.runLater(() -> {
            List<Object> objectList = event.getData();
            if (objectList.get(3).equals(scheduledTest.getId())){
                if ((int)objectList.get(2) != 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("The teacher added " + objectList.get(2) + " minutes to the test!");
                    alert.show();
                }
            }
        });
    }

    public void handleDownloadButton(ActionEvent actionEvent) {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setFontFamily("Arial");
        titleRun.setFontSize(15);
        titleRun.setBold(true);
        titleRun.setText(scheduledTest.getSubjectName() + ", " + scheduledTest.getCourseName());
        titleRun.setText(", " + scheduledTest.getTeacherName());
        //titleRun.setText("Local Test Format");
        titleRun.addBreak();
        titleRun.setText("Exam Form Code : " + scheduledTest.getExamFormCode());
        titleRun.setText(", For student " + student.getFirst_name() + " " + student.getLast_name());
        titleRun.addBreak();
        int counter = 1;

        for (Question_Score questionScore : questionScoreList) {
            Question_Answer questionAnswer = new Question_Answer();
            questionAnswer.setStudentTest(studentTest);
            questionAnswer.setQuestionScore(questionScore);
            questionAnswer.setAnswer(-1); // Initialize with no answer selected
            questionScore.getQuestionAnswers().add(questionAnswer);
            questionAnswers.add(questionAnswer);
            Question_Score qs = questionAnswer.getQuestionScore();
            Question q = qs.getQuestion();
            String questionText = q.getText();
            String answer0 = q.getAnswer0();
            String answer1 = q.getAnswer1();
            String answer2 = q.getAnswer2();
            String answer3 = q.getAnswer3();

            XWPFParagraph questionParagraph = document.createParagraph();
            XWPFRun questionRun = questionParagraph.createRun();
            questionRun.setFontFamily("Arial");
            questionRun.setBold(true);
            String score = String.valueOf(questionAnswer.getQuestionScore().getScore());
            questionRun.setText(counter + ".  " + questionText + " (" + score + ").");
            questionRun.addBreak();
            XWPFRun questionRun2 = questionParagraph.createRun();
            questionRun2.setFontFamily("Arial");
            questionRun2.setBold(false);
            questionRun2.setText("  1.  " + answer0);
            questionRun2.addBreak();
            questionRun2.setText("  2.  " + answer1);
            questionRun2.addBreak();
            questionRun2.setText("  3.  " + answer2);
            questionRun2.addBreak();
            questionRun2.setText("  4.  " + answer3);
            if(!Objects.equals(qs.getStudent_note(), "")){
                questionRun2.addBreak();
                questionRun2.setText("Teacher note : " + qs.getStudent_note());
            }
            questionRun2.addBreak();
            questionRun2.addBreak();
            counter++;
        }

        try {
            String filename = "test" + scheduledTest.getExamFormCode() + "for" + student.getId() + ".docx";
            FileOutputStream output = new FileOutputStream(filename);
            document.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleChooseFileButton(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX Files (*.docx)", "*.docx");
        fileChooser.getExtensionFilters().add(docxFilter);
        File selectedFile = fileChooser.showOpenDialog(App.getStage());
        if(selectedFile == null){return;}
        inputFileTextBox.setText(selectedFile.getName());
        TestFile test = new TestFile();
        test.setStudentID(student.getId());
        test.setFileName("test" + scheduledTest.getExamFormCode() + "for" + student.getId() + ".docx");
        test.setTestCode(scheduledTest.getId());
        byte[] fileData = Files.readAllBytes(selectedFile.toPath());
        test.setFileData(fileData);
        final_file = test;
    }

    public void handleLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LOGOUT");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit test and logout ?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            ArrayList<String> info = new ArrayList<>();
            info.add(id);
            info.add("student");
            SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        } else {
            alert.close();
        }
    }

    public void handleBackButtonClick(ActionEvent actionEvent) { Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT");
        alert.setHeaderText(null);
        alert.setContentText("Cant go back, please submit test");
        ButtonType yesButton = new ButtonType("return");
        alert.getButtonTypes().setAll(yesButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton){
            alert.close();
        }
    }

    public void handleGoHomeButtonClick(ActionEvent actionEvent) {
        handleBackButtonClick(null);
    }
}


