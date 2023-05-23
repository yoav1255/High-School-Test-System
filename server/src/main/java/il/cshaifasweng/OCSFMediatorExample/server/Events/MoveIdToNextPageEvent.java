package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class MoveIdToNextPageEvent {
    private String id;

    public MoveIdToNextPageEvent(String id) {
        System.out.println("In event move id to next page "+id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
