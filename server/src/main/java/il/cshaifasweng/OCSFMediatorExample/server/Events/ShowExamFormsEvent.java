package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;

import java.util.List;

public class ShowExamFormsEvent {
    List<ExamForm> examForms;

    public ShowExamFormsEvent(List<ExamForm> examForms) {
        this.examForms = examForms;
    }

    public List<ExamForm> getExamForms() {
        return examForms;
    }
}
