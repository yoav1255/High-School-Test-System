package il.cshaifasweng.OCSFMediatorExample.client;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Teacher")
public class Teacher extends Person{
    @ManyToMany(mappedBy = "teachers")
    private List <Subject> subjects;
    @ManyToMany(mappedBy = "teachers")
    private List <Course> courses;
    @OneToMany(mappedBy = "teacher")
    private List<ScheduledTest> scheduledTests;


}
