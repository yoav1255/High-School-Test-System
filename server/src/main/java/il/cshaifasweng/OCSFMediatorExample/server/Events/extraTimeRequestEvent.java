package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

import java.util.ArrayList;
import java.util.List;

public class extraTimeRequestEvent {

    private List<Object> data = new ArrayList<>();

    public extraTimeRequestEvent(List<Object> data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

}
