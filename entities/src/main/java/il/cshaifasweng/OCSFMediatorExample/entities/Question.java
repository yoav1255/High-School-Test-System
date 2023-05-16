package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Question")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int indexAnswer;
    @ManyToMany
    @JoinTable(name = "question_course",
            joinColumns = @JoinColumn(name="question_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToMany
    @JoinTable(name = "question_examForms",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "examForm_id"))
    private List<ExamForm> examForms;



}
