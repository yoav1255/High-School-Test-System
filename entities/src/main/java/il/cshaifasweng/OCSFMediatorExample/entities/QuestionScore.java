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
public class QuestionScore implements Serializable {
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

    private int score;
    public QuestionScore(int score, ExamForm examForm, Question question) {

        this.score = score;
        this.examForm = examForm;
        this.question = question;
    }
    public QuestionScore(int score, Question question){
        this.score = score;
        this.question = question;
    }
    public QuestionScore(int grade){ this.score = grade;}
    public QuestionScore(){}

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

    public static List<QuestionScore> generateQuestionScores(){
        List<QuestionScore> questionScores = new ArrayList<>();
        questionScores.add(new QuestionScore(30));
        questionScores.add(new QuestionScore(20));
        questionScores.add(new QuestionScore(25));
        questionScores.add(new QuestionScore(50));
        questionScores.add(new QuestionScore(10));
        questionScores.add(new QuestionScore(22));
        questionScores.add(new QuestionScore(38));
        questionScores.add(new QuestionScore(30));

        return  questionScores;
    }
}
