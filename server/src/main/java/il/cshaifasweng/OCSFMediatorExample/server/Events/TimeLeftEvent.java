package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

import java.util.List;

public class TimeLeftEvent {

    private List<List<Object>> scheduleTestId_timeLeft_List;

    public TimeLeftEvent(List<List<Object>> scheduleTestId_timeLeft) {
        this.scheduleTestId_timeLeft_List = scheduleTestId_timeLeft;
    }

    public List<List<Object>> getScheduleTestId_timeLeft() {
        return scheduleTestId_timeLeft_List;
    }
}
