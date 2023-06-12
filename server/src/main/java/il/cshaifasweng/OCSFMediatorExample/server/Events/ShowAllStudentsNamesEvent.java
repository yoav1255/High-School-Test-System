package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Student;

import java.util.List;

public class ShowAllStudentsNamesEvent {
    private List<Student> studentList;
    public ShowAllStudentsNamesEvent(List<Student> studentList) {
        this.studentList = studentList;
    }
    public List<Student> getStudentList() {
        return studentList;
    }
}
