package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowAllStudentsNamesEvent {
    private List<String> studentList;
    public ShowAllStudentsNamesEvent(List<String> studentList) {
        this.studentList = studentList;
    }
    public List<String> getStudentList() {
        return studentList;
    }
}
