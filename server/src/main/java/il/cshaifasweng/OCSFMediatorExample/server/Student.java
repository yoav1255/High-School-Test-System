package il.cshaifasweng.OCSFMediatorExample.server;
import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Student")
public class Student extends User implements Serializable{

    @OneToMany(mappedBy = "student")
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
        newStudents.add(new Student("1","yoyo@gmail.com","Eitan","Male","David","123123Y"));
        newStudents.add(new Student("2","eldadM@gmail.com","Eldad","Male","Moshe","2233PPP"));
        newStudents.add(new Student("3","danM@gmail.com","Dan","Male","David","123123Y"));
        newStudents.add(new Student("4","adiL@gmail.com","Adi","Female","Levi","11554ss"));
        newStudents.add(new Student("5","liorc@gmail.com","Lior","Female","Cohen","345yyy"));
        newStudents.add(new Student("6","shaniD@gmail.com","Shani","feMale","David","31ffdd"));
        newStudents.add(new Student("7","yaakovB@gmail.com","Yaakov","Male","Ben-Dor","1pppds2"));
        newStudents.add(new Student("8","lanaF@gmail.com","Lana","Female","Franco","2332211dd"));
        newStudents.add(new Student("9","leaP@gmail.com","Lea","Female","Pomeran","99ol9o"));
        newStudents.add(new Student("10","matanP@gmail.com","Matan","Male","Polik","yo2s3r"));
        newStudents.add(new Student("11","orenG@gmail.com","Oren","Male","Golan","123123"));
        newStudents.add(new Student("12","meitarY@gmail.com","Meitar","Female","Yeruham","9o8i7us"));
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