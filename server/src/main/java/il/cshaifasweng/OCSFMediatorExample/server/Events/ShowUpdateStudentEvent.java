package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;

//
public class ShowUpdateStudentEvent {
    private StudentTest studentTest;
    public ShowUpdateStudentEvent(StudentTest studentTest) {
        this.studentTest = studentTest;
    }
    public StudentTest getStudentTest() {
        return studentTest;
    }
}
