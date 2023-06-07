package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

public class TeacherExecuteExamEvent {
    private ScheduledTest scheduledTest;
    public ScheduledTest getScheduledTest(){
        return scheduledTest;
    }
    public TeacherExecuteExamEvent(ScheduledTest scheduledTest){
        this.scheduledTest = scheduledTest;
    }
}
