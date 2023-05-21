package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;

import java.util.List;

public class ExamFormEvent {
        private List<String> examFormList;

    public ExamFormEvent(List<String> examFormList) {
        this.examFormList = examFormList;
    }

    public List<String> getExamFormEventCode() {
        return examFormList;
    }


}
