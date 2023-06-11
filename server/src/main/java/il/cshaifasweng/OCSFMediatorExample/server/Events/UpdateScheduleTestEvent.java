package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class UpdateScheduleTestEvent {
    private boolean check;

    public UpdateScheduleTestEvent(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }
}
