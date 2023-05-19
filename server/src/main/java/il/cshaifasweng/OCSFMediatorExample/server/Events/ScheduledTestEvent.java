package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ExamForm;

import java.util.List;

public class ScheduledTestEvent {
    private ExamForm examForm;

    public ScheduledTestEvent(ExamForm examForm){this.examForm=examForm;}
    public ExamForm getScheduledTestEvent(){return examForm;}

}
