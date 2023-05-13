package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.Student;
import il.cshaifasweng.OCSFMediatorExample.server.StudentTest;

import java.util.List;

public class ShowOneStudentEvent {
    private List<StudentTest> studentTests;
    public ShowOneStudentEvent(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }
    public List<StudentTest> getStudentTests() {
        return studentTests;
    }
}
