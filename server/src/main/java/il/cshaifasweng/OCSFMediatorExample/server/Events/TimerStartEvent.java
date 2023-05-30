package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

public class TimerStartEvent {
    private ScheduledTest scheduledTest;

    public TimerStartEvent(ScheduledTest scheduledTest) {
        this.scheduledTest = scheduledTest;
    }

    public ScheduledTest getScheduledTest() {
        return scheduledTest;
    }
}
