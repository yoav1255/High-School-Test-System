package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ExamForm")
public class ExamForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code; //TODO handle code properly!!
    private int timeLimit;

    @Column(name = "general_notes", columnDefinition = "TEXT")
    private String generalNotes;
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
    @OneToMany(mappedBy = "examForm")
    private List<ScheduledTest> scheduledTests;
    @OneToMany(mappedBy = "examForm", cascade = CascadeType.ALL)
    private List<Question_Score> questionScores;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    public ExamForm(String code, int timeLimit) {
        this.code = code;
        this.timeLimit = timeLimit;

        this.scheduledTests = new ArrayList<ScheduledTest>();
        this.questionScores = new ArrayList<Question_Score>();
    }

    public ExamForm(String code, int timeLimit, String generalNotes, Course course, Subject subject, Teacher teacher) {
        this.code = code;
        this.timeLimit = timeLimit;
        this.generalNotes = generalNotes;
        this.course = course;
        this.subject = subject;
        this.scheduledTests = new ArrayList<>();
        this.questionScores = new ArrayList<>();
        this.teacher = teacher;
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

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        course.setExamInstances(course.getExamInstances()+1);
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

    public List<Question_Score> getQuestionScores() {
        return questionScores;
    }

    public void setQuestionScores(List<Question_Score> questionScores) {
        this.questionScores = questionScores;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List <String> getListExamFormCodes(){
        List<String> exam=getListExamFormCodes();
        return exam;
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
