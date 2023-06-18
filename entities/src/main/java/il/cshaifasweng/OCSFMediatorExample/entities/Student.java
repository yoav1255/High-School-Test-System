package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Student")
public class Student extends User implements Serializable{

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentTest> studentTests;

    public Student(String id, String email,String first_name, String gender, String last_name, String password) {
        super(id, first_name, last_name, gender, email, password);
        this.studentTests = new ArrayList<StudentTest>();
    }
    public Student(){}

    public List<StudentTest> getStudentTests() {
        return studentTests;
    }
    public void setStudentTests(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }
    public void addStudentTests(StudentTest studentTest) { studentTests.add(studentTest); }

    public static List<Student> GenerateStudents(){
        List<Student> newStudents = new ArrayList<>();
        newStudents.add(new Student("11","yoyo@gmail.com","Eitan","Male","David","s1"));
        newStudents.add(new Student("12","eldadM@gmail.com","Eldad","Male","Moshe","s2"));
        newStudents.add(new Student("13","danM@gmail.com","Dan","Male","David","s3"));
        newStudents.add(new Student("14","adiL@gmail.com","Adi","Female","Levi","s4"));
        newStudents.add(new Student("15","liorc@gmail.com","Lior","Female","Cohen","s5"));
        newStudents.add(new Student("16","shaniD@gmail.com","Shani","FeMale","David","s6"));
        newStudents.add(new Student("17","yaakovB@gmail.com","Yaakov","Male","Ben-Dor","s7"));
        newStudents.add(new Student("18","lanaF@gmail.com","Lana","Female","Franco","s8"));
        newStudents.add(new Student("19","leaP@gmail.com","Lea","Female","Pomeran","s9"));
        newStudents.add(new Student("101","matanP@gmail.com","Matan","Male","Polik","s10"));
        newStudents.add(new Student("102","orenG@gmail.com","Oren","Male","Golan","s11"));
        newStudents.add(new Student("103","meitarY@gmail.com","Meitar","Female","Yeruham","s12"));
//        newStudents.add(new Student("13", "shalom@gmail.com", "Shalom","Haim","male","777"));
//        newStudents.add(new Student("14", "ronen@gmail.com", "ronen","Eta","male","9090"));
//        newStudents.add(new Student("16","oded@gmail.com","Oded","Yor","male","86jih"));
//        newStudents.add(new Student("17","haim@gmail.com","Haim","Cohen","male","8976"));
//        newStudents.add(new Student("18","roei@gmail.com","Roei","Shefer","male","kh7t"));
//        newStudents.add(new Student("19","oron@gmail.com","Oron","Tamar","male","jgi7"));
//        newStudents.add(new Student("20","ronit@gmail.com","Ronit","Ashurov","female","ko97"));

        return newStudents;
    }

}
