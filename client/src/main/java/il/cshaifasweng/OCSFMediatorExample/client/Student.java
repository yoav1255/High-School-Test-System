package il.cshaifasweng.OCSFMediatorExample.client;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Student")
public class Student extends User {
    @OneToMany(mappedBy = "student")
    private List<StudentTest> studentTests;

    public Student(String id, String first_name, String last_name, String gender, String email, String password, List<StudentTest> studentTests) {
        super(id, first_name, last_name, gender, email, password);
        this.studentTests = studentTests;
    }
    public Student(){}

    public List<StudentTest> getStudentTests() {
        return studentTests;
    }
    public void setStudentTests(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }
}
