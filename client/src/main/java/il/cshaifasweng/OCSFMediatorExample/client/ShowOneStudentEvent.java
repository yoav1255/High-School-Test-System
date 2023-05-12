package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.Student;

import java.util.List;

public class ShowOneStudentEvent {
    private Student student;
    public ShowOneStudentEvent(Student student) {
        this.student = student;
    }
    public Student getStudent() {
        return student;
    }
}
