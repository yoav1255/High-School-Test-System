package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.StudentTest;

import java.util.List;

public class ShowUpdateStudentEvent {
    private StudentTest studentTest;
    public ShowUpdateStudentEvent(StudentTest studentTest) {
        this.studentTest = studentTest;
    }
    public StudentTest getStudentTest() {
        return studentTest;
    }
}
