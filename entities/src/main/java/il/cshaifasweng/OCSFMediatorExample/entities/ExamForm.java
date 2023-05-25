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
    private String teacherNotes;
    private String studentNotes;
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
    @OneToMany(mappedBy = "examForm")
    private List<ScheduledTest> scheduledTests;
    @OneToMany(mappedBy = "examForm")
    private List<QuestionScore> questionScores;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    public ExamForm(String code, int timeLimit,String teacherNotes,String studentNotes) {
        this.code = code;
        this.timeLimit = timeLimit;
        this.teacherNotes = teacherNotes;
        this.studentNotes = studentNotes;
        this.scheduledTests = new ArrayList<ScheduledTest>();
        this.questionScores = new ArrayList<QuestionScore>();
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

    public String getTeacherNotes() {
        return teacherNotes;
    }

    public void setTeacherNotes(String teacherNotes) {
        this.teacherNotes = teacherNotes;
    }

    public String getStudentNotes() {
        return studentNotes;
    }

    public void setStudentNotes(String studentNotes) {
        this.studentNotes = studentNotes;
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

    public List<QuestionScore> getQuestionScores() {
        return questionScores;
    }

    public void setQuestionScores(List<QuestionScore> questionScores) {
        this.questionScores = questionScores;
    }
    public String getCourseName(){
        return course.getName();
    }
    public String getSubjectName(){
        return subject.getName();
    }
    public String getExamFormCode(){
        return (code + getCourse().getCode() + getSubject().getCode());
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
        examForms.add(new ExamForm("1",180,"",""));
        examForms.add(new ExamForm("2",120,"",""));
        examForms.add(new ExamForm("3",120,"",""));
        examForms.add(new ExamForm("4",180,"",""));
        examForms.add(new ExamForm("5",90,"",""));
        examForms.add(new ExamForm("6",120,"",""));
        examForms.add(new ExamForm("7",90,"",""));
        examForms.add(new ExamForm("8",180,"",""));
        examForms.add(new ExamForm("9",90,"",""));
        examForms.add(new ExamForm("10",180,"",""));
        examForms.add(new ExamForm("11",180,"",""));
        examForms.add(new ExamForm("12",120,"",""));
        examForms.add(new ExamForm("13",180,"",""));

        return examForms;
    }
}
