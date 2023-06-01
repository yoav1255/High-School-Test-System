package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "StudentTest")
public class StudentTest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "scheduledTest_id")
    private ScheduledTest scheduledTest;
    @OneToMany(mappedBy = "studentTest" )
    private List<Question_Answer> questionAnswers;

    private int grade;
    private long timeToComplete;
    private boolean isOnTime;
    private boolean isChecked;

    public StudentTest(int id, int grade, long timeToComplete) {
        this.id = id;
        this.grade = grade;
        this.timeToComplete = timeToComplete;
        this.questionAnswers = new ArrayList<>();
        isChecked = false;
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

    public long getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(long timeToComplete) {
        this.timeToComplete = timeToComplete;
    }
    public Course getCourseName(ScheduledTest scheduledTest){
        Course course = scheduledTest.getExamForm().getCourse();
        return course;
    }

    public String getCourseName(){
        String course = scheduledTest.getCourseName();
        return course;
    }
    public String getSubjectName(){
        String subject = scheduledTest.getSubjectName();
        return subject;
    }
    public String getTeacherName(){
        String teacher = scheduledTest.getTeacherName();
        return teacher;
    }
    public String getExamFormCode(){
        return scheduledTest.getExamFormCode();
    }
    public Subject  getSubject(ScheduledTest scheduledTest) {
        Subject subject = scheduledTest.getExamForm().getSubject();
        return subject;
    }
    public Teacher getTeacher(ScheduledTest scheduledTest){
        Teacher teacher = scheduledTest.getTeacher();
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

    public List<Question_Answer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<Question_Answer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public boolean isOnTime() {
        return isOnTime;
    }

    public void setOnTime(boolean onTime) {
        isOnTime = onTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static List<StudentTest> GenerateStudentTests(){
        List<StudentTest> studentTests = new ArrayList<StudentTest>();
        studentTests.add(new StudentTest(1,85,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(2,90,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(3,87,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(4,85,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(5,49,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(6,67,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(7,99,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(8,100,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(9,54,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(10,72,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(11,83,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(12,65,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(13,97,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(14,57,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(15,75,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(16,47,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(17,66,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(18,85,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(19,99,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(20,93,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(21,76,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(22,85,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(23,83,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(24,100,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));
        studentTests.add(new StudentTest(25,100,Duration.ofHours(2).plusMinutes(30).plusSeconds(15).toMillis()));

        return studentTests;
    }
}
