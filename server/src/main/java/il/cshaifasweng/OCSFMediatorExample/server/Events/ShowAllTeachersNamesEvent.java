package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowAllTeachersNamesEvent {
    private List<String> teacherList;
    public ShowAllTeachersNamesEvent(List<String> teacherList) {
        this.teacherList = teacherList;
    }
    public List<String> getTeacherList() {
        return teacherList;
    }
}
