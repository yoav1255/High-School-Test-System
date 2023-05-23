package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;

public class TeacherFromIdEvent {
    private Teacher teacher;
    public TeacherFromIdEvent(Teacher teacher) {
        this.teacher = teacher;
    }
    public Teacher getTeacherFromId() {
        return teacher;
    }
}
