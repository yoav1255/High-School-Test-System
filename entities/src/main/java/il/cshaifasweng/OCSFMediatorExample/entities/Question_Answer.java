package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Question_Answer")
public class Question_Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionScore question;
    @ManyToOne
    @JoinColumn(name = "studentTest_id")
    private StudentTest studentTest;
    private int answer;

    public Question_Answer(QuestionScore question, StudentTest studentTest, int answer) {
        this.question = question;
        this.studentTest = studentTest;
        this.answer = answer;
    }

    public Question_Answer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuestionScore getQuestion() {
        return question;
    }

    public void setQuestion(QuestionScore question) {
        this.question = question;
    }

    public StudentTest getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(StudentTest studentTest) {
        this.studentTest = studentTest;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
