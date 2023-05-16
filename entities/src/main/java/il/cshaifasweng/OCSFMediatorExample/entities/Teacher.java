package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Teacher")
public class Teacher extends User {
    @ManyToMany(mappedBy = "teachers")
    private List <Subject> subjects;
    @ManyToMany(mappedBy = "teachers")
    private List <Course> courses;
    @OneToMany(mappedBy = "teacher")
    private List<ScheduledTest> scheduledTests;

    public Teacher(String id, String email, String first_name, String gender, String last_name, String password) {
        super(id, first_name, last_name, gender, email, password);
        this.subjects = new ArrayList<Subject>();
        this.courses = new ArrayList<Course>();
        this.scheduledTests = new ArrayList<ScheduledTest>();
    }
    public Teacher(){}

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public void addSubject(Subject subject){ subjects.add(subject); }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    public void addCourses(Course course){ courses.add(course); }

    public List<ScheduledTest> getScheduledTests() {
        return scheduledTests;
    }

    public void setScheduledTests(List<ScheduledTest> scheduledTests) {
        this.scheduledTests = scheduledTests;
    }
    public void addScheduledTest(ScheduledTest scheduledTest){ scheduledTests.add(scheduledTest); }
    public static List<Teacher> GenerateTeachers(){
        List<Teacher> teachers = new ArrayList<Teacher>();
        teachers.add(new Teacher("1","shosh@gmail.com","Shoshana","Female","Levi","tt2p10"));
        teachers.add(new Teacher("2","riki@gmail.com","Riki","Female","Gal","rikiGal2"));
        teachers.add(new Teacher("3","Gili@gmail.com","Gili","Female","Atari","GOlgol7"));
        teachers.add(new Teacher("4","arikush@gmail.com","Arik","male","Rozen","65arik"));
        teachers.add(new Teacher("5","eduard@gmail.com","Eduard","male","Luy","opjgg"));
        teachers.add(new Teacher("6","shani@gmail.com","Shani","Female","Abutbul","lolo"));
        teachers.add(new Teacher("7","meirav@gmail.com","Meirav","Female","Segal","u86g"));

        return teachers;
    }
}
