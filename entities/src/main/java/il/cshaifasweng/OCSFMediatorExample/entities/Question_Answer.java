package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Question_Answer")
public class Question_Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "questionScore_id")
    private Question_Score questionScore;
    @ManyToOne
    @JoinColumn(name = "studentTest_id")
    private StudentTest studentTest;
    private int answer;
    @Column(name = "answer_note", columnDefinition = "TEXT")
    private String note;

    public Question_Answer(Question_Score questionScore, StudentTest studentTest, int answer) {
        this.questionScore = questionScore;
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

    public Question_Score getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(Question_Score questionScore) {
        this.questionScore = questionScore;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
