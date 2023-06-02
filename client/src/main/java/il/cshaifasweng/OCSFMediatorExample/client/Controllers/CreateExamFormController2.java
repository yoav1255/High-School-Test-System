package il.cshaifasweng.OCSFMediatorExample.client.Controllers;

import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
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

        if(examForm!=null){ // We are in update mode
            Platform.runLater(()->{
                ComboSubject.setValue(examForm.getSubjectName());
                timeLimit.setText(Integer.toString( examForm.getTimeLimit()));
            });
        }
    }
    @FXML
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowUpdateExamFormEvent(ShowUpdateExamFormEvent event){
        List<Object> teacherAndExam = event.getSetTeacherAndExam();
        teacherId = teacherAndExam.get(0).toString();
        examForm = (ExamForm) teacherAndExam.get(1);
    }

    @FXML
    public void onSelectSubject(ActionEvent event) {
        try {
            String subjectName = ComboSubject.getValue();
            SimpleClient.getClient().sendToServer(new CustomMessage("#getCourses", subjectName));
            Platform.runLater(()->{
                ComboCourse.setDisable(false);
                ComboCourse.setValue("");
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

        if(examForm!=null){ // We are in update mode
            Platform.runLater(()->{
                ComboCourse.setValue(examForm.getCourseName());
            });
        }
    }
    @FXML
    public void onSelectCourse(ActionEvent event) {
        try {
            String courseName = ComboCourse.getValue();
//            selectedQuestionsListView.setItems(null);
//            questionsListView.setItems(null);
//            questionList.clear();
//            questionScoreList.clear();

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
            System.out.println(questionList.get(0).getText());
            ObservableList<Question> questions1 = FXCollections.observableArrayList(questionList);
            questionsListView.setItems(questions1);

//            Platform.runLater(()->{

                questionsListView.setCellFactory(param -> new ListCell<Question>() {
                    @Override
                    protected void updateItem(Question question, boolean empty) {
                        super.updateItem(question, empty);
                        if (empty || question == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            VBox vbox = new VBox();
                            Label questionText = new Label(question.getText());
                            vbox.getChildren().add(questionText);

                            // Add the answers as separate labels in the VBox

                            Label answerLabel0 = new Label("1.      "+question.getAnswer0());
                            if (question.getIndexAnswer() == 0) {
                                answerLabel0.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                            }
                            vbox.getChildren().add(answerLabel0);

                            Label answerLabel1 = new Label("2.      "+question.getAnswer1());
                            if (question.getIndexAnswer() == 1) {
                                answerLabel1.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                            }
                            vbox.getChildren().add(answerLabel1);

                            Label answerLabel2 = new Label("3.      "+question.getAnswer2());
                            if (question.getIndexAnswer() == 2) {
                                answerLabel2.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                            }
                            vbox.getChildren().add(answerLabel2);

                            Label answerLabel3 = new Label("4.      "+question.getAnswer3());
                            if (question.getIndexAnswer() == 3) {
                                answerLabel3.setStyle("-fx-font-weight: bold; -fx-background-color: green;");
                            }
                            vbox.getChildren().add(answerLabel3);

                            setGraphic(vbox);

                        }
                    }
                });
//            });



            courseChanged++;
//            if(examForm!=null){ //We are in update mode
//                //TODO: run later??
//                if(courseChanged==1){ // course has not changed since initialization with values
//                    Platform.runLater(()->{
//                        try {
//                            SimpleClient.getClient().sendToServer(new CustomMessage("#getQuestionScores",examForm));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    });
//                }
//            }
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
            labelMsg.setVisible(true);
            try {
                timeLim = Integer.parseInt(timeLimit.getText());
            }catch (NumberFormatException notNum){
                labelMsg.setText("Time limit invalid!");
            }
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
                examForm.setGeneralNotes(generalNotes.getText());
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
                    //JOptionPane.showMessageDialog(null, "Exam Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    void handleBackButtonClick(ActionEvent event) {
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
    void handleGoHomeButtonClick(ActionEvent event) {
        try{
            App.switchScreen("teacherHome");
            Platform.runLater(()->{
                EventBus.getDefault().post(new MoveIdToNextPageEvent(teacherId));
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

@FXML
    public void addSelectedQuestion(ActionEvent event) {
    Question selectedQuestion = questionsListView.getSelectionModel().getSelectedItem();
    if (selectedQuestion != null) {
        openDialog(selectedQuestion,false,null);
        updateSelectedListView();
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
        selectedQuestionsListView.setCellFactory(param -> new ListCell<Question_Score>() {
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

                    Label score = new Label("( " + Integer.toString(question.getScore()) + " points )");
                    vbox.getChildren().add(score);

                    setGraphic(vbox);
                }
            }
        });
    }

    public void openDialog(Question selectedQuestion , boolean isUpdate, Question_Score selectedQuestionScore){
        try {
            Dialog<Question_Score> scoreDialog = new Dialog<>();
            scoreDialog.setTitle("Enter Score and Notes");

            DialogPane dialogPane = scoreDialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Label questionTextLabel = new Label(selectedQuestion.getText());
            TextField scoreField = new TextField();
            TextArea teacherNoteArea = new TextArea();
            TextArea studentNoteArea = new TextArea();
            if(isUpdate){
                Platform.runLater(()->{
                    scoreField.setText(Integer.toString(selectedQuestionScore.getScore()));
                    teacherNoteArea.setText(selectedQuestionScore.getTeacher_note());
                    studentNoteArea.setText(selectedQuestionScore.getStudent_note());
                    System.out.println(teacherNoteArea.getText());
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
                        if(isUpdate) {
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
                    if(!isUpdate) {
                        selectedQuestionsListView.getItems().add(questionScore);
                        //questionScoreList.add(questionScore);
                    }
                });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
