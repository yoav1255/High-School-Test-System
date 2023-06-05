package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;

import java.util.List;

public class ShowStudentFromScheduleEvent {
    List<StudentTest> studentTests;

    public ShowStudentFromScheduleEvent(List<StudentTest> studentTests) {
        this.studentTests = studentTests;
    }

    public List<StudentTest> getStudentTests() {
        return studentTests;
    }
}
