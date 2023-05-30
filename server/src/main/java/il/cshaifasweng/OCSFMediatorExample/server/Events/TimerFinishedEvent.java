package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

public class TimerFinishedEvent {
    private ScheduledTest scheduledTest;

    public TimerFinishedEvent(ScheduledTest scheduledTest) {
        this.scheduledTest = scheduledTest;
    }

    public ScheduledTest getScheduledTest() {
        return scheduledTest;
    }
}
