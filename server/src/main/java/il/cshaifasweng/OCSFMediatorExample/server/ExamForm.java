package il.cshaifasweng.OCSFMediatorExample.server;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ExamForm")
public class ExamForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String code; //TODO construct the code properly
    private int timeLimit;
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
    @OneToMany(mappedBy = "examForm")
    private List<ScheduledTest> scheduledTests;
    @ManyToMany(mappedBy = "examForms")
    private List<Question> questions;

    public ExamForm(String code, int timeLimit) {
        this.code = code;
        this.timeLimit = timeLimit;
        this.scheduledTests = new ArrayList<ScheduledTest>();
        this.questions = new ArrayList<Question>();
    }
    public ExamForm(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<ScheduledTest> getScheduledTests() {
        return scheduledTests;
    }
    public void setScheduledTests(List<ScheduledTest> scheduledTests) {
        this.scheduledTests = scheduledTests;
    }
    public void addScheduledTest(ScheduledTest scheduledTest){ this.scheduledTests.add(scheduledTest); }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public String getCourseName(){
        return course.getName();
    }
    public String getSubjectName(){
        return subject.getName();
    }
    public String getExamFormCode(){
        return code;
    }

    public static List<ExamForm> GenerateExamForms(){
        List<ExamForm> examForms = new ArrayList<ExamForm>();
        examForms.add(new ExamForm("1",180));
        examForms.add(new ExamForm("2",120));
        examForms.add(new ExamForm("3",120));
        examForms.add(new ExamForm("4",180));
        examForms.add(new ExamForm("5",90));
        examForms.add(new ExamForm("6",120));
        examForms.add(new ExamForm("7",90));
        examForms.add(new ExamForm("8",180));
        examForms.add(new ExamForm("9",90));
        examForms.add(new ExamForm("10",180));
        examForms.add(new ExamForm("11",180));
        examForms.add(new ExamForm("12",120));
        examForms.add(new ExamForm("13",180));

        return examForms;
    }
}
