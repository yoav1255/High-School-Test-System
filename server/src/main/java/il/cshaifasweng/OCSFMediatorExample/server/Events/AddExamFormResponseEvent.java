package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class AddExamFormResponseEvent {
    private boolean check;

    public AddExamFormResponseEvent(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }
}
