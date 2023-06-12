package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.ExtraTime;
import il.cshaifasweng.OCSFMediatorExample.entities.ScheduledTest;

import java.util.ArrayList;
import java.util.List;

public class extraTimeRequestEvent {

    private List<ExtraTime> data = new ArrayList<>();

    public extraTimeRequestEvent(List<ExtraTime> data) {
        this.data = data;
    }

    public List<ExtraTime> getData() {
        return data;
    }

    public void setData(List<ExtraTime> data) {
        this.data = data;
    }

}
