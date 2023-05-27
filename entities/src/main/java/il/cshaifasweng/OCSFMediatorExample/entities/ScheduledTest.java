package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ScheduledTest")
public class ScheduledTest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private LocalDate date;
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

    public ScheduledTest(String id, LocalDate date, Time time, int submissions) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.studentTests = new ArrayList<StudentTest>();
        this.submissions = submissions;
    }
    public ScheduledTest(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
        scheduledTests.add(new ScheduledTest("11PO",  LocalDate.of(2023,10,05),new Time(12,20,0),20));
        scheduledTests.add(new ScheduledTest("2SDE",LocalDate.of(2022,05,03),new Time(15,0,0),15));
        scheduledTests.add(new ScheduledTest("3D3E",LocalDate.of(2022,07,03),new Time(14,0,0),10));
        scheduledTests.add(new ScheduledTest("4E3E",LocalDate.of(2022,05,11),new Time(10,0,0),15));
        scheduledTests.add(new ScheduledTest("53ED",LocalDate.of(2022,05,13),new Time(10,0,0),15));
        scheduledTests.add(new ScheduledTest("88U7",LocalDate.of(2022,05,19),new Time(10,0,0),15));
        scheduledTests.add(new ScheduledTest("JE83",LocalDate.of(2022,05,17),new Time(10,0,0),12));
        scheduledTests.add(new ScheduledTest("H37D",LocalDate.of(2022,05,17),new Time(12,0,0),12));
        scheduledTests.add(new ScheduledTest("NDH7",LocalDate.of(2022,05,25),new Time(10,0,0),12));
        scheduledTests.add(new ScheduledTest("BH37",LocalDate.of(2022,05,30),new Time(10,0,0),12));
        scheduledTests.add(new ScheduledTest("HDG63",LocalDate.of(2022,05,31),new Time(10,0,0),16));
        scheduledTests.add(new ScheduledTest("12DW",LocalDate.of(2022,06,02),new Time(10,0,0),12));

        return scheduledTests;
    }
}
