package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ScheduledTest")
public class ScheduledTest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private LocalDate date;
    private LocalTime time; //Todo check Time and Date format
    @ManyToOne
    @JoinColumn(name = "examForm_id")
    private ExamForm examForm;
    @OneToMany(mappedBy = "scheduledTest",cascade = CascadeType.ALL)
    private List<StudentTest> studentTests;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private int submissions;
    private int checkedSubmissions;
    private boolean isChecked;
    private int status; // 0 -> not started , 1 -> during test , 2 -> finished
    private int timeLimit;
    private int activeStudents;
    private boolean isComputerTest;


    public ScheduledTest(String id, LocalDate date, LocalTime time,boolean isComputerTest) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.isComputerTest=isComputerTest;
        this.studentTests = new ArrayList<StudentTest>();
        submissions = 0;
        status = 0;
        checkedSubmissions = 0;
        isChecked = false;
        activeStudents = 0;
    }
    public ScheduledTest(){
        this.studentTests = new ArrayList<StudentTest>();
        status = 0;
        checkedSubmissions = 0;
        isChecked = false;
        activeStudents = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

     public void setIschecked(boolean isChecked){
        this.isChecked=isChecked;
     }
    public int getCheckedSubmissions(){
        return checkedSubmissions;
    }

    public void setCheckedSubmissions(int checkedSubmissions) {
        this.checkedSubmissions = checkedSubmissions;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean getIsComputerTest() {
        return isComputerTest;
    }

    public void setIsComputerTest(boolean isComputerTest) {
        this.isComputerTest = isComputerTest;
    }
    public ExamForm getExamForm() {
        return examForm;
    }
    public String getExamFormCode(){
        return examForm.getExamFormCode();
    }

    public void setExamForm(ExamForm examForm) {
        this.examForm = examForm;
        this.timeLimit = examForm.getTimeLimit();
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

    public int getStatus() {
        if(this.status != 0 && this.status!=1){
            this.status =2;
        }
        return status;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(int activeStudents) {
        this.activeStudents = activeStudents;
    }

    public static List<ScheduledTest> GenerateScheduledTests(){
        List<ScheduledTest> scheduledTests = new ArrayList<ScheduledTest>();
        scheduledTests.add(new ScheduledTest("11PO",  LocalDate.of(2023,10,05), LocalTime.of(12,20,0),true));
        scheduledTests.add(new ScheduledTest("2SDE",LocalDate.of(2022,05,03),LocalTime.of(15,0,0),true));
        scheduledTests.add(new ScheduledTest("3D3E",LocalDate.of(2022,07,03),LocalTime.of(14,0,0),true));
        scheduledTests.add(new ScheduledTest("4E3E",LocalDate.of(2022,05,11),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("53ED",LocalDate.of(2022,05,13),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("88U7",LocalDate.of(2022,05,19),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("JE83",LocalDate.of(2022,05,17),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("H37D",LocalDate.of(2022,05,17),LocalTime.of(12,0,0),true));
        scheduledTests.add(new ScheduledTest("NDH7",LocalDate.of(2022,05,25),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("BH37",LocalDate.of(2022,05,30),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("HDG63",LocalDate.of(2022,05,31),LocalTime.of(10,0,0),true));
        scheduledTests.add(new ScheduledTest("12DW",LocalDate.of(2022,06,02),LocalTime.of(10,0,0),true));

        return scheduledTests;
    }
}
