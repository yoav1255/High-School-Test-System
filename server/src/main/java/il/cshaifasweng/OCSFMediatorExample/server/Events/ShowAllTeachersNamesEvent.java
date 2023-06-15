package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;

import java.util.List;

public class ShowAllTeachersNamesEvent {
    private List<Teacher> teacherList;
    public ShowAllTeachersNamesEvent(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }
    public List<Teacher> getTeacherList() {
        return teacherList;
    }
}
