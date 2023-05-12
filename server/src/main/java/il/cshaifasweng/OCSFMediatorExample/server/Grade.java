package il.cshaifasweng.OCSFMediatorExample.server;

public class Grade {
    private int id;
    private String student_id;
    private String test_id;
    private String teacher_id;
    private int subject_id;
    private int course_id;
    private int grade;

    public Grade(int id,String student_id, String test_id, String teacher_id, int subject_id,int course_id, int grade) {
        this.id = id;
        this.student_id=student_id;
        this.test_id = test_id;
        this.teacher_id = teacher_id;
        this.subject_id = subject_id;
        this.course_id = course_id;
        this.grade = grade;
    }
    public Grade(){}
    public Grade(String student_id, String test_id)// create grade row based on student and test
    {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
