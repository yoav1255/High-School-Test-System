package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class MoveIdToNextPageEvent {
    private String id;

    public MoveIdToNextPageEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
