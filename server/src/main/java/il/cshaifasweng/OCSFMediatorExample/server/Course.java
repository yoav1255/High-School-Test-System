package il.cshaifasweng.OCSFMediatorExample.server;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;
    private String name;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @OneToMany(mappedBy = "course")
    private List<ExamForm> examForms;
    @ManyToMany(mappedBy = "courses")
    private List<Question> questions;
    @ManyToMany
    @JoinTable(name = "teacher_course",
            joinColumns = @JoinColumn(name="course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers;

    public Course(int code, String name) {
        this.code = code;
        this.name = name;
        this.examForms = new ArrayList<ExamForm>();
        this.questions = new ArrayList<Question>();
        this.teachers = new ArrayList<Teacher>();
    }
    public Course(){}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) { this.subject = subject;}

    public List<ExamForm> getExamForms() {
        return examForms;
    }

    public void setExamForms(List<ExamForm> examForms) {
        this.examForms = examForms;
    }
    public void addExamForm(ExamForm examForm){ this.examForms.add(examForm); }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    public void addTeacher(Teacher teacher){ this.teachers.add(teacher); }
    public static List<Course> GenerateCourses(){
        List<Course> courses= new ArrayList<Course>();
        courses.add(new Course(1,"Hedva"));
        return courses;
    }
}
