package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Question")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    private String answer0;
    private String answer1;
    private String answer2;
    private String answer3;
    private int indexAnswer;
    @ManyToMany
    @JoinTable(name = "question_course",
            joinColumns = @JoinColumn(name="question_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Question_Score> questionScores;


    public Question(String text, String answer0, String answer1, String answer2, String answer3, int indexAnswer) {
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer0 = answer0;
        this.indexAnswer = indexAnswer;
        courses = new ArrayList<>();
        questionScores = new ArrayList<>();
    }

    public Question() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer0() {
        return answer0;
    }

    public void setAnswer0(String answer0) {
        this.answer0 = answer0;
    }

    public int getIndexAnswer() {
        return indexAnswer;
    }

    public void setIndexAnswer(int indexAnswer) {
        this.indexAnswer = indexAnswer;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Question_Score> getQuestionScores() {
        return questionScores;
    }

    public void setQuestionScores(List<Question_Score> questionScores) {
        this.questionScores = questionScores;
    }
    public void addQuestionScore(Question_Score questionScore){
        questionScores.add(questionScore);
    }


    public static List<Question> GenerateQuestions(){
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("1+1 = ?", "2","5","11","None",0));
        questions.add(new Question("3+7 = ?", "5","10","2","None",1));
        questions.add(new Question("50-3 = ?", "5","10","47","None",2));
        questions.add(new Question("5-3 = ?", "5","10","2","None",2));
        questions.add(new Question("20+3 = ?", "5","10","20","None",3));
        questions.add(new Question("50-3*7 = ?", "5","15","10","None",3));
        questions.add(new Question("5+10 = ?", "5","15","2","None",1));
        questions.add(new Question("5+15 = ?", "5","20","2","None",1));


        return  questions;
    }
}
