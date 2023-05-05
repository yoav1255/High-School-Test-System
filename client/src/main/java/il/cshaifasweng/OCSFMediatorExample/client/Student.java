package il.cshaifasweng.OCSFMediatorExample.client;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Student")
public class Student extends Person{
    @OneToMany(mappedBy = "student")
    private List<StudentTest> studentTests;

    // TODO: add courses???
}
