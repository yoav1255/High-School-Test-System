package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class MoveIdTestOverEvent {
    private String id;

    public MoveIdTestOverEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
