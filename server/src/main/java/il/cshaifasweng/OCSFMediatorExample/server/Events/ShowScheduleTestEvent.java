package il.cshaifasweng.OCSFMediatorExample.server.Events;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;

import java.util.List;

public class ShowScheduleTestEvent {
    private List<ScheduledTest> scheduledTestList;
    public ShowScheduleTestEvent(List<ScheduledTest> scheduledTestList) {
        this.scheduledTestList = scheduledTestList;
    }
    public List<ScheduledTest> getScheduledTestList() {
        return scheduledTestList;
    }
}
