package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Student;

public class SelectedStudentEvent {
    private Student student;

    public SelectedStudentEvent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
