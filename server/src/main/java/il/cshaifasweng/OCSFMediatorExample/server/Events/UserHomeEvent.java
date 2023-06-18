package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.ArrayList;

public class UserHomeEvent {
    ArrayList<Object> id;
    public UserHomeEvent(ArrayList<Object> id) {
        this.id = id;
        System.out.println("in user home event "+id );
    }
    public ArrayList<Object>  getUserID() {
        return id;
    }
}
