package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "QuestionScore")
public class Question_Score implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "examForm_id")
    private ExamForm examForm;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "questionScore", cascade = CascadeType.ALL)
    private List <Question_Answer> questionAnswers;
    @Column(name = "student_note", columnDefinition = "TEXT")
    private String student_note;
    @Column(name = "teacher_note", columnDefinition = "TEXT")
    private String teacher_note;

    private int score;
    public Question_Score(int score, ExamForm examForm, Question question) {

        this.score = score;
        this.examForm = examForm;
        this.question = question;
        this.questionAnswers = new ArrayList<>();
    }
    public Question_Score(int score, Question question,String teacher_note,String student_note) {

        this.score = score;
        this.question = question;
        this.teacher_note = teacher_note;
        this.student_note =student_note;
        this.questionAnswers = new ArrayList<>();
    }
    public Question_Score(int score,ExamForm examForm ,Question question,String student_note,String teacher_note) {

        this.score = score;
        this.question = question;
        this.teacher_note = teacher_note;
        this.student_note =student_note;
        this.examForm = examForm;
        this.questionAnswers = new ArrayList<>();
    }
    public Question_Score(int score, Question question){
        this.score = score;
        this.question = question;
    }
    public Question_Score(int score){ this.score = score;}
    public Question_Score(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExamForm getExamForm() {
        return examForm;
    }

    public void setExamForm(ExamForm examForm) {
        this.examForm = examForm;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Question_Answer> getQuestionAnswers() {
        //TODO remove the first line!!!!!!!!!
        questionAnswers = new ArrayList<>();
        return questionAnswers;
    }

    public void setQuestionAnswers(List<Question_Answer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public String getStudent_note() {
        return student_note;
    }

    public void setStudent_note(String student_note) {
        this.student_note = student_note;
    }

    public String getTeacher_note() {
        return teacher_note;
    }

    public void setTeacher_note(String teacher_note) {
        this.teacher_note = teacher_note;
    }

    public static List<Question_Score> generateQuestionScores(){
        List<Question_Score> questionScores = new ArrayList<>();
        questionScores.add(new Question_Score(30));
        questionScores.add(new Question_Score(20));
        questionScores.add(new Question_Score(25));
        questionScores.add(new Question_Score(50));
        questionScores.add(new Question_Score(10));
        questionScores.add(new Question_Score(22));
        questionScores.add(new Question_Score(38));
        questionScores.add(new Question_Score(30));

        return  questionScores;
    }
}
