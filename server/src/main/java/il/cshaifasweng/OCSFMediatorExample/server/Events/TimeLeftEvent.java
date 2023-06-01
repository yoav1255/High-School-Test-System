package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class TimeLeftEvent {
    private long timeLeft;

    public TimeLeftEvent(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}
