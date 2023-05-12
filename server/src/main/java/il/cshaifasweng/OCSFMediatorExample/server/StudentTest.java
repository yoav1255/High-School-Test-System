package il.cshaifasweng.OCSFMediatorExample.server;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "StudentTest")
public class StudentTest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "scheduledTest_id")
    private ScheduledTest scheduledTest;
    private int grade;
    private int timeToComplete; //TODO check how to use Duration properly

    public StudentTest(int id, int grade, int timeToComplete) {
        this.id = id;
        this.grade = grade;
        this.timeToComplete = timeToComplete;
    }
    public StudentTest(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ScheduledTest getScheduledTest() {
        return scheduledTest;
    }

    public void setScheduledTest(ScheduledTest scheduledTest) {
        this.scheduledTest = scheduledTest;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }
    public int getCourseCode(ScheduledTest scheduledTest){
        int course = scheduledTest.getExamForm().getCourse().getCode();
        return course;
    }
    public int  getSubjectCode(ScheduledTest scheduledTest) {
        int subject = scheduledTest.getExamForm().getSubject().getCode();
        return subject;
    }
    public String getTeacherId(ScheduledTest scheduledTest){
        String teacher = scheduledTest.getTeacher().getId();
        return teacher;
    }
    public void setCourse(ScheduledTest scheduledTest,Course course){
        scheduledTest.getExamForm().setCourse(course);
    }
    public void setSubject(ScheduledTest scheduledTest,Subject subject){
        scheduledTest.getExamForm().setSubject(subject);
    }
    public void setTeacher(ScheduledTest scheduledTest,Teacher teacher){
        scheduledTest.setTeacher(teacher);
    }

    public static List<StudentTest> GenerateStudentTests(){
        List<StudentTest> studentTests = new ArrayList<StudentTest>();
        studentTests.add(new StudentTest(1,85,180));
        studentTests.add(new StudentTest(2,90,180));
        studentTests.add(new StudentTest(3,87,140));
        studentTests.add(new StudentTest(4,85,175));
        studentTests.add(new StudentTest(5,49,162));
        studentTests.add(new StudentTest(6,67,100));
        studentTests.add(new StudentTest(7,99,150));
        studentTests.add(new StudentTest(8,100,155));
        studentTests.add(new StudentTest(9,54,180));
        studentTests.add(new StudentTest(10,72,177));
        studentTests.add(new StudentTest(11,83,179));
        studentTests.add(new StudentTest(12,65,140));
        studentTests.add(new StudentTest(13,97,100));
        studentTests.add(new StudentTest(14,57,70));
        studentTests.add(new StudentTest(15,75,68));
        studentTests.add(new StudentTest(16,47,150));
        studentTests.add(new StudentTest(17,66,170));
        studentTests.add(new StudentTest(18,85,132));
        studentTests.add(new StudentTest(19,99,155));
        studentTests.add(new StudentTest(20,93,176));
        studentTests.add(new StudentTest(21,76,120));
        studentTests.add(new StudentTest(22,85,180));
        studentTests.add(new StudentTest(23,83,170));
        studentTests.add(new StudentTest(24,100,180));
        studentTests.add(new StudentTest(25,100,160));

        return studentTests;
    }
}
