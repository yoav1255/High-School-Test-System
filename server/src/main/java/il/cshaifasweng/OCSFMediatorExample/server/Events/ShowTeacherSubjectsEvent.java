package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Subject;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;

import java.util.List;

public class ShowTeacherSubjectsEvent {
    private List<Subject> subjects;

    public ShowTeacherSubjectsEvent(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public List<Subject> getSubjects() {
        return subjects;
    }
}
