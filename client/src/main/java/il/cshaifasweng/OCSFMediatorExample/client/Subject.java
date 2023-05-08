package il.cshaifasweng.OCSFMediatorExample.client;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;
    private String name;
    @OneToMany(mappedBy = "subject")
    private List<Course> courses;
    @ManyToMany
    @JoinTable(name="teachers_subjects",
            joinColumns = @JoinColumn(name="subject_code"),
            inverseJoinColumns = @JoinColumn(name="teacher_code"))
    private List<Teacher> teachers;
    @OneToMany(mappedBy = "subject")
    private List<Question> questions;
    @OneToMany(mappedBy = "subject")
    private List<ExamForm> examForms;

    public Subject(int code, String name) {
        this.code = code;
        this.name = name;
        this.courses = new ArrayList<Course>();
        this.teachers = new ArrayList<Teacher>();
        this.questions = new ArrayList<Question>();
        this.examForms = new ArrayList<ExamForm>();
    }
    public Subject(){}

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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    public void addCourse(Course course){ courses.add(course);}

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    public void addTeacher(Teacher teacher) { this.teachers.add(teacher); }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<ExamForm> getExamForms() {
        return examForms;
    }

    public void setExamForms(List<ExamForm> examForms) {
        this.examForms = examForms;
    }
    public void addExamForm(ExamForm examForm) { this.examForms.add(examForm); }

    public static List<Subject> GenerateSubjects(){
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject(1,"Math"));
        return subjects;
    }
}
