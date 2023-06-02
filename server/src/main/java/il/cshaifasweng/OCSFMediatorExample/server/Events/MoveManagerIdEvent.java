package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class MoveManagerIdEvent {
    private String id;

    public MoveManagerIdEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
