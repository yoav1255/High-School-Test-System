package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.ArrayList;
import java.util.List;

public class ManagerExtraTimeEvent {
    List<Object> obj = new ArrayList<>();

    public ManagerExtraTimeEvent(List<Object> obj) {
        this.obj = obj;
    }

    public List<Object> getData() {
        return obj;
    }

    public void setDecision(List<Object> obj) {
        this.obj = obj;
    }
}
