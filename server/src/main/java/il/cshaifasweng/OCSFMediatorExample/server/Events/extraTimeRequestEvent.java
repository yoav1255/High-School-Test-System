package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

public class extraTimeRequestEvent {
    private int extraMinutes;
    private String msg;
    private ScheduledTest scheduledTest;
    public extraTimeRequestEvent(int extraMinutes, String msg, ScheduledTest scheduledTest) {
        this.extraMinutes = extraMinutes;
        this.msg = msg;
        this.scheduledTest = scheduledTest;
    }

    public int getExtraMinutes() {
        return extraMinutes;
    }

    public String getMsg() {
        return msg;
    }

    public ScheduledTest getScheduledTest() {
        return scheduledTest;
    }

    public void setExtraMinutes(int extraMinutes) {
        this.extraMinutes = extraMinutes;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setScheduledTest(ScheduledTest scheduledTest) {
        this.scheduledTest = scheduledTest;
    }
}
