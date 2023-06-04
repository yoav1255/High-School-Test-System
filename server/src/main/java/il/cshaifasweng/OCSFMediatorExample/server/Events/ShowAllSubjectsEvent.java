package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Subject;

import java.util.List;

public class ShowAllSubjectsEvent {
    private List<Subject> subjects;

    public ShowAllSubjectsEvent(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
