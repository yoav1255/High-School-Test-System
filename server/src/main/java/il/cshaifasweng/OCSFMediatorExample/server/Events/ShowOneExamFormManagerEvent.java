package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class ShowOneExamFormManagerEvent {
    private List<Object> setManagerAndExam;

    public ShowOneExamFormManagerEvent(List<Object> setManagerAndExam) {
        this.setManagerAndExam = setManagerAndExam;
    }

    public List<Object> getSetManagerAndExam() {
        return setManagerAndExam;
    }
}
