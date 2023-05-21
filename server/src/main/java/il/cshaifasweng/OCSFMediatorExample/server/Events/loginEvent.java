package il.cshaifasweng.OCSFMediatorExample.server.Events;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;

import java.util.List;

public class loginEvent {
    private String user_type;
    public loginEvent(String user_type) {
        this.user_type = user_type;
    }
    public String getUserType() {
        return user_type;
    }
}
