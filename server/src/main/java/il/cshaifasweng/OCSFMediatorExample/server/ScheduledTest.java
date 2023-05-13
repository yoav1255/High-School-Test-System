package il.cshaifasweng.OCSFMediatorExample.server;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ScheduledTest")
public class ScheduledTest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private Time time; //Todo check Time and Date format
    @ManyToOne
    @JoinColumn(name = "examForm_id")
    private ExamForm examForm;
    @OneToMany(mappedBy = "scheduledTest")
    private List<StudentTest> studentTests;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private int submissions;

    public ScheduledTest(int id, Date date, Time time, int submissions) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.studentTests = new ArrayList<StudentTest>();
        this.submissions = submissions;
    }
    public ScheduledTest(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public ExamForm getExamForm() {
        return examForm;
    }
    public String getExamFormCode(){
        return examForm.getExamFormCode();
    }

    public void setExamForm(ExamForm examForm) {
        this.examForm = examForm;
    }

    public List<StudentTest> getStudentTests() {
        return studentTests;
    }

    public void setStudentTests(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }
    public void addStudentTest(StudentTest studentTest){ studentTests.add(studentTest); }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getSubmissions() {
        return submissions;
    }

    public void setSubmissions(int submissions) {
        this.submissions = submissions;
    }
    public String getCourseName(){
        String course = examForm.getCourseName();
        return course;
    }
    public String getSubjectName(){
        String subject = examForm.getSubjectName();
        return subject;
    }
    public String getTeacherName(){
        String teacher = getTeacher().getFirst_name() +" "+ getTeacher().getLast_name();
        return teacher;
    }

    public static List<ScheduledTest> GenerateScheduledTests(){
        List<ScheduledTest> scheduledTests = new ArrayList<ScheduledTest>();
        scheduledTests.add(new ScheduledTest(1,new Date(2023,10,05),new Time(12,20,0),20));
        scheduledTests.add(new ScheduledTest(2,new Date(2022,05,03),new Time(15,0,0),15));
        scheduledTests.add(new ScheduledTest(3,new Date(2022,07,03),new Time(14,0,0),10));
        scheduledTests.add(new ScheduledTest(4,new Date(2022,05,11),new Time(10,0,0),12));

        return scheduledTests;
    }
}
