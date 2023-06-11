package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class CheckFirstEntryEvent {
    private boolean isFirst;

    public CheckFirstEntryEvent(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isFirst() {
        return isFirst;
    }
}
