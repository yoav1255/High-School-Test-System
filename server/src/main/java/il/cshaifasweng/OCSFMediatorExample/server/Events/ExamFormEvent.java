package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;

import java.util.List;

public class ExamFormEvent {
        private List<String> examFormList;

    public ExamFormEvent(List<String> examFormList) {
        System.out.println("I set ExamFprmEvent");
        this.examFormList = examFormList;
    }

    public List<String> getExamFormEventCode() {
        System.out.println("I get ExamFprmEvent");
        return examFormList;
    }


}
