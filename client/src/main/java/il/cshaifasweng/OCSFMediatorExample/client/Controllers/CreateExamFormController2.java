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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class CreateExamFormController2 {

    @FXML
    private Label labelMsg;
    @FXML
    private ComboBox<String> ComboSubject;
    @FXML
    private ComboBox<String> ComboCourse;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button updateButton;

    @FXML
    private ListView<Question> questionsListView;
    @FXML
    private ListView<Question_Score> selectedQuestionsListView;
    @FXML
    private TextField timeLimit;
    @FXML
    private TextArea generalNotes;

    private List<Question> questionList;
    private List<Question_Score> questionScoreList;
    private String ExamCode;
    private Subject sub;
    private Course cour;
    private String teacherId;
    private ExamForm examForm;
    private int courseChanged;
    private Teacher teacher;
    private boolean isUpdate;
    private boolean isUpdateScore;


    public CreateExamFormController2(){ EventBus.getDefault().register(this); }
    public void cleanup() {
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void initialize(){
        ComboCourse.setDisable(true);
        courseChanged=0;
        questionScoreList = new ArrayList<>();
        questionList = new ArrayList<>();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoveIdToNextPageEvent(MoveIdToNextPageEvent event){
        teacherId = event.getId();
        isUpdate=false;
    }

    @Subscribe
    public void onShowOneExamFormEvent(ShowOneExamFormEvent event){
        examForm = (ExamForm) event.getSetTeacherAndExam().get(1);
        teacherId = (String) event.getSetTeacherAndExam().get(0);
        isUpdate=true;
    }

    @Subscribe
    public void onTeacherFromIdEvent(TeacherFromIdEvent event){
        teacher = event.getTeacherFromId();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowTeacherSubjects(ShowTeacherSubjectsEvent event){
        List<Subject> subjects = event.getSubjects();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Subject subject:subjects){
            items.add(subject.getName());
        }
        ComboSubject.setItems(items);

        if(isUpdate){ // We are in update mode
            Platform.runLater(()->{
                ComboSubject.setValue(examForm.getSubjectName());
                timeLimit.setText(Integer.toString( examForm.getTimeLimit()));
                generalNotes.setText(examForm.getGeneralNotes());
            });
        }
    }

    @FXML
    public void onSelectSubject(ActionEvent event) {
        try {
            String subjectName = ComboSubject.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourses", subjectName));
            Platform.runLater(()->{
                ComboCourse.setDisable(false);
                ComboCourse.setValue("");
                selectedQuestionsListView.getItems().clear(); // subject changed
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onShowSubjectCourses(ShowSubjectCoursesEvent event){
        List<Course> courses = event.getCourses();
        if(!courses.isEmpty())
            sub = courses.get(0).getSubject();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Course course:courses){
            items.add(course.getName());
        }
        Platform.runLater(()->{
            ComboCourse.setItems(items);
        });

        if(isUpdate){ // We are in update mode
            Platform.runLater(()->{
                ComboCourse.setValue(examForm.getCourseName());
            });
        }
    }
    @FXML
    public void onSelectCourse(ActionEvent event) {
        try {
            String courseName = ComboCourse.getValue();
            selectedQuestionsListView.getItems().clear(); // course changed

            Platform.runLater(()->{
                try {
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getCourseFromName",courseName));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestions", courseName));
                    SimpleClient.getClient().sendToServer(new CustomMessage("#getExamFormCode",courseName));
                }catch (Exception e){
                    e.printStackTrace();
                }

            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onShowCourseEvent(ShowCourseEvent event){
        this.cour = event.getCourse();
    }
    @Subscribe
    public void onShowCourseQuestions(ShowCourseQuestionsEvent event){
        try {
            questionList = event.getQuestions();
            ObservableList<Question> questions1 = FXCollections.observableArrayList(questionList);
            Platform.runLater(()->{
                questionsListView.setItems(questions1);
            });
            updateQuestionListView();

            if(isUpdate && courseChanged==0) {
                for (Question_Score questionScore : examForm.getQuestionScores()) {
                    selectedQuestionsListView.getItems().add(questionScore);
                    updateSelectedListView();
                }
                updateQuestionListView();
            }
                courseChanged++;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowExamFormQuestionScoresEvent(ShowExamFormQuestionScoresEvent event){
        questionScoreList = event.getQuestionScores();
    }

    @FXML
    public void submitForm(ActionEvent event) {
        try {
            int timeLim = 0;
            int sum = 0;
            try {
                timeLim = Integer.parseInt(timeLimit.getText());
            }catch (NumberFormatException notNum){
                Platform.runLater(()->{
                    labelMsg.setText("Time limit invalid!");
                });
            }
            Platform.runLater(()->{
                labelMsg.setVisible(true);
            });

            if(timeLim<=0 || timeLim>1000)
                labelMsg.setText("Time not allowed!");
            else { // Time is valid
                Random random = new Random();
                int randomNumber = random.nextInt(999) + 1;//TODO change it
                ExamCode = Integer.toString(cour.getCode()) + sub.getCode() + randomNumber;//TODO handle code properly!
                examForm = new ExamForm(ExamCode,timeLim);
                examForm.setSubject(sub);
                examForm.setCourse(cour);
                examForm.setTeacher(teacher);
                examForm.setGeneralNotes(teacher.getFirst_name() + " " + teacher.getLast_name() +" :\n"+generalNotes.getText());
                questionScoreList.clear();
                for(Question_Score questionScore : selectedQuestionsListView.getItems()){
                    Question_Score questionScore1 = new Question_Score(questionScore.getScore(),questionScore.getExamForm(),questionScore.getQuestion(),questionScore.getStudent_note(),questionScore.getTeacher_note());
                    questionScoreList.add(questionScore1);
                }
                examForm.setQuestionScores(questionScoreList);


                for(Question_Score questionScore:questionScoreList){
                    questionScore.setExamForm(examForm);
                    sum+=questionScore.getScore();
                }
                if(sum!=100){
                    labelMsg.setText("Grade must sum to 100!");
                }
                else {
                    cleanup();
                    SimpleClient.getClient().sendToServer(new CustomMessage("#addExamForm", examForm));
//                    SimpleClient.getClient().sendToServer(new CustomMessage("#addQuestionScores", questionScoreList));
//                    JOptionPane.showMessageDialog(null, "Exam Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    App.switchScreen("showExamForms");
                    Platform.runLater(()->{
                        EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoBackButtonClick(ActionEvent event) {
        int input = JOptionPane.showConfirmDialog(null, "Your changes will be lost. Do you wand to proceed?", "Select an Option...",
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (input == JOptionPane.YES_OPTION){
            cleanup();
            try {
                App.switchScreen("showExamForms");
                Platform.runLater(()->{
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


@FXML
    public void addSelectedQuestion(ActionEvent event) {
    Question selectedQuestion = questionsListView.getSelectionModel().getSelectedItem();
    if (selectedQuestion != null) {
        openDialog(selectedQuestion,false,null);
        updateSelectedListView();
        //TODO delete the question from the shown questions
        questionsListView.getItems().remove(selectedQuestion);
        updateQuestionListView();
    }
}

@FXML
    public void updateSelectedQuestion(ActionEvent event) {
        Question_Score selected_Question_Score = selectedQuestionsListView.getSelectionModel().getSelectedItem();
        if (selected_Question_Score != null) {
            Question selectedQuestion = selected_Question_Score.getQuestion();
            openDialog(selectedQuestion,true, selected_Question_Score);
            updateSelectedListView();
        }
    }

@FXML
    public void removeSelectedQuestion(ActionEvent event) {
        int selectedIndex = selectedQuestionsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            selectedQuestionsListView.getItems().remove(selectedIndex);
        }
    }

    public void updateSelectedListView(){
        selectedQuestionsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Question_Score question, boolean empty) {
                super.updateItem(question, empty);
                if (empty || question == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox();
                    // show question text and score
                    Label questionText = new Label(question.getQuestion().getText());
                    vbox.getChildren().add(questionText);

                    Label score = new Label("( " + question.getScore() + " points )");
                    vbox.getChildren().add(score);

                    setGraphic(vbox);
                }
            }
        });
    }
    public void updateQuestionListView(){
        questionsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Question question, boolean empty) {
                super.updateItem(question, empty);
                if (empty || question == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Platform.runLater(() -> {
                        VBox vbox = new VBox();
                        Label questionText = new Label(question.getText());
                        vbox.getChildren().add(questionText);

                        // Add the answers as separate labels in the VBox

                        Label answerLabel0 = new Label("1.      " + question.getAnswer0());
                        if (question.getIndexAnswer() == 0) {
                            answerLabel0.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel0);

                        Label answerLabel1 = new Label("2.      " + question.getAnswer1());
                        if (question.getIndexAnswer() == 1) {
                            answerLabel1.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel1);

                        Label answerLabel2 = new Label("3.      " + question.getAnswer2());
                        if (question.getIndexAnswer() == 2) {
                            answerLabel2.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel2);

                        Label answerLabel3 = new Label("4.      " + question.getAnswer3());
                        if (question.getIndexAnswer() == 3) {
                            answerLabel3.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                        }
                        vbox.getChildren().add(answerLabel3);

                        setGraphic(vbox);

                    });
                }
            }
        });
    }

    public void openDialog(Question selectedQuestion , boolean isUpdateScore, Question_Score selectedQuestionScore){
        try {
            Dialog<Question_Score> scoreDialog = new Dialog<>();
            scoreDialog.setTitle("Enter Score and Notes");

            DialogPane dialogPane = scoreDialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Label questionTextLabel = new Label(selectedQuestion.getText());
            TextField scoreField = new TextField();
            TextArea teacherNoteArea = new TextArea();
            TextArea studentNoteArea = new TextArea();
            if(isUpdateScore){
                Platform.runLater(()->{
                    scoreField.setText(Integer.toString(selectedQuestionScore.getScore()));
                    teacherNoteArea.setText(selectedQuestionScore.getTeacher_note());
                    studentNoteArea.setText(selectedQuestionScore.getStudent_note());
                });
            }
            GridPane gridPane = new GridPane();
            gridPane.add(new Label("Question:"), 0, 0);
            gridPane.add(questionTextLabel, 1, 0);
            gridPane.add(new Label("Score:"), 0, 1);
            gridPane.add(scoreField, 1, 1);
            gridPane.add(new Label("Teacher's Note:"), 0, 2);
            gridPane.add(teacherNoteArea, 1, 2);
            gridPane.add(new Label("Student's Note:"), 0, 3);
            gridPane.add(studentNoteArea, 1, 3);
            dialogPane.setContent(gridPane);
            scoreDialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    try {
                        int score = Integer.parseInt(scoreField.getText());
                        if (score > 100 || score < 0) {
                            //TODO add something
                        }
                        String teacherNote = teacherNoteArea.getText();
                        String studentNote = studentNoteArea.getText();
                        if(isUpdateScore) {
                            selectedQuestionScore.setScore(score);
                            selectedQuestionScore.setStudent_note(studentNote);
                            selectedQuestionScore.setTeacher_note(teacherNote);
                            return selectedQuestionScore;
                        }
                        else {
                            return new Question_Score(score,selectedQuestion,teacherNote,studentNote);
                        }
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            });

                scoreDialog.showAndWait().ifPresent(questionScore -> {
                    if(!isUpdateScore) {
                        selectedQuestionsListView.getItems().add(questionScore);
                        //questionScoreList.add(questionScore);
                    }
                });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

@FXML
    public void handleBackButtonClick(ActionEvent event) {
    try {
        cleanup();
        if (isUpdate) { // return to the show test
            List<Object> setObjectAndExam = new ArrayList<>();
            setObjectAndExam.add(teacherId);
            setObjectAndExam.add(examForm);

            App.switchScreen("showOneExamForm");
            Platform.runLater(() -> {
                try {
                    EventBus.getDefault().post(new ShowOneExamFormEvent(setObjectAndExam));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } else {  // return to all tests
            App.switchScreen("showExamForms");
            Platform.runLater(()->{
                try {
                    EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }catch (Exception e){
        e.printStackTrace();
    }
}


    @FXML
    void handleGoHomeButtonClick(ActionEvent event) {
        try{
            cleanup();
            App.switchScreen("teacherHome");
            Platform.runLater(()->{
                EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LOGOUT");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            ArrayList<String> info = new ArrayList<>();
            info.add(teacherId);
            info.add("teacher");
            SimpleClient.getClient().sendToServer(new CustomMessage("#logout", info));
            System.out.println("Perform logout");
            cleanup();
            javafx.application.Platform.exit();
        } else {
            alert.close();
        }
    }
}
