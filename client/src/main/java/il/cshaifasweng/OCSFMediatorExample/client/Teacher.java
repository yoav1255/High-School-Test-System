package il.cshaifasweng.OCSFMediatorExample.client;
import javax.persistence.*;
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

    public Teacher(String id, String first_name, String last_name, String gender, String email, String password, List<Subject> subjects, List<Course> courses, List<ScheduledTest> scheduledTests) {
        super(id, first_name, last_name, gender, email, password);
        this.subjects = subjects;
        this.courses = courses;
        this.scheduledTests = scheduledTests;
    }
    public Teacher(){}
}
