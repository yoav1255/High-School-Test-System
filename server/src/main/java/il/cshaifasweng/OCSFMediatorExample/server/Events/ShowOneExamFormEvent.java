package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowOneExamFormEvent {
    List<Object> setTeacherAndExam;

    public ShowOneExamFormEvent(List<Object> setTeacherAndExam) {
        this.setTeacherAndExam = setTeacherAndExam;
    }

    public List<Object> getSetTeacherAndExam() {
        return setTeacherAndExam;
    }
}
