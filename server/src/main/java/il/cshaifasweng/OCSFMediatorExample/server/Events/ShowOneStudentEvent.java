package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;

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
