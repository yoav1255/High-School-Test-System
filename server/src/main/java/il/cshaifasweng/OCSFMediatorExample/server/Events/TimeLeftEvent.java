package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

import java.util.List;

public class TimeLeftEvent {

    private List<Object> scheduleTestId_timeLeft;

    public TimeLeftEvent(List<Object> scheduleTestId_timeLeft) {
        this.scheduleTestId_timeLeft = scheduleTestId_timeLeft;
    }

    public List<Object> getScheduleTestId_timeLeft() {
        return scheduleTestId_timeLeft;
    }
}
