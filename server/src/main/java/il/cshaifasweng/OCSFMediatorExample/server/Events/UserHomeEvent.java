package il.cshaifasweng.OCSFMediatorExample.server.Events;

public class UserHomeEvent {
    private String id;
    public UserHomeEvent(String id) {
        this.id = id;
        System.out.println("in event "+id );
    }
    public String getUserID() {
        return id;
    }
}