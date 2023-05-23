package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowUpdateExamFormEvent {
    List<Object> setTeacherAndExam;

    public ShowUpdateExamFormEvent(List<Object> setTeacherAndExam) {
        this.setTeacherAndExam = setTeacherAndExam;
    }

    public List<Object> getSetTeacherAndExam() {
        return setTeacherAndExam;
    }
}
