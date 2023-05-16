package il.cshaifasweng.OCSFMediatorExample.client.Events;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;

import java.util.List;

public class ShowAllStudentsEvent {
    private List<Student> studentList;
    public ShowAllStudentsEvent(List<Student> studentList) {
        this.studentList = studentList;
    }
    public List<Student> getStudentList() {
        return studentList;
    }
}
